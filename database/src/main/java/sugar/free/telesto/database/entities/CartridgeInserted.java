package sugar.free.telesto.database.entities;

import android.os.Parcel;

import androidx.room.Entity;

@Entity(inheritSuperIndices = true)
public class CartridgeInserted extends GenericHistoryEntity {

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getSimpleName());
        super.writeToParcel(dest, flags);
    }

    public static final Creator<CartridgeInserted> CREATOR = new Creator<CartridgeInserted>() {
        @Override
        public CartridgeInserted createFromParcel(Parcel source) {
            source.readString();
            CartridgeInserted cartridgeInserted = new CartridgeInserted();
            cartridgeInserted.readFromParcel(source);
            return cartridgeInserted;
        }

        @Override
        public CartridgeInserted[] newArray(int size) {
            return new CartridgeInserted[size];
        }
    };
}
