package sugar.free.telesto.database.entities;

import android.os.Parcel;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class CartridgeRemoved extends GenericHistoryEntity {

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        super.writeToParcel(dest, flags);
    }

    public static final Creator<CartridgeRemoved> CREATOR = new Creator<CartridgeRemoved>() {
        @Override
        public CartridgeRemoved createFromParcel(Parcel source) {
            source.readString();
            CartridgeRemoved cartridgeRemoved = new CartridgeRemoved();
            cartridgeRemoved.readFromParcel(source);
            return cartridgeRemoved;
        }

        @Override
        public CartridgeRemoved[] newArray(int size) {
            return new CartridgeRemoved[size];
        }
    };
}
