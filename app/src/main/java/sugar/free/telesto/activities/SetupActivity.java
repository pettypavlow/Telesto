package sugar.free.telesto.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sugar.free.telesto.R;
import sugar.free.telesto.TelestoApp;
import sugar.free.telesto.adapter.BluetoothDeviceAdapter;
import sugar.free.telesto.descriptors.TelestoState;
import sugar.free.telesto.services.connection_service.ConnectionService;
import sugar.free.telesto.utils.ExceptionTranslator;

public class SetupActivity extends AppCompatActivity implements ConnectionService.StateCallback {

    public static final int PERMISSION_REQUEST_CODE = 102;

    private SetupViewModel viewModel;
    private boolean scanning;
    private AlertDialog alertDialog;

    private LinearLayout welcomeScreen;
    private ScrollView eulaScreen;
    private LinearLayout searchScreen;
    private LinearLayout progressScreen;
    private LinearLayout finishScreen;

    private TextView progress1Number;
    private TextView progress1Title;
    private TextView progress2Number;
    private TextView progress2Title;
    private TextView progress3Number;
    private TextView progress3Title;
    private TextView progress4Number;
    private TextView progress4Title;
    private TextView progress5Number;
    private TextView progress5Title;
    private TextView progress6Number;
    private TextView progress6Title;
    private TextView progress7Number;
    private TextView progress7Title;

    private View progress1;
    private View progress2;
    private View progress3;
    private View progress4;
    private View progress5;

    private Button back;
    private Button next;
    private Toolbar toolbar;
    private FrameLayout moon;
    private TextView clickNext;
    private TextView welcome;
    private LinearLayout controls;
    private ImageView moonImage;
    private CheckBox acceptEula;
    private RecyclerView deviceList;

    private BluetoothDeviceAdapter bluetoothDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        TelestoApp.getConnectionService().setSetupActivity(this);
        TelestoApp.getConnectionService().requestConnection(this);
        TelestoApp.getConnectionService().registerStateCallback(this);

        viewModel = ViewModelProviders.of(this).get(SetupViewModel.class);

        welcomeScreen = findViewById(R.id.welcome_screen);
        eulaScreen = findViewById(R.id.eula_screen);
        searchScreen = findViewById(R.id.search_screen);
        progressScreen = findViewById(R.id.progress_screen);
        finishScreen = findViewById(R.id.finish_screen);

