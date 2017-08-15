package danielkreiter.simplecryptofolio.UI;


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText editText;
    Calendar calendar;
    private int day;
    private int month;
    private int birthYear;
    private Context context;

    public EditTextDatePicker(Context context, EditText editTextView) {
        editTextView.setFocusable(false);
        this.editText = editTextView;
        this.editText.setOnClickListener(this);
        this.context = context;
        this.calendar = Calendar.getInstance();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        birthYear = year;
        month = monthOfYear;
        day = dayOfMonth;
        updateView();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }


    private void updateView() {
        editText.setText(new StringBuilder().append(day).append(".").append(month + 1)
                .append(".").append(birthYear).append(" "));
    }
}