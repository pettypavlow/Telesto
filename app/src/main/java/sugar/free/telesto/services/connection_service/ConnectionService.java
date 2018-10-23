package sugar.free.telesto.services.connection_service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import org.spongycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.activities.LauncherActivity;
import sugar.free.telesto.activities.SetupActivity;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.exceptions.AboutToDisconnectException;
import sugar.free.telesto.exceptions.BluetoothBondRemovedException;
import sugar.free.telesto.exceptions.ConnectionFailedException;
import sugar.free.telesto.exceptions.ConnectionLostException;
import sugar.free.telesto.exceptions.DisconnectedException;
import sugar.free.telesto.exceptions.OrbitalException;
import sugar.free.telesto.exceptions.ReceivedPacketInInvalidStateException;
import sugar.free.telesto.exceptions.RecoveryFailedException;
import sugar.free.telesto.exceptions.SatlPairingRejectedException;
import sugar.free.telesto.exceptions.SatlWrongStateException;
import sugar.free.telesto.exceptions.SocketCreationFailedException;
import sugar.free.telesto.exceptions.TimeoutException;
import sugar.free.telesto.exceptions.app_layer_errors.InvalidServicePasswordException;
import sugar.free.telesto.exceptions.satl_errors.SatlCompatibleStateErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlIncompatibleVersionErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlInvalidCommIdErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlInvalidMessageTypeErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlInvalidPacketErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlInvalidPayloadLengthErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlNoneErrorException;
import sugar.free.telesto.exceptions.satl_errors.SatlUndefinedErrorException;
import sugar.free.telesto.parser.app_layer.AppLayerMessage;
import sugar.free.telesto.parser.app_layer.configuration.CloseConfigurationWriteSessionMessage;
import sugar.free.telesto.parser.app_layer.configuration.OpenConfigurationWriteSessionMessage;
import sugar.free.telesto.parser.app_layer.configuration.WriteConfigurationBlockMessage;
import sugar.free.telesto.parser.app_layer.connection.ActivateServiceMessage;
import sugar.free.telesto.parser.app_layer.connection.BindMessage;
import sugar.free.telesto.parser.app_layer.connection.ConnectMessage;
import sugar.free.telesto.parser.app_layer.connection.DisconnectMessage;
import sugar.free.telesto.parser.app_layer.connection.ServiceChallengeMessage;
import sugar.free.telesto.parser.ids.ServiceIDs;
import sugar.free.telesto.parser.satl.ConnectionRequest;
import sugar.free.telesto.parser.satl.ConnectionResponse;
import sugar.free.telesto.parser.satl.DataMessage;
import sugar.free.telesto.parser.satl.ErrorMessage;
import sugar.free.telesto.parser.satl.KeyRequest;
import sugar.free.telesto.parser.satl.KeyResponse;
import sugar.free.telesto.parser.satl.PairingStatus;
import sugar.free.telesto.parser.satl.SatlMessage;
import sugar.free.telesto.parser.satl.SynAckResponse;
import sugar.free.telesto.parser.satl.SynRequest;
import sugar.free.telesto.parser.satl.VerifyConfirmRequest;
import sugar.free.telesto.parser.satl.VerifyConfirmResponse;
import sugar.free.telesto.parser.satl.VerifyDisplayRequest;
import sugar.free.telesto.parser.satl.VerifyDisplayResponse;
import sugar.free.telesto.parser.utils.ByteBuf;
import sugar.free.telesto.parser.utils.Nonce;
import sugar.free.telesto.parser.utils.crypto.Cryptograph;
import sugar.free.telesto.parser.utils.crypto.DerivedKeys;
import sugar.free.telesto.parser.utils.crypto.KeyPair;
import sugar.free.telesto.utils.NotificationUtil;
import sugar.free.telesto.utils.PairingDataStorage;

public class ConnectionService extends Service implements SocketHolder.Callback {

    private static final long RESPONSE_TIMEOUT = 6000;
    private static final int NONCE_RECOVERY_INCREASE = 50;