        toolbar = findViewById(R.id.toolbar);
        moon = findViewById(R.id.moon);
        clickNext = findViewById(R.id.click_next);
        welcome = findViewById(R.id.welcome);
        controls = findViewById(R.id.controls);
        moonImage = findViewById(R.id.moon_image);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);
        acceptEula = findViewById(R.id.accept_eula);
        acceptEula.setOnClickListener(this::acceptClicked);
        progress1 = findViewById(R.id.progress_1);
        progress2 = findViewById(R.id.progress_2);
        progress3 = findViewById(R.id.progress_3);
        progress4 = findViewById(R.id.progress_4);
        progress5 = findViewById(R.id.progress_5);
        deviceList = findViewById(R.id.device_list);
        deviceList.setLayoutManager(new LinearLayoutManager(this));
        bluetoothDeviceAdapter = new BluetoothDeviceAdapter(this::bluetoothDeviceSelected, viewModel.getBluetoothDevices());
        deviceList.setAdapter(bluetoothDeviceAdapter);

        progress1Number = findViewById(R.id.pairing_progress_1_number);
        progress1Title = findViewById(R.id.pairing_progress_1_title);
        progress2Number = findViewById(R.id.pairing_progress_2_number);
        progress2Title = findViewById(R.id.pairing_progress_2_title);
        progress3Number = findViewById(R.id.pairing_progress_3_number);
        progress3Title = findViewById(R.id.pairing_progress_3_title);
        progress4Number = findViewById(R.id.pairing_progress_4_number);
        progress4Title = findViewById(R.id.pairing_progress_4_title);
        progress5Number = findViewById(R.id.pairing_progress_5_number);
        progress5Title = findViewById(R.id.pairing_progress_5_title);
        progress6Number = findViewById(R.id.pairing_progress_6_number);
        progress6Title = findViewById(R.id.pairing_progress_6_title);
        progress7Number = findViewById(R.id.pairing_progress_7_number);
        progress7Title = findViewById(R.id.pairing_progress_7_title);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setIcon(R.drawable.ic_setup);

        if (viewModel.getVerificationString() != null)
            showVerificationString(viewModel.getVerificationString());

        if (viewModel.getScreen() == null) {
            moon.setAlpha(0);
            welcome.setAlpha(0);
            clickNext.setAlpha(0);
            toolbar.setAlpha(0);
            controls.setAlpha(0);

            moon.animate().alpha(1).setDuration(750);
            welcome.animate().alpha(1).setDuration(750).setStartDelay(350);

            clickNext.animate().alpha(1).setDuration(750).setStartDelay(750);

            controls.animate().alpha(1).setDuration(750).setStartDelay(1250);
            toolbar.animate().alpha(1).setDuration(750).setStartDelay(1500);
            setActiveScreen(SetupViewModel.Screen.WELCOME, false);
        } else setActiveScreen(viewModel.getScreen(), false);
        if (viewModel.getTelestoState() != null) updateProgress(viewModel.getTelestoState(), false);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(60000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        moonImage.startAnimation(rotateAnimation);

        checkBatteryOptimization();
    }

    private void updateProgress(TelestoState state, boolean animate) {
        viewModel.setTelestoState(state);
        switch (state) {
            case CONNECTING:
                progress1Title.setTypeface(null, Typeface.BOLD);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                progress1Number.setAlpha(1);
                progress2Number.setAlpha(1);
                progress3Number.setAlpha(1);
                progress4Number.setAlpha(1);
                progress5Number.setAlpha(1);
                progress6Number.setAlpha(1);
                progress7Number.setAlpha(1);
                break;
            case SATL_CONNECTION_REQUEST:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.BOLD);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                if (animate) progress1Number.animate().alpha(0).setDuration(250);
                else progress1Number.setAlpha(0);
                progress2Number.setAlpha(1);
                progress3Number.setAlpha(1);
                progress4Number.setAlpha(1);
                progress5Number.setAlpha(1);
                progress6Number.setAlpha(1);
                progress7Number.setAlpha(1);
                break;
            case SATL_KEY_REQUEST:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.BOLD);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                if (animate) {
                    progress1Number.animate().alpha(0).setDuration(250);
                    progress2Number.animate().alpha(0).setDuration(250);
                } else {
                    progress1Number.setAlpha(0);
                    progress2Number.setAlpha(0);
                }
                progress3Number.setAlpha(1);
                progress4Number.setAlpha(1);
                progress5Number.setAlpha(1);
                progress6Number.setAlpha(1);
                progress7Number.setAlpha(1);
                break;
            case SATL_VERIFY_DISPLAY_REQUEST:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.BOLD);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                if (animate) {
                    progress1Number.animate().alpha(0).setDuration(250);
                    progress2Number.animate().alpha(0).setDuration(250);
                    progress3Number.animate().alpha(0).setDuration(250);
                } else {
                    progress1Number.setAlpha(0);
                    progress2Number.setAlpha(0);
                    progress3Number.setAlpha(0);
                }
                progress4Number.setAlpha(1);
                progress5Number.setAlpha(1);
                progress6Number.setAlpha(1);
                progress7Number.setAlpha(1);
                break;
            case AWAITING_CODE_CONFIRMATION:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.BOLD);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                if (animate) {
                    progress1Number.animate().alpha(0).setDuration(250);
                    progress2Number.animate().alpha(0).setDuration(250);
                    progress3Number.animate().alpha(0).setDuration(250);
                    progress4Number.animate().alpha(0).setDuration(250);
                } else {
                    progress1Number.setAlpha(0);
                    progress2Number.setAlpha(0);
                    progress3Number.setAlpha(0);
                    progress4Number.setAlpha(0);
                }
                progress5Number.setAlpha(1);
                progress6Number.setAlpha(1);
                progress7Number.setAlpha(1);
                break;
            case SATL_VERIFY_CONFIRM_REQUEST:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.BOLD);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                if (animate) {
                    progress1Number.animate().alpha(0).setDuration(250);
                    progress2Number.animate().alpha(0).setDuration(250);
                    progress3Number.animate().alpha(0).setDuration(250);
                    progress4Number.animate().alpha(0).setDuration(250);
                    progress5Number.animate().alpha(0).setDuration(250);
                } else {
                    progress1Number.setAlpha(0);
                    progress2Number.setAlpha(0);
                    progress3Number.setAlpha(0);
                    progress4Number.setAlpha(0);
                    progress5Number.setAlpha(0);
                }
                progress6Number.setAlpha(1);
                progress7Number.setAlpha(1);
                break;
            case APP_BIND_MESSAGE:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.BOLD);
                if (animate) {
                    progress1Number.animate().alpha(0).setDuration(250);
                    progress2Number.animate().alpha(0).setDuration(250);
                    progress3Number.animate().alpha(0).setDuration(250);
                    progress4Number.animate().alpha(0).setDuration(250);
                    progress5Number.animate().alpha(0).setDuration(250);
                    progress6Number.animate().alpha(0).setDuration(250);
                } else {
                    progress1Number.setAlpha(0);
                    progress2Number.setAlpha(0);
                    progress3Number.setAlpha(0);
                    progress4Number.setAlpha(0);
                    progress5Number.setAlpha(0);
                    progress6Number.setAlpha(0);
                }
                progress7Number.setAlpha(1);
                break;
            case CONNECTED:
                progress1Title.setTypeface(null, Typeface.NORMAL);
                progress2Title.setTypeface(null, Typeface.NORMAL);
                progress3Title.setTypeface(null, Typeface.NORMAL);
                progress4Title.setTypeface(null, Typeface.NORMAL);
                progress5Title.setTypeface(null, Typeface.NORMAL);
                progress6Title.setTypeface(null, Typeface.NORMAL);
                progress7Title.setTypeface(null, Typeface.NORMAL);
                if (animate) {
                    progress1Number.animate().alpha(0).setDuration(250);
                    progress2Number.animate().alpha(0).setDuration(250);
                    progress3Number.animate().alpha(0).setDuration(250);
                    progress4Number.animate().alpha(0).setDuration(250);
                    progress5Number.animate().alpha(0).setDuration(250);
                    progress6Number.animate().alpha(0).setDuration(250);
                    progress7Number.animate().alpha(0).setDuration(250);
                } else {
                    progress1Number.setAlpha(0);
                    progress2Number.setAlpha(0);
                    progress3Number.setAlpha(0);
                    progress4Number.setAlpha(0);
                    progress5Number.setAlpha(0);
                    progress6Number.setAlpha(0);
                    progress7Number.setAlpha(0);
                }
                break;
        }
    }

    private void checkBatteryOptimization() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (alertDialog != null) alertDialog.dismiss();
        TelestoApp.getConnectionService().unregisterStateCallback(this);
        TelestoApp.getConnectionService().setSetupActivity(null);
        TelestoApp.getConnectionService().withdrawConnectionRequest(this);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewModel.getScreen() == SetupViewModel.Screen.EULA)
            next.setEnabled(acceptEula.isChecked());
    }

    private void setActiveScreen(SetupViewModel.Screen screen, boolean animate) {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        if (animate) {
            View currentView = getViewForScreen(viewModel.getScreen());
            View nextView = getViewForScreen(screen);
            if (screen.ordinal() > viewModel.getScreen().ordinal()) {
                currentView.animate().translationX(-width).setDuration(250);
                nextView.setTranslationX(width);
            } else {
                currentView.animate().translationX(width).setDuration(250);
                nextView.setTranslationX(-width);
            }
            nextView.animate().translationX(0).setDuration(250);

            progress1.animate().alpha(screen.ordinal() == 0 ? 1f : 0.3f).setDuration(250);
            progress2.animate().alpha(screen.ordinal() == 1 ? 1f : 0.3f).setDuration(250);
            progress3.animate().alpha(screen.ordinal() == 2 ? 1f : 0.3f).setDuration(250);
            progress4.animate().alpha(screen.ordinal() == 3 ? 1f : 0.3f).setDuration(250);
            progress5.animate().alpha(screen.ordinal() == 4 ? 1f : 0.3f).setDuration(250);
            if (screen == SetupViewModel.Screen.WELCOME || screen == SetupViewModel.Screen.FINISH || screen == SetupViewModel.Screen.PAIRING) {
                back.animate().alpha(0).setDuration(250);
                back.setOnClickListener(null);
            } else {
                back.animate().alpha(1).setDuration(250);
                back.setOnClickListener(this::backClicked);
            }
            if (screen == SetupViewModel.Screen.SEARCH_DEVICE || screen == SetupViewModel.Screen.FINISH || screen == SetupViewModel.Screen.PAIRING) {
                next.animate().alpha(0).setDuration(250);
                next.setOnClickListener(null);
            } else {
                next.animate().alpha(1).setDuration(250);
                next.setOnClickListener(this::nextClicked);
            }
        } else {
            for (SetupViewModel.Screen s : SetupViewModel.Screen.values())
                getViewForScreen(s).setTranslationX(s == screen ? 0 : width);
            progress1.setAlpha(screen.ordinal() == 0 ? 1f : 0.3f);
            progress2.setAlpha(screen.ordinal() == 1 ? 1f : 0.3f);
            progress3.setAlpha(screen.ordinal() == 2 ? 1f : 0.3f);
            progress4.setAlpha(screen.ordinal() == 3 ? 1f : 0.3f);
            progress5.setAlpha(screen.ordinal() == 4 ? 1f : 0.3f);
            if (screen == SetupViewModel.Screen.WELCOME || screen == SetupViewModel.Screen.FINISH || screen == SetupViewModel.Screen.PAIRING) {
                back.setAlpha(0);
                back.setOnClickListener(null);
            } else {
                back.setAlpha(1);
                back.setOnClickListener(this::backClicked);
            }
            if (screen == SetupViewModel.Screen.SEARCH_DEVICE || screen == SetupViewModel.Screen.FINISH || screen == SetupViewModel.Screen.PAIRING) {
                next.setAlpha(0);
                next.setOnClickListener(null);
            } else {
                next.setAlpha(1);
                next.setOnClickListener(this::nextClicked);
            }
        }
        viewModel.setScreen(screen);
        if (screen == SetupViewModel.Screen.EULA) next.setEnabled(acceptEula.isChecked());
        else next.setEnabled(true);
        if (screen == SetupViewModel.Screen.SEARCH_DEVICE) scanForDevices();
        else stopScanning();
    }

    @Override
    public void onBackPressed() {
        if (viewModel.getScreen().ordinal() == 0 || viewModel.getScreen() == SetupViewModel.Screen.FINISH)
            finish();
        else
            setActiveScreen(SetupViewModel.Screen.values()[viewModel.getScreen().ordinal() - 1], true);
    }

    private View getViewForScreen(SetupViewModel.Screen screen) {
        switch (screen) {
            case WELCOME:
                return welcomeScreen;
            case EULA:
                return eulaScreen;
            case SEARCH_DEVICE:
                return searchScreen;
            case PAIRING:
                return progressScreen;
            case FINISH:
                return finishScreen;
            default:
                return null;
        }
    }

    public void backClicked(View view) {
        setActiveScreen(SetupViewModel.Screen.values()[viewModel.getScreen().ordinal() - 1], true);
    }

    public void nextClicked(View view) {
        setActiveScreen(SetupViewModel.Screen.values()[viewModel.getScreen().ordinal() + 1], true);
    }

    public void closeClicked(View view) {
        startActivity(new Intent(this, StatusActivity.class));
        finish();
    }

    public void acceptClicked(View view) {
        next.setEnabled(acceptEula.isChecked());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel.getScreen() == SetupViewModel.Screen.SEARCH_DEVICE) scanForDevices();
    }

    @Override
    protected void onStop() {
        stopScanning();
        super.onStop();
    }

    private void scanForDevices() {
        if (!scanning) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) bluetoothAdapter.enable();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
                registerReceiver(broadcastReceiver, intentFilter);
                bluetoothAdapter.startDiscovery();
                scanning = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length == 0 || grantResults.length == 0) finish();
            else if (!permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) finish();
            else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) finish();
            else scanForDevices();
        }
    }

    private void stopScanning() {
        if (scanning) {
            unregisterReceiver(broadcastReceiver);
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            scanning = false;
        }
    }

    private void bluetoothDeviceSelected(BluetoothDevice bluetoothDevice) {
        setActiveScreen(SetupViewModel.Screen.PAIRING, true);
        TelestoApp.getConnectionService().pair(bluetoothDevice.getAddress());
    }

    @Override
    public void stateChanged(TelestoState state) {
        runOnUiThread(() -> {
            updateProgress(state, true);
            if (state == TelestoState.NOT_PAIRED) {
                if (viewModel.getScreen() != SetupViewModel.Screen.FINISH)
                    setActiveScreen(SetupViewModel.Screen.SEARCH_DEVICE, true);
                hideVerificationString();
            } else if (state == TelestoState.CONNECTED) setActiveScreen(SetupViewModel.Screen.FINISH, true);
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
                BluetoothAdapter.getDefaultAdapter().startDiscovery();
            else if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!viewModel.getBluetoothDevices().contains(bluetoothDevice))
                    bluetoothDeviceAdapter.addItem(bluetoothDevice);
            }
        }
    };

    public void displayException(Exception e) {
        ExceptionTranslator.makeToast(this, e);
    }

    public void showVerificationString(String verificationString) {
        runOnUiThread(() -> {
            viewModel.setVerificationString(verificationString);
            TextView textView = new TextView(this);
            textView.setTextAppearance(this, R.style.ValueStyle);
            textView.setText(verificationString);
            textView.setAllCaps(false);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            float dpi = getResources().getDisplayMetrics().density;
            textView.setPadding((int) (24 * dpi), (int) (4 * dpi), (int) (24 * dpi), (int) (4 * dpi));

            alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setTitle(R.string.compare_codes)
                    .setMessage(R.string.does_this_code_match_the_one_displayed_on_the_pump)
                    .setPositiveButton(R.string.yes, ((dialogInterface, i) -> {
                        viewModel.setVerificationString(null);
                        try {
                            TelestoApp.getConnectionService().confirmVerificationString();
                        } catch (Exception e) {
                            displayException(e);
                        }
                    }))
                    .setNegativeButton(R.string.no, ((dialogInterface, i) -> {
                        viewModel.setVerificationString(null);
                        try {
                            TelestoApp.getConnectionService().rejectVerificationString();
                        } catch (Exception e) {
                            displayException(e);
                        }
                    }))
                    .setView(textView)
                    .setCancelable(false)
                    .show();
        });
    }

    public void hideVerificationString() {
        runOnUiThread(() -> {
            viewModel.setVerificationString(null);
            if (alertDialog != null) alertDialog.dismiss();
        });
    }

    public static class SetupViewModel extends ViewModel {

        private Screen screen;
        private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
        private String verificationString = null;
        private TelestoState telestoState;

        public Screen getScreen() {
            return this.screen;
        }

        public List<BluetoothDevice> getBluetoothDevices() {
            return this.bluetoothDevices;
        }

        public String getVerificationString() {
            return this.verificationString;
        }

        public TelestoState getTelestoState() {
            return this.telestoState;
        }

        public void setScreen(Screen screen) {
            this.screen = screen;
        }

        public void setBluetoothDevices(List<BluetoothDevice> bluetoothDevices) {
            this.bluetoothDevices = bluetoothDevices;
        }

        public void setVerificationString(String verificationString) {
            this.verificationString = verificationString;
        }

        public void setTelestoState(TelestoState telestoState) {
            this.telestoState = telestoState;
        }

        public enum Screen {
            WELCOME,
            EULA,
            SEARCH_DEVICE,
            PAIRING,
            FINISH
        }
    }
}
