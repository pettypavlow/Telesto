package sugar.free.telesto.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import sugar.free.telesto.TelestoApp;

public class ExceptionTranslator {

    private static final Map<Class<? extends Exception>, Integer> TABLE = new HashMap<>();

    public static String getString(Exception exception) {
        Integer res = TABLE.get(exception);
        return res == null ? exception.getClass().getSimpleName() : TelestoApp.getInstance().getString(res);
    }

    public static void makeToast(Context context, Exception exception) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, getString(exception), Toast.LENGTH_LONG).show());
    }
}