    private List<StateCallback> stateCallbacks = Collections.synchronizedList(new ArrayList<>());
    private LocalBinder localBinder = new LocalBinder();
    private final Object $stateLock = new Object[0];
    private TelestoState state;
    private PairingDataStorage pairingDataStorage;
    private PowerManager.WakeLock wakeLock;
    private final List<Object> connectionRequests = new ArrayList<>();
    private SocketHolder socketHolder;
    private DelayedActionThread disconnectionAwaiter;
    private DelayedActionThread recoveryAwaiter;
    private DelayedActionThread timeoutThread;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private boolean receiverRegistered;
    private SatlMessage lastSatlMessage;
    private KeyRequest keyRequest;
    private int socketAttempts = 0;
    private long waitingTime = 0;
    private ByteBuf byteBuf = new ByteBuf(1024);
    private int recoveryAttempts = 0;
    private String verificationString;
    private SetupActivity setupActivity;
    private boolean disconnectAfterMessage;

    private KeyPair keyPair;
    private byte[] randomBytes;

    private MessageRequest activeRequest;
    private final List<MessageRequest> messageRequests = new ArrayList<>();
    private final List<sugar.free.telesto.parser.app_layer.Service> activatedServices = new ArrayList<>();

    private KeyPair getKeyPair() {
        if  (keyPair == null) keyPair = Cryptograph.generateRSAKey();
        return keyPair;
    }

    private byte[] getRandomBytes() {
        if (randomBytes == null) {
            randomBytes = new byte[28];
            new SecureRandom().nextBytes(randomBytes);
        }
        return randomBytes;
    }

    public void registerStateCallback(StateCallback stateCallback) {
        stateCallbacks.add(stateCallback);
    }

    public void unregisterStateCallback(StateCallback stateCallback) {
        stateCallbacks.remove(stateCallback);
    }

    public void setSetupActivity(SetupActivity setupActivity) {
        this.setupActivity = setupActivity;
    }

    public MessageRequest requestMessage(AppLayerMessage message) {
        MessageRequest messageRequest = new MessageRequest(message);
        if (getState() != TelestoState.CONNECTED) {
            messageRequest.exception = new DisconnectedException();
            return messageRequest;
        }
        synchronized (messageRequests) {
            if (message instanceof WriteConfigurationBlockMessage)
                messageRequests.add(new MessageRequest(new OpenConfigurationWriteSessionMessage()));
            messageRequests.add(messageRequest);
            if (message instanceof WriteConfigurationBlockMessage)
                messageRequests.add(new MessageRequest(new CloseConfigurationWriteSessionMessage()));
            Collections.sort(messageRequests);
            if (activeRequest == null) requestNextMessage();
        }
        return messageRequest;
    }

    private void requestNextMessage() {
        synchronized (messageRequests) {
            if (messageRequests.size() > 0) {
                activeRequest = messageRequests.get(0);
                messageRequests.remove(0);
                sugar.free.telesto.parser.app_layer.Service service = activeRequest.request.getService();
                if (service != sugar.free.telesto.parser.app_layer.Service.CONNECTION && !activatedServices.contains(service)) {
                    if (service.getServicePassword() == null) {
                        ActivateServiceMessage activateServiceMessage = new ActivateServiceMessage();
                        activateServiceMessage.setServiceID(ServiceIDs.IDS.getB(service));
                        activateServiceMessage.setVersion(service.getVersion());
                        activateServiceMessage.setServicePassword(new byte[16]);
                        sendAppLayerMessage(activateServiceMessage);
                    } else if (service.getServicePassword().length() != 16) {
                        completeActiveRequest(new InvalidServicePasswordException(0));
                    } else {
                        ServiceChallengeMessage serviceChallengeMessage = new ServiceChallengeMessage();
                        serviceChallengeMessage.setServiceID(ServiceIDs.IDS.getB(service));
                        serviceChallengeMessage.setVersion(service.getVersion());
                        sendAppLayerMessage(serviceChallengeMessage);
                    }
                } else sendAppLayerMessage(activeRequest.getRequest());
            }
        }
    }

    private void completeActiveRequest(AppLayerMessage response) {
        synchronized (messageRequests) {
            if (activeRequest == null) return;
            synchronized (activeRequest) {
                activeRequest.response = response;
                activeRequest.notifyAll();
            }
            activeRequest = null;
            requestNextMessage();
        }
    }

    private void completeActiveRequest(Exception exception) {
        synchronized (messageRequests) {
            if (activeRequest == null) return;
            synchronized (activeRequest) {
                activeRequest.exception = exception;
                activeRequest.notifyAll();
            }
            activeRequest = null;
            requestNextMessage();
        }
    }

    private void completeAllRequests(Exception exception) {
        synchronized (messageRequests) {
            if (activeRequest != null) {
                synchronized (activeRequest) {
                    activeRequest.exception = exception;
                    activeRequest.notifyAll();
                }
                activeRequest = null;
            }
            for (MessageRequest messageRequest : messageRequests) {
                synchronized (messageRequest) {
                    messageRequest.exception = exception;
                    messageRequest.notifyAll();
                }
            }
            messageRequests.clear();
        }
    }

    @Override
    public void onCreate() {
        pairingDataStorage = new PairingDataStorage(this);
        state = pairingDataStorage.isPaired() ? TelestoState.DISCONNECTED : TelestoState.NOT_PAIRED;
        wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Telesto:ConnectionService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public TelestoState getState() {
        synchronized ($stateLock) {
            return state;
        }
    }

    private void setState(TelestoState state) {
        synchronized ($stateLock) {
            if ((state == TelestoState.DISCONNECTED || state == TelestoState.NOT_PAIRED) && wakeLock.isHeld()) wakeLock.release();
            else if (!wakeLock.isHeld()) wakeLock.acquire();
            for (StateCallback stateCallback : stateCallbacks) stateCallback.stateChanged(state);
            this.state = state;
        }
    }

    public void requestConnection(Object lock) {
        synchronized (connectionRequests) {
            connectionRequests.add(lock);
            Log.d("ConnectionService", "Connection requested: " + lock);
            if (disconnectionAwaiter != null) disconnectionAwaiter.interrupt();
            disconnectAfterMessage = false;
            TelestoState state = getState();
            if ((state == TelestoState.DISCONNECTED || state == TelestoState.APP_DISCONNECT_MESSAGE) && pairingDataStorage.isPaired()) initiateConnection();
        }
    }

    public void withdrawConnectionRequest(Object lock) {
        synchronized (connectionRequests) {
            connectionRequests.remove(lock);
            Log.d("ConnectionService", "Connection request withdrawn: " + lock);
            if (connectionRequests.size() == 0) {
                if (recoveryAwaiter != null) {
                    recoveryAwaiter.interrupt();
                    recoveryAwaiter = null;
                    setState(TelestoState.DISCONNECTED);
                } else if (connectionRequests.size() == 0 && (getState() != TelestoState.NOT_PAIRED || getState() != TelestoState.APP_DISCONNECT_MESSAGE || getState() != TelestoState.DISCONNECTED)) {
                    long disconnectionDelay = Math.max(1000, Math.min(30000, Long.parseLong(TelestoApp.getSharedPreferences().getString("disconnectionDelay", "10000"))));
                    Log.i("DisconnectionDelay", disconnectionDelay + "");
                    disconnectionAwaiter = DelayedActionThread.runDelayed(disconnectionDelay, this::disconnectionDelayExceeded);
                }
            }
        }
    }

    private void runRecoveryStrategies(boolean enforce) {
        if (TelestoApp.getSharedPreferences().getBoolean("waitBeforeRetry", true)) {
            long maxWaitingTime = Math.max(0, Math.min(60000, Long.parseLong(TelestoApp.getSharedPreferences().getString("maxWaitingTime", "20000"))));
            long minWaitingTime = Math.max(0, Math.min(30000, Long.parseLong(TelestoApp.getSharedPreferences().getString("minWaitingTime", "4000"))));
            if (enforce) waitingTime = maxWaitingTime;
            else if (waitingTime < minWaitingTime) waitingTime = minWaitingTime;
            else waitingTime += 1000;
            if (waitingTime > maxWaitingTime) waitingTime = maxWaitingTime;
        } else waitingTime = 0;
        socketAttempts++;
        if (TelestoApp.getSharedPreferences().getBoolean("allowSocketResets", true)
                && (socketAttempts > Math.max(0, Integer.parseInt(TelestoApp.getSharedPreferences().getString("maxSocketAttempts", "10"))) || enforce)) {
            Log.d("ConnectionService", "Exceeded maxSocketAttempts, discarding socket...");
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                }
                bluetoothSocket = null;
            }
            socketAttempts = 0;
        }
    }

    private void initiateConnection() {
        if (waitingTime == 0) connect();
        else {
            setState(TelestoState.WAITING);
            recoveryAwaiter = DelayedActionThread.runDelayed(waitingTime, this::connect);
        }
    }

    private void initiateDisconnection() {
        if (getState() == TelestoState.CONNECTED) {
            synchronized (messageRequests) {
                disconnectAfterMessage = true;
                if (activeRequest == null) {
                    setState(TelestoState.APP_DISCONNECT_MESSAGE);
                    completeAllRequests(new AboutToDisconnectException());
                    sendAppLayerMessage(new DisconnectMessage());
                }
            }
        } else disconnect();
    }

    private void connect() {
        recoveryAwaiter = null;
        if (bluetoothDevice == null) bluetoothDevice = bluetoothAdapter.getRemoteDevice(pairingDataStorage.getMacAddress());
        if (pairingDataStorage.isPaired() && bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
            handleConnectionRelatedException(new BluetoothBondRemovedException(), true);
            return;
        }
        setState(TelestoState.CONNECTING);
        socketHolder = new SocketHolder(this, bluetoothAdapter, !pairingDataStorage.isPaired(), bluetoothDevice, bluetoothSocket);
        socketHolder.connect();
    }

    private void cleanup() {
        if (socketHolder != null) {
            socketHolder.close();
            socketHolder = null;
        }
        if (disconnectionAwaiter != null) {
            disconnectionAwaiter.interrupt();
            disconnectionAwaiter = null;
        }
        if (recoveryAwaiter != null) {
            recoveryAwaiter.interrupt();
            recoveryAwaiter = null;
        }
        if (timeoutThread != null) {
            timeoutThread.interrupt();
            timeoutThread = null;
        }
        if (receiverRegistered) {
            unregisterReceiver(broadcastReceiver);
            receiverRegistered = false;
        }
        lastSatlMessage = null;
        keyRequest = null;
        byteBuf = new ByteBuf(1024);
        verificationString = null;
        disconnectAfterMessage = false;
        messageRequests.clear();
        activeRequest = null;
        activatedServices.clear();
    }

    public void reset() {
        Log.d("ConnectionService", "Reset");
        cleanup();
        socketAttempts = 0;
        waitingTime = 0;
        recoveryAttempts = 0;
        bluetoothSocket = null;
        bluetoothDevice = null;
        setState(TelestoState.NOT_PAIRED);
        pairingDataStorage.reset();
    }

    private void disconnect() {
        Log.d("ConnectionService", "Disconnect");
        if (disconnectionAwaiter != null) {
            disconnectionAwaiter.interrupt();
            disconnectionAwaiter = null;
        }
        setState(TelestoState.DISCONNECTED);
        cleanup();
    }

    public synchronized void pair(String macAddress) {
        Log.d("ConnectionService", "Pairing with " + macAddress);
        pairingDataStorage.setMacAddress(macAddress);
        connect();
    }

    public synchronized void confirmVerificationString() {
        Log.d("ConnectionService", "Verification string confirmed");
        VerifyConfirmRequest verifyConfirmRequest = new VerifyConfirmRequest();
        verifyConfirmRequest.setPairingStatus(PairingStatus.CONFIRMED);
        setState(TelestoState.SATL_VERIFY_CONFIRM_REQUEST);
        sendSatlMessage(verifyConfirmRequest, true);
    }

    public synchronized void rejectVerificationString() {
        Log.d("ConnectionService", "Verification string rejected");
        VerifyConfirmRequest verifyConfirmRequest = new VerifyConfirmRequest();
        verifyConfirmRequest.setPairingStatus(PairingStatus.REJECTED);
        sendSatlMessage(verifyConfirmRequest, true);
        handleConnectionRelatedException(new SatlPairingRejectedException(), true);
    }

    private void disconnectionDelayExceeded() {
        Log.d("ConnectionService", "Disconnection delay exceeded");
        disconnectionAwaiter = null;
        initiateDisconnection();
    }

    private void handleConnectionRelatedException(Exception exception, boolean critical) {
        if (critical) Log.d("ConnectionService", "Critical exception occurred: " + exception.getClass().getCanonicalName());
        else Log.d("ConnectionService", "Exception occurred: " + exception.getClass().getCanonicalName());
        cleanup();
        if (!pairingDataStorage.isPaired()) {
            if (setupActivity != null) setupActivity.displayException(exception);
            reset();
        } else if (critical) {
            reset();
            NotificationUtil.showCriticalErrorNotification(this);
            Intent intent = new Intent(this, LauncherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (connectionRequests.size() > 0) initiateConnection();
    }

    @Override
    public void onSocketCreated(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }

    @Override
    public void onSocketCreationFailed() {
        handleConnectionRelatedException(new SocketCreationFailedException(), false);
    }

    @Override
    public void onConnectionSucceeded() {
        Log.d("ConnectionService", "Connection succeeded");
        if (!receiverRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            registerReceiver(broadcastReceiver, intentFilter);
            receiverRegistered = true;
        }
        if (pairingDataStorage.isPaired()) {
            setState(TelestoState.SATL_SYN_REQUEST);
            sendSatlMessage(new SynRequest(), true);
        } else {
            setState(TelestoState.SATL_CONNECTION_REQUEST);
            sendSatlMessage(new ConnectionRequest(), true);
        }
    }

    @Override
    public void onConnectionFailed(long durationOfAttempt) {
        Log.d("ConnectionService", "Connection failed");
        runRecoveryStrategies(durationOfAttempt < 1000);
        handleConnectionRelatedException(new ConnectionFailedException(), false);
    }

    @Override
    public void onConnectionLost() {
        Log.d("ConnectionService", "Connection lost");
        if (receiverRegistered) {
            unregisterReceiver(broadcastReceiver);
            receiverRegistered = false;
        }
        handleConnectionRelatedException(new ConnectionLostException(), false);
    }

    @Override
    public void onBytesReceived(byte[] bytes, int length) {
        byteBuf.putBytes(bytes, length);
        while (SatlMessage.hasCompletePacket(byteBuf)) {
            try {
                SatlMessage satlMessage = SatlMessage.deserialize(byteBuf, pairingDataStorage.getLastNonceReceived(), pairingDataStorage.getIncomingKey());
                processSatlMessage(satlMessage);
            } catch (OrbitalException e) {
                handleConnectionRelatedException(e, false);
            }
        }
    }

    public void processSatlMessage(SatlMessage satlMessage) {
        Log.d("ConnectionService", "Received SatlMessage: " + satlMessage.getClass().getSimpleName());
        if (timeoutThread != null) {
            timeoutThread.interrupt();
            timeoutThread = null;
        }
        pairingDataStorage.setLastNonceReceived(satlMessage.getNonce());
        if (!(satlMessage instanceof ErrorMessage)) recoveryAttempts = 0;
        if (satlMessage instanceof ConnectionResponse) processConnectionResponse();
        else if (satlMessage instanceof KeyResponse) processKeyResponse((KeyResponse) satlMessage);
        else if (satlMessage instanceof VerifyDisplayResponse) processVerifyDisplayResponse();
        else if (satlMessage instanceof VerifyConfirmResponse)
            processVerifyConfirmResponse((VerifyConfirmResponse) satlMessage);
        else if (satlMessage instanceof DataMessage) processDataMessage((DataMessage) satlMessage);
        else if (satlMessage instanceof SynAckResponse) processSynAckResponse();
        else if (satlMessage instanceof ErrorMessage)
            processErrorMessage((ErrorMessage) satlMessage);
    }

    private void processConnectionResponse() {
        if (getState() != TelestoState.SATL_CONNECTION_REQUEST) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), true);
            return;
        }
        keyRequest = new KeyRequest();
        keyRequest.setPreMasterKey(getKeyPair().getPublicKeyBytes());
        keyRequest.setRandomBytes(getRandomBytes());
        setState(TelestoState.SATL_KEY_REQUEST);
        sendSatlMessage(keyRequest, true);
    }

    private void processKeyResponse(KeyResponse keyResponse) {
        if (getState() != TelestoState.SATL_KEY_REQUEST) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), true);
            return;
        }
        try {
            DerivedKeys derivedKeys = Cryptograph.deriveKeys(Cryptograph.combine(keyRequest.getSatlContent(), keyResponse.getSatlContent()),
                    Cryptograph.decryptRSA(keyPair.getPrivateKey(), keyResponse.getPreMasterSecret()),
                    randomBytes,
                    keyResponse.getRandomData());
            pairingDataStorage.setCommId(keyResponse.getCommID());
            keyRequest = null;
            verificationString = derivedKeys.getVerificationString();
            pairingDataStorage.setOutgoingKey(derivedKeys.getOutgoingKey());
            pairingDataStorage.setIncomingKey(derivedKeys.getIncomingKey());
            pairingDataStorage.setLastNonceSent(new Nonce());
            setState(TelestoState.SATL_VERIFY_DISPLAY_REQUEST);
            sendSatlMessage(new VerifyDisplayRequest(), true);
        } catch (InvalidCipherTextException e) {
            handleConnectionRelatedException(e, true);
        }
    }

    private void processVerifyDisplayResponse() {
        if (getState() != TelestoState.SATL_VERIFY_DISPLAY_REQUEST) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), true);
            return;
        }
        setState(TelestoState.AWAITING_CODE_CONFIRMATION);
        if (setupActivity != null) setupActivity.showVerificationString(verificationString);
        verificationString = null;
    }

    private void processVerifyConfirmResponse(VerifyConfirmResponse verifyConfirmResponse) {
        if (getState() != TelestoState.SATL_VERIFY_CONFIRM_REQUEST) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), true);
            return;
        }
        switch (verifyConfirmResponse.getPairingStatus()) {
            case CONFIRMED:
                setState(TelestoState.APP_BIND_MESSAGE);
                sendAppLayerMessage(new BindMessage());
                break;
            case PENDING:
                try {
                    Thread.sleep(200);
                    VerifyConfirmRequest verifyConfirmRequest = new VerifyConfirmRequest();
                    verifyConfirmRequest.setPairingStatus(PairingStatus.CONFIRMED);
                    sendSatlMessage(verifyConfirmRequest, true);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
            case REJECTED:
                handleConnectionRelatedException(new SatlPairingRejectedException(), true);
                break;
        }
    }

    private void processSynAckResponse() {
        if (getState() != TelestoState.SATL_SYN_REQUEST) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), true);
            return;
        }
        setState(TelestoState.APP_CONNECT_MESSAGE);
        sendAppLayerMessage(new ConnectMessage());
    }

    private void processDataMessage(DataMessage dataMessage) {
        TelestoState state = getState();
        switch (state) {
            case CONNECTED:
            case APP_BIND_MESSAGE:
            case APP_CONNECT_MESSAGE:
            case APP_DISCONNECT_MESSAGE:
                break;
            default:
                handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), false);
                return;
        }
        try {
            AppLayerMessage appLayerMessage = AppLayerMessage.unwrap(dataMessage);
            Log.d("ConnectionService", "Received AppLayerMessage: " + appLayerMessage.getClass().getSimpleName());
            if (!(appLayerMessage instanceof DisconnectMessage)) {
                if (disconnectAfterMessage) {
                    setState(TelestoState.APP_DISCONNECT_MESSAGE);
                    completeAllRequests(new AboutToDisconnectException());
                    sendAppLayerMessage(new DisconnectMessage());
                }
                if (appLayerMessage instanceof BindMessage) processBindMessage();
                else if (appLayerMessage instanceof ConnectMessage) processConnectMessage();
                else if (appLayerMessage instanceof ActivateServiceMessage) processActivateServiceMessage();
                else if (appLayerMessage instanceof ServiceChallengeMessage) processServiceChallengeMessage((ServiceChallengeMessage) appLayerMessage);
                else if (!(appLayerMessage instanceof sugar.free.telesto.parser.app_layer.connection.DisconnectMessage)) processGenericAppLayerMessage(appLayerMessage);
            } else processDisconnectMessage();
        } catch (Exception e) {
            if (getState() != TelestoState.CONNECTED) handleConnectionRelatedException(e, true);
            else {
                Log.d("ConnectionService", "Got exception while processing request: " + e.getClass().getCanonicalName());
                synchronized (messageRequests) {
                    if (disconnectAfterMessage) {
                        synchronized (activeRequest) {
                            activeRequest.exception = e;
                            activeRequest.notifyAll();
                            activeRequest = null;
                        }
                        setState(TelestoState.APP_DISCONNECT_MESSAGE);
                        completeAllRequests(new AboutToDisconnectException());
                        sendAppLayerMessage(new DisconnectMessage());
                    } else completeActiveRequest(e);
                }
            }
        }
    }

    private void processBindMessage() {
        if (getState() != TelestoState.APP_BIND_MESSAGE) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), true);
            return;
        }
        pairingDataStorage.setPaired(true);
        setState(TelestoState.CONNECTED);
    }

    private void processConnectMessage() {
        if (getState() != TelestoState.APP_CONNECT_MESSAGE) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), false);
            return;
        }
        setState(TelestoState.CONNECTED);
    }

    private void processDisconnectMessage() {
        if (getState() != TelestoState.APP_DISCONNECT_MESSAGE) {
            handleConnectionRelatedException(new ReceivedPacketInInvalidStateException(), false);
            return;
        }
        activatedServices.clear();
        if (!disconnectAfterMessage) {
            setState(TelestoState.APP_CONNECT_MESSAGE);
            sendAppLayerMessage(new ConnectMessage());
        } else disconnect();
    }

    private void processActivateServiceMessage() {
        synchronized (messageRequests) {
            activatedServices.add(activeRequest.request.getService());
            if (disconnectAfterMessage) {
                setState(TelestoState.APP_DISCONNECT_MESSAGE);
                completeAllRequests(new AboutToDisconnectException());
                sendAppLayerMessage(new DisconnectMessage());
            } else sendAppLayerMessage(activeRequest.request);
        }
    }

    private void processServiceChallengeMessage(ServiceChallengeMessage serviceChallengeMessage) {
        synchronized (messageRequests) {
            if (disconnectAfterMessage) {
                setState(TelestoState.APP_DISCONNECT_MESSAGE);
                completeAllRequests(new AboutToDisconnectException());
                sendAppLayerMessage(new DisconnectMessage());
            } else {
                sugar.free.telesto.parser.app_layer.Service service = activeRequest.request.getService();
                ActivateServiceMessage activateServiceMessage = new ActivateServiceMessage();
                activateServiceMessage.setServiceID(ServiceIDs.IDS.getB(service));
                activateServiceMessage.setVersion(service.getVersion());
                activateServiceMessage.setServicePassword(Cryptograph.getServicePasswordHash(service.getServicePassword(), serviceChallengeMessage.getRandomData()));
                sendAppLayerMessage(activateServiceMessage);
            }
        }
    }

    private synchronized void processGenericAppLayerMessage(AppLayerMessage appLayerMessage) {
        synchronized (messageRequests) {
            if (disconnectAfterMessage) {
                synchronized (activeRequest) {
                    activeRequest.response = appLayerMessage;
                    activeRequest.notifyAll();
                    activeRequest = null;
                }
                setState(TelestoState.APP_DISCONNECT_MESSAGE);
                completeAllRequests(new AboutToDisconnectException());
                sendAppLayerMessage(new DisconnectMessage());
            } else completeActiveRequest(appLayerMessage);
        }
    }

    private void processErrorMessage(ErrorMessage errorMessage) {
        switch (errorMessage.getError()) {
            case INVALID_NONCE:
                if (state == TelestoState.SATL_SYN_REQUEST) {
                    Nonce nonce = pairingDataStorage.getLastNonceSent();
                    nonce.increment(NONCE_RECOVERY_INCREASE - 1);
                    pairingDataStorage.setLastNonceSent(nonce);
                    lastSatlMessage.setNonce(nonce);
                } else {
                    handleConnectionRelatedException(new RecoveryFailedException(), true);
                    break;
                }
            case INVALID_CRC:
            case INVALID_MAC_TRAILER:
            case DECRYPT_VERIFY_FAILED:
                recoveryAttempts++;
                if (recoveryAttempts <= 10) sendSatlMessage(lastSatlMessage, false);
                else handleConnectionRelatedException(new RecoveryFailedException(), true);
                break;
            case INVALID_PAYLOAD_LENGTH:
                handleConnectionRelatedException(new SatlInvalidPayloadLengthErrorException(), false);
                break;
            case INVALID_MESSAGE_TYPE:
                handleConnectionRelatedException(new SatlInvalidMessageTypeErrorException(), false);
                break;
            case INCOMPATIBLE_VERSION:
                handleConnectionRelatedException(new SatlIncompatibleVersionErrorException(), true);
                break;
            case COMPATIBLE_STATE:
                handleConnectionRelatedException(new SatlCompatibleStateErrorException(), true);
                break;
            case INVALID_COMM_ID:
                handleConnectionRelatedException(new SatlInvalidCommIdErrorException(), true);
                break;
            case INVALID_PACKET:
                handleConnectionRelatedException(new SatlInvalidPacketErrorException(), false);
                break;
            case WRONG_STATE:
                handleConnectionRelatedException(new SatlWrongStateException(), true);
                break;
            case UNDEFINED:
                handleConnectionRelatedException(new SatlUndefinedErrorException(), true);
                break;
            case NONE:
                handleConnectionRelatedException(new SatlNoneErrorException(), true);
                break;
        }
    }

    private void sendSatlMessage(SatlMessage satlMessage, boolean prepare) {
        if (socketHolder == null) return;
        this.lastSatlMessage = satlMessage;
        if (prepare) {
            satlMessage.setCommID(pairingDataStorage.getCommId());
            Nonce nonce = pairingDataStorage.getLastNonceSent();
            if (nonce != null) {
                nonce.increment();
                pairingDataStorage.setLastNonceSent(nonce);
            }
            satlMessage.setNonce(nonce);
        }
        ByteBuf serialized = satlMessage.serialize(satlMessage.getClass(), pairingDataStorage.getOutgoingKey());
        if (timeoutThread != null) timeoutThread.interrupt();
        timeoutThread = DelayedActionThread.runDelayed(RESPONSE_TIMEOUT, () -> handleConnectionRelatedException(new TimeoutException(), false));
        socketHolder.sendBytes(serialized.getBytes());
    }

    private void sendAppLayerMessage(AppLayerMessage appLayerMessage) {
        sendSatlMessage(AppLayerMessage.wrap(appLayerMessage), true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (pairingDataStorage.getMacAddress() != null && pairingDataStorage.getMacAddress()
                    .equals(((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)).getAddress())) {
                handleConnectionRelatedException(new ConnectionLostException(), false);
                Log.d("ConnectionService", "ACL Disconnect");
            }
        }
    };

    public class MessageRequest implements Comparable<MessageRequest> {

        private AppLayerMessage request;
        private AppLayerMessage response;
        private Exception exception;

        private MessageRequest(AppLayerMessage request) {
            this.request = request;
        }

        public AppLayerMessage await() throws Exception {
            synchronized (this) {
                while (exception == null && response == null) wait();
                if (exception != null) throw exception;
                return response;
            }
        }

        @Override
        public int compareTo(MessageRequest messageRequest) {
            return request.compareTo(messageRequest.request);
        }

        public AppLayerMessage getRequest() {
            return this.request;
        }

        public AppLayerMessage getResponse() {
            return this.response;
        }

        public Exception getException() {
            return this.exception;
        }
    }

    public class LocalBinder extends Binder {
        public ConnectionService getService() {
            return ConnectionService.this;
        }
    }

    public interface StateCallback {
        void stateChanged(TelestoState state);
    }
}
