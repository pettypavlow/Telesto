package sugar.free.telesto.descriptors;

import android.os.Parcel;
import android.os.Parcelable;

public class Alert implements Parcelable {

    private int alertId;
    private AlertCategory alertCategory;
    private AlertType alertType;
    private AlertStatus alertStatus;
    private int tbrAmount;
    private int tbrDuration;
    private double programmedBolusAmount;
    private double deliveredBolusAmount;
    private double cartridgeAmount;

    protected Alert(Parcel in) {
        alertId = in.readInt();
        alertCategory = in.readParcelable(AlertCategory.class.getClassLoader());
        alertType = in.readParcelable(AlertType.class.getClassLoader());
        alertStatus = in.readParcelable(AlertStatus.class.getClassLoader());
        tbrAmount = in.readInt();
        tbrDuration = in.readInt();
        programmedBolusAmount = in.readDouble();
        deliveredBolusAmount = in.readDouble();
        cartridgeAmount = in.readDouble();
    }

    public static final Creator<Alert> CREATOR = new Creator<Alert>() {
        @Override
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        @Override
        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };

    public Alert() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(alertId);
        parcel.writeParcelable(alertCategory, i);
        parcel.writeParcelable(alertType, i);
        parcel.writeParcelable(alertStatus, i);
        parcel.writeInt(tbrAmount);
        parcel.writeInt(tbrDuration);
        parcel.writeDouble(programmedBolusAmount);
        parcel.writeDouble(deliveredBolusAmount);
        parcel.writeDouble(cartridgeAmount);
    }

    public int getAlertId() {
        return this.alertId;
    }

    public AlertCategory getAlertCategory() {
        return this.alertCategory;
    }

    public AlertType getAlertType() {
        return this.alertType;
    }

    public AlertStatus getAlertStatus() {
        return this.alertStatus;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public void setAlertCategory(AlertCategory alertCategory) {
        this.alertCategory = alertCategory;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public void setAlertStatus(AlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }

    public int getTBRAmount() {
        return tbrAmount;
    }

    public void setTBRAmount(int tbrAmount) {
        this.tbrAmount = tbrAmount;
    }

    public int getTBRDuration() {
        return tbrDuration;
    }

    public void setTBRDuration(int tbrDuration) {
        this.tbrDuration = tbrDuration;
    }

    public double getProgrammedBolusAmount() {
        return programmedBolusAmount;
    }

    public void setProgrammedBolusAmount(double programmedBolusAmount) {
        this.programmedBolusAmount = programmedBolusAmount;
    }

    public double getDeliveredBolusAmount() {
        return deliveredBolusAmount;
    }

    public void setDeliveredBolusAmount(double deliveredBolusAmount) {
        this.deliveredBolusAmount = deliveredBolusAmount;
    }

    public void setCartridgeAmount(double cartridgeAmount) {
        this.cartridgeAmount = cartridgeAmount;
    }

    public double getCartridgeAmount() {
        return cartridgeAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alert alert = (Alert) o;

        if (alertId != alert.alertId) return false;
        if (tbrAmount != alert.tbrAmount) return false;
        if (tbrDuration != alert.tbrDuration) return false;
        if (Double.compare(alert.programmedBolusAmount, programmedBolusAmount) != 0) return false;
        if (Double.compare(alert.deliveredBolusAmount, deliveredBolusAmount) != 0) return false;
        if (Double.compare(alert.cartridgeAmount, cartridgeAmount) != 0) return false;
        if (alertCategory != alert.alertCategory) return false;
        if (alertType != alert.alertType) return false;
        return alertStatus == alert.alertStatus;
    }
}
