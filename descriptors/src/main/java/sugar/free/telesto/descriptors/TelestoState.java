package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public enum TelestoState implements Parcelable {

    APP_DISCONNECT_MESSAGE,
    DISCONNECTED,
    WAITING,
    CONNECTING,
    SATL_CONNECTION_REQUEST,
    SATL_KEY_REQUEST,
    SATL_VERIFY_DISPLAY_REQUEST,
    AWAITING_CODE_CONFIRMATION,
    SATL_VERIFY_CONFIRM_REQUEST,
    SATL_SYN_REQUEST,
    APP_CONNECT_MESSAGE,
    APP_BIND_MESSAGE,
    CONNECTED,
    NOT_PAIRED;


    public static final Creator<TelestoState> CREATOR = new Creator<TelestoState>() {
        @Override
        public TelestoState createFromParcel(Parcel in) {
            return values()[in.readInt()];
        }

        @Override
        public TelestoState[] newArray(int size) {
            return new TelestoState[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
