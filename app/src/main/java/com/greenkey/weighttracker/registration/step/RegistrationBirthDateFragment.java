package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.SettingsManager;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;
import java.util.Date;

import static android.R.attr.minDate;


public class RegistrationBirthDateFragment extends Fragment  implements Step {
    DatePicker birthDatePicker;
    long date;
    boolean chooseDate = false;
    String errorMesage;

    private DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            chooseDate = true;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_birth_date_fragment, container, false);
        birthDatePicker = (DatePicker)view.findViewById(R.id.birth_date_picker);
        final long currentDate = SettingsManager.getBirthDate();
        if(currentDate == 0){
            birthDatePicker.init(1990,0,1,dateChangedListener);
        }
        else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentDate);
            birthDatePicker.init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),dateChangedListener);
            chooseDate = true;
        }
        birthDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        return view;
    }

    private static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public int getName() {
        return R.string.birth_date;
    }

    @Override
    public VerificationError verifyStep() {
        if(chooseDate){
            date = getDateFromDatePicker(birthDatePicker).getTime();
            SettingsManager.setBirthDate(date);
            return null;
        }
        else{
            errorMesage = getString(R.string.birth_date_error);
            return new VerificationError(errorMesage);
        }
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
