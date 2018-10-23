package sugar.free.telesto.services.connection_service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import sugar.free.telesto.parser.utils.ByteBuf;

public class SocketHolder {

    private Callback callback;
    private BluetoothAdapter bluetoothAdapter;
    private boolean pairing;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private SocketConnectorThread socketConnectorThread;
    private SocketReaderThread socketReaderThread;
    private SocketWriterThread socketWriterThread;

    public SocketHolder(Callback callback, BluetoothAdapter bluetoothAdapter, boolean pairing, BluetoothDevice bluetoothDevice, BluetoothSocket bluetoothSocket) {
        this.callback = new CallbackSealing(callback);
        this.bluetoothAdapter = bluetoothAdapter;
        this.pairing = pairing;
        this.bluetoothDevice = bluetoothDevice;
        this.bluetoothSocket = bluetoothSocket;
    }

    public void connect() {
        synchronized (this) {
            socketConnectorThread = new SocketConnectorThread();
            socketConnectorThread.start();
        }
    }

    public void sendBytes(byte[] bytes) {
        synchronized (socketWriterThread.byteBuf) {
            socketWriterThread.byteBuf.putBytes(bytes);
            socketWriterThread.byteBuf.notifyAll();
        }
    }

    public void close() {
        synchronized (this) {
            if (callback instanceof CallbackSealing) ((CallbackSealing) callback).mute();
            if (socketConnectorThread != null) socketConnectorThread.interrupt();
            if (socketReaderThread != null) socketReaderThread.interrupt();
            if (socketWriterThread != null) socketWriterThread.interrupt();
            if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public interface Callback {
        void onSocketCreated(BluetoothSocket bluetoothSocket);
        void onSocketCreationFailed();
        void onConnectionSucceeded();
        void onConnectionFailed(long durationOfAttempt);
        void onConnectionLost();
        void onBytesReceived(byte[] bytes, int length);
    }

    //A pragmatic way to deal with Android's nasty Bluetooth stack
    public class CallbackSealing implements Callback {

        private boolean muted = false;
        private Callback actualCallback;

        public CallbackSealing(Callback actualCallback) {
            this.actualCallback = actualCallback;
        }

        @Override
        public void onSocketCreated(BluetoothSocket bluetoothSocket) {
            synchronized (this) {
                if (!muted) actualCallback.onSocketCreated(bluetoothSocket);
            }
        }

        @Override
        public void onSocketCreationFailed() {
            synchronized (this) {
                if (!muted) actualCallback.onSocketCreationFailed();
            }
        }

        @Override
        public void onConnectionSucceeded() {
            synchronized (this) {
                if (!muted) actualCallback.onConnectionSucceeded();
            }
        }

        @Override
        public void onConnectionFailed(long durationOfAttempt) {
            synchronized (this) {
                if (!muted) actualCallback.onConnectionFailed(durationOfAttempt);
            }
        }

        @Override
        public void onConnectionLost() {
            synchronized (this) {
                if (!muted) actualCallback.onConnectionLost();
            }
        }

        @Override
        public void onBytesReceived(byte[] bytes, int length) {
            synchronized (this) {
                if (!muted) actualCallback.onBytesReceived(bytes, length);
            }
        }

        public void mute() {
            synchronized (this) {
                muted = true;
            }
        }
    }

    private class SocketConnectorThread extends Thread {

        @Override
        public void run() {
            try {
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                return;
            }
            if (pairing) {
                try {
                    Method removeBond = bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null);
                    removeBond.invoke(bluetoothDevice, (Object[]) null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            try {
                synchronized (SocketHolder.this) {
                    if (bluetoothSocket == null) {
                        bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));
                        callback.onSocketCreated(bluetoothSocket);
                    }
                }
            } catch (IOException e) {
                callback.onSocketCreationFailed();
                return;
            }
            long connectionStart = System.currentTimeMillis();
            try {
                bluetoothSocket.connect();
                synchronized (SocketHolder.this) {
                    socketConnectorThread = null;
                    socketReaderThread = new SocketReaderThread(bluetoothSocket.getInputStream());
                    socketWriterThread = new SocketWriterThread(bluetoothSocket.getOutputStream());
                    socketReaderThread.start();
                    socketWriterThread.start();
                }
                callback.onConnectionSucceeded();
            } catch (IOException e) {
                callback.onConnectionFailed(System.currentTimeMillis() - connectionStart);
                try {
                    bluetoothSocket.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    private class SocketReaderThread extends Thread {

        private InputStream inputStream;

        public SocketReaderThread(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                int length;
                while (!isInterrupted()) {
                    if ((length = inputStream.read(buffer)) > 0)
                        callback.onBytesReceived(buffer, length);
                }
            } catch (IOException e) {
                if (!isInterrupted()) callback.onConnectionLost();
            }
        }
    }

    private class SocketWriterThread extends Thread {

        private OutputStream outputStream;
        private final ByteBuf byteBuf = new ByteBuf(1024);

        public SocketWriterThread(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    synchronized (byteBuf) {
                        while (byteBuf.getSize() == 0) byteBuf.wait();
                        outputStream.write(byteBuf.readBytes());
                        outputStream.flush();
                    }
                }
            } catch (IOException e) {
                if (!isInterrupted()) callback.onConnectionLost();
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
