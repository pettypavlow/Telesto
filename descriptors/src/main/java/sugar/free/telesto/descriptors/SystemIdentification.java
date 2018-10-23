package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class SystemIdentification implements Parcelable {

    private String serialNumber;
    private int systemIdAppendix;
    private String manufacturingDate;

    protected SystemIdentification(Parcel in) {
        serialNumber = in.readString();
        systemIdAppendix = in.readInt();
        manufacturingDate = in.readString();
    }

    public static final Creator<SystemIdentification> CREATOR = new Creator<SystemIdentification>() {
        @Override
        public SystemIdentification createFromParcel(Parcel in) {
            return new SystemIdentification(in);
        }

        @Override
        public SystemIdentification[] newArray(int size) {
            return new SystemIdentification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serialNumber);
        parcel.writeInt(systemIdAppendix);
        parcel.writeString(manufacturingDate);
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public int getSystemIdAppendix() {
        return this.systemIdAppendix;
    }

    public String getManufacturingDate() {
        return this.manufacturingDate;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setSystemIdAppendix(int systemIdAppendix) {
        this.systemIdAppendix = systemIdAppendix;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }
}
