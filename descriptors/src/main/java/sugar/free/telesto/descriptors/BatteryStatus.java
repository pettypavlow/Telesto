package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class BatteryStatus implements Parcelable {

    private BatteryType batteryType;
    private int batteryAmount;
    private SymbolStatus symbolStatus;

    protected BatteryStatus(Parcel in) {
        batteryType = in.readParcelable(BatteryType.class.getClassLoader());
        batteryAmount = in.readInt();
        symbolStatus = in.readParcelable(SymbolStatus.class.getClassLoader());
    }

    public static final Creator<BatteryStatus> CREATOR = new Creator<BatteryStatus>() {
        @Override
        public BatteryStatus createFromParcel(Parcel in) {
            return new BatteryStatus(in);
        }

        @Override
        public BatteryStatus[] newArray(int size) {
            return new BatteryStatus[size];
        }
    };

    public BatteryStatus() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(batteryType, i);
        parcel.writeInt(batteryAmount);
        parcel.writeParcelable(symbolStatus, i);
    }

    public BatteryType getBatteryType() {
        return this.batteryType;
    }

    public int getBatteryAmount() {
        return this.batteryAmount;
    }

    public SymbolStatus getSymbolStatus() {
        return this.symbolStatus;
    }

    public void setBatteryType(BatteryType batteryType) {
        this.batteryType = batteryType;
    }

    public void setBatteryAmount(int batteryAmount) {
        this.batteryAmount = batteryAmount;
    }

    public void setSymbolStatus(SymbolStatus symbolStatus) {
        this.symbolStatus = symbolStatus;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BatteryStatus)) return false;
        final BatteryStatus other = (BatteryStatus) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$batteryType = this.getBatteryType();
        final Object other$batteryType = other.getBatteryType();
        if (this$batteryType == null ? other$batteryType != null : !this$batteryType.equals(other$batteryType))
            return false;
        if (this.getBatteryAmount() != other.getBatteryAmount()) return false;
        final Object this$symbolStatus = this.getSymbolStatus();
        final Object other$symbolStatus = other.getSymbolStatus();
        if (this$symbolStatus == null ? other$symbolStatus != null : !this$symbolStatus.equals(other$symbolStatus))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $batteryType = this.getBatteryType();
        result = result * PRIME + ($batteryType == null ? 43 : $batteryType.hashCode());
        result = result * PRIME + this.getBatteryAmount();
        final Object $symbolStatus = this.getSymbolStatus();
        result = result * PRIME + ($symbolStatus == null ? 43 : $symbolStatus.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof BatteryStatus;
    }
}
