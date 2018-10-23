package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class CartridgeStatus implements Parcelable {

    private boolean inserted;
    private CartridgeType cartridgeType;
    private SymbolStatus symbolStatus;
    private double remainingAmount;

    public static final Creator<CartridgeStatus> CREATOR = new Creator<CartridgeStatus>() {
        @Override
        public CartridgeStatus createFromParcel(Parcel in) {
            return new CartridgeStatus(in);
        }

        @Override
        public CartridgeStatus[] newArray(int size) {
            return new CartridgeStatus[size];
        }
    };

    protected CartridgeStatus(Parcel in) {
        inserted = in.readByte() != 0;
        cartridgeType = in.readParcelable(CartridgeType.class.getClassLoader());
        symbolStatus = in.readParcelable(SymbolStatus.class.getClassLoader());
        remainingAmount = in.readDouble();
    }

    public CartridgeStatus() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (inserted ? 1 : 0));
        parcel.writeParcelable(cartridgeType, i);
        parcel.writeParcelable(symbolStatus, i);
        parcel.writeDouble(remainingAmount);
    }

    public boolean isInserted() {
        return this.inserted;
    }

    public CartridgeType getCartridgeType() {
        return this.cartridgeType;
    }

    public SymbolStatus getSymbolStatus() {
        return this.symbolStatus;
    }

    public double getRemainingAmount() {
        return this.remainingAmount;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public void setCartridgeType(CartridgeType cartridgeType) {
        this.cartridgeType = cartridgeType;
    }

    public void setSymbolStatus(SymbolStatus symbolStatus) {
        this.symbolStatus = symbolStatus;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof CartridgeStatus)) return false;
        final CartridgeStatus other = (CartridgeStatus) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.isInserted() != other.isInserted()) return false;
        final Object this$cartridgeType = this.getCartridgeType();
        final Object other$cartridgeType = other.getCartridgeType();
        if (this$cartridgeType == null ? other$cartridgeType != null : !this$cartridgeType.equals(other$cartridgeType))
            return false;
        final Object this$symbolStatus = this.getSymbolStatus();
        final Object other$symbolStatus = other.getSymbolStatus();
        if (this$symbolStatus == null ? other$symbolStatus != null : !this$symbolStatus.equals(other$symbolStatus))
            return false;
        if (Double.compare(this.getRemainingAmount(), other.getRemainingAmount()) != 0)
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isInserted() ? 79 : 97);
        final Object $cartridgeType = this.getCartridgeType();
        result = result * PRIME + ($cartridgeType == null ? 43 : $cartridgeType.hashCode());
        final Object $symbolStatus = this.getSymbolStatus();
        result = result * PRIME + ($symbolStatus == null ? 43 : $symbolStatus.hashCode());
        final long $remainingAmount = Double.doubleToLongBits(this.getRemainingAmount());
        result = result * PRIME + (int) ($remainingAmount >>> 32 ^ $remainingAmount);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof CartridgeStatus;
    }
}
