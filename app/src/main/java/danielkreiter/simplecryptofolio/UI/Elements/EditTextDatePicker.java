package danielkreiter.simplecryptofolio.UI.Elements;


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * EditTextPicker
 */
public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText mEditText;
    Calendar mCalendar;
    private int mDay;
    private int mMonth;
    private int mBirthYear;
    private Context mContext;

    /**
     * Instantiates a new EditTextPicker.
     *
     * @param context      the context
     * @param editTextView the edit text view
     */
    public EditTextDatePicker(Context context, EditText editTextView) {
        editTextView.setFocusable(false);
        this.mEditText = editTextView;
        this.mEditText.setOnClickListener(this);
        this.mContext = context;
        this.mCalendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mBirthYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateView();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(mContext, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    private void updateView() {
        mEditText.setText(new StringBuilder().append(mDay).append(".").append(mMonth + 1)
                .append(".").append(mBirthYear).append(" "));
    }
}