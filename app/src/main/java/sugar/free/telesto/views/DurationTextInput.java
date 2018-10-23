package sugar.free.telesto.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputEditText;

import sugar.free.telesto.R;

public class DurationTextInput extends TextInputEditText implements TextWatcher {

    private boolean running = false;
    private boolean deleting = false;

    public DurationTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        deleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        showError(getText().toString());
        if (running || deleting) return;
        running = true;
        int selection = getSelectionStart();
        if (selection == 2) selection = 3;
        String text = editable.toString().replaceAll("[^0-9]", "");
        if (text.length() >= 2)
            text = text.substring(0, 2) + ":" + text.substring(2, Math.min(text.length(), 4));
        setText(text);
        setSelection(Math.min(selection, text.length()));
        running = false;
    }

    private void showError(String text) {
        if (text.length() != 5 || !text.matches("[0-9]{2}:[0-9]{2}")) {
            setError(getContext().getString(R.string.the_duration_must_be_entered_in_format));
            return;
        }
        int duration = getDurationInMinutes();
        int minutes = Integer.parseInt(text.substring(3,5));
        if (minutes >= 60) setError(getContext().getString(R.string.invalid_input));
        else if (duration == 0) setError(getContext().getString(R.string.the_duration_must_not_be_0));
        else if (duration > 24 * 60) setError(getContext().getString(R.string.the_duration_must_not_exceed_24_hours));
        else if (duration % 15 != 0) setError(getContext().getString(R.string.the_step_size_is_15_minutes));
        else setError(null);
    }

    public boolean isValid() {
        String text = getText().toString();
        if (text.length() != 5) return false;
        if (!text.matches("[0-9]{2}:[0-9]{2}")) return false;
        int minutes = Integer.parseInt(text.substring(3,5));
        int duration = getDurationInMinutes();
        if (minutes >= 60) return false;
        if (duration > 24 * 60) return false;
        if (duration == 0) return false;
        if (duration % 15 != 0) return false;
        return true;
    }

    public int getDurationInMinutes() {
        String text = getText().toString();
        if (text.length() != 5) return 0;
        int hours = Integer.parseInt(text.substring(0,2));
        int minutes = Integer.parseInt(text.substring(3,5));
        return hours * 60 + minutes;
    }
}
