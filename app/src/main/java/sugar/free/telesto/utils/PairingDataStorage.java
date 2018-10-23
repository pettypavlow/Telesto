package sugar.free.telesto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import sugar.free.telesto.parser.utils.Nonce;

public class PairingDataStorage {

    private final static char[] hexValues = "0123456789ABCDEF".toCharArray();

    private SharedPreferences preferences;

    private boolean paired;
    private String macAddress;
    private Nonce lastNonceSent;
    private Nonce lastNonceReceived;
    private long commId;
    byte[] incomingKey;
    byte[] outgoingKey;

    public PairingDataStorage(Context context) {
        this.preferences = context.getSharedPreferences(context.getPackageName() + ".PAIRING_DATA_STORAGE", Context.MODE_PRIVATE);
        paired = preferences.getBoolean("paired", false);
        macAddress = preferences.getString("macAddress", null);
        String lastNonceSentHex = preferences.getString("lastNonceSent", null);
        if (lastNonceSentHex != null) lastNonceSent = new Nonce(fromHexString(lastNonceSentHex));
        String lastNonceReceivedHex = preferences.getString("lastNonceReceived", null);
        if (lastNonceReceivedHex != null) lastNonceReceived = new Nonce(fromHexString(lastNonceReceivedHex));
        commId = preferences.getLong("commId", 0);
        incomingKey = fromHexString(preferences.getString("incomingKey", null));
        outgoingKey = fromHexString(preferences.getString("outgoingKey", null));
    }

    private static String toHexString(byte[] bytes) {
        if (bytes == null) return null;
        char[] hexRepresentation = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            hexRepresentation[i * 2] = hexValues[value >>> 4];
            hexRepresentation[i * 2 + 1] = hexValues[value & 0x0F];
        }
        return new String(hexRepresentation);
    }

    private static byte[] fromHexString(String hexString) {
        if (hexString == null) return null;
        byte[] bytes = new byte[hexString.length() / 2];
        int length = hexString.length();
        for (int i = 0; i < length; i += 2)
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        return bytes;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
        preferences.edit().putBoolean("paired", paired).apply();
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        preferences.edit().putString("macAddress", macAddress).apply();
    }

    public void setLastNonceSent(Nonce lastNonceSent) {
        this.lastNonceSent = lastNonceSent;
        preferences.edit().putString("lastNonceSent", lastNonceSent == null ? null : toHexString(lastNonceSent.getStorageValue())).apply();
    }

    public void setLastNonceReceived(Nonce lastNonceReceived) {
        this.lastNonceReceived = lastNonceReceived;
        preferences.edit().putString("lastNonceReceived", lastNonceReceived == null ? null : toHexString(lastNonceReceived.getStorageValue())).apply();
    }

    public void setCommId(long commId) {
        this.commId = commId;
        preferences.edit().putLong("commId", commId).apply();
    }

    public void setIncomingKey(byte[] incomingKey) {
        this.incomingKey = incomingKey;
        preferences.edit().putString("incomingKey", toHexString(incomingKey)).apply();
    }

    public void setOutgoingKey(byte[] outgoingKey) {
        this.outgoingKey = outgoingKey;
        preferences.edit().putString("outgoingKey", toHexString(outgoingKey)).apply();
    }

    public SharedPreferences getPreferences() {
        return this.preferences;
    }

    public boolean isPaired() {
        return this.paired;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public Nonce getLastNonceSent() {
        return this.lastNonceSent;
    }

    public Nonce getLastNonceReceived() {
        return this.lastNonceReceived;
    }

    public long getCommId() {
        return this.commId;
    }

    public byte[] getIncomingKey() {
        return this.incomingKey;
    }

    public byte[] getOutgoingKey() {
        return this.outgoingKey;
    }

    public void reset() {
        setPaired(false);
        setMacAddress(null);
        setCommId(0);
        setIncomingKey(null);
        setOutgoingKey(null);
        setLastNonceReceived(null);
        setLastNonceSent(null);
    }
}
