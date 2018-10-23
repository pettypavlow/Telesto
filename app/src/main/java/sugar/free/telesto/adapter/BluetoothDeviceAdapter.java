package sugar.free.telesto.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import sugar.free.telesto.R;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private ItemClickListener itemClickListener;
    private List<BluetoothDevice> bluetoothDevices;

    public BluetoothDeviceAdapter(ItemClickListener itemClickListener, List<BluetoothDevice> bluetoothDevices) {
        this.itemClickListener = itemClickListener;
        this.bluetoothDevices = bluetoothDevices;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bluetooth_device, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);
        if (bluetoothDevice.getName() == null) {
            holder.getDeviceName().setTextColor(ContextCompat.getColor(holder.getDeviceName().getContext(), R.color.colorError));
            holder.getDeviceName().setText(R.string.name_unavailable);
        } else {
            holder.getDeviceName().setTextColor(0xFFFFFFFF);
            holder.getDeviceName().setText(bluetoothDevice.getName());
        }
        holder.getMacAddress().setText(bluetoothDevice.getAddress());
        holder.getRoot().setOnClickListener((view) -> itemClickListener.onItemClicked(bluetoothDevices.get(position)));
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    public void addItem(BluetoothDevice bluetoothDevice) {
        bluetoothDevices.add(bluetoothDevice);
        notifyItemInserted(bluetoothDevices.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView macAddress;
        private TextView deviceName;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            macAddress = itemView.findViewById(R.id.mac_address);
            deviceName = itemView.findViewById(R.id.device_name);
        }

        public View getRoot() {
            return this.root;
        }

        public TextView getMacAddress() {
            return this.macAddress;
        }

        public TextView getDeviceName() {
            return this.deviceName;
        }
    }

    public interface ItemClickListener {
        void onItemClicked(BluetoothDevice bluetoothDevice);
    }

}
