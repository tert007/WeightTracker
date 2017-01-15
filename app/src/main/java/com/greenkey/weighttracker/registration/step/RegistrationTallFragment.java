package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.greenkey.weighttracker.entity.helper.TallHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationTallFragment extends Fragment implements Step {
    private static final int MAX_SEEK_BAR_VALUE = 250;
    private static final int START_SEEK_BAR_VALUE = 175;
    private static final int MAX_ICON_TALL = 175;
    private DisplayMetrics displayMetrics;
    private int coefficient;

    private int currentTall;
    private int tallUnitIndex;
    private String[] units;

    private TextView tallTextView;
    private TextView tallUnitTextView;

    private ImageView humanImageView;
    private boolean isChoseTall = false;
    private String errorMessage;
    private AppCompatSeekBar heightVerticalSeekBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        units = getResources().getStringArray(R.array.tall_units_short_name);

        currentTall = SettingsManager.getTall();
        tallUnitIndex = SettingsManager.getTallUnitIndex();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        currentTall = SettingsManager.getTall();
        tallUnitIndex = SettingsManager.getTallUnitIndex();

        final View view = inflater.inflate(R.layout.registration_tall_fragment, container, false);

        humanImageView = (ImageView) view.findViewById(R.id.human_image_view);
        heightVerticalSeekBar = (AppCompatSeekBar) view.findViewById(R.id.height_vertical_seek_bar);

        displayMetrics = getResources().getDisplayMetrics();
        coefficient = (int) Math.round(displayMetrics.widthPixels / START_SEEK_BAR_VALUE / 1.5);

        heightVerticalSeekBar.setMax(MAX_SEEK_BAR_VALUE);

        if(currentTall == 0) {
            heightVerticalSeekBar.setProgress(START_SEEK_BAR_VALUE);
            currentTall = START_SEEK_BAR_VALUE;
        }
        else {
            heightVerticalSeekBar.setProgress(currentTall);
            isChoseTall = true;
        }

        tallTextView = (TextView) view.findViewById(R.id.registration_tall_text_view);
        tallTextView.setText(TallHelper.convertToUnitSystem(currentTall, tallUnitIndex));

        tallUnitTextView = (TextView) view.findViewById(R.id.registration_tall_unit_text_view);


        if (tallUnitIndex == 0) {
            tallUnitTextView.setVisibility(View.VISIBLE);
            tallUnitTextView.setText(units[tallUnitIndex]);
        } else {
            tallUnitTextView.setVisibility(View.GONE); //Дюймы и футы и так черточками обозначаются, не нужно доп. писать
        }

        if (tallUnitIndex == 0) {
            tallUnitTextView.setVisibility(View.VISIBLE);
            tallUnitTextView.setText(units[tallUnitIndex]);
        } else {
            tallUnitTextView.setVisibility(View.GONE); //Дюймы и футы и так черточками обозначаются, не нужно доп. писать
        }

        setIconTall(currentTall);
        humanImageView.setImageResource(getImageForTall(currentTall));

        heightVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!isChoseTall)
                    isChoseTall = true;

                currentTall = progress;

                tallTextView.setText(TallHelper.convertToUnitSystem(currentTall, tallUnitIndex));
                humanImageView.setImageResource(getImageForTall(currentTall));
                setIconTall(currentTall);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    private void setIconTall(int progress){
        if(currentTall > MAX_ICON_TALL) {
            humanImageView.getLayoutParams().height = MAX_ICON_TALL * coefficient;
            humanImageView.getLayoutParams().width = MAX_ICON_TALL * coefficient;
        }
        else {
            humanImageView.getLayoutParams().height = progress * coefficient;
            humanImageView.getLayoutParams().width = progress * coefficient;
        }
    }

    private int getImageForTall(int progress){
        if (progress >= 20 && progress < 100) {
           return R.drawable.registration_tall_chicken_icon;
        } else if (progress >= 100 && progress < 180) {
           return R.drawable.registration_tall_boy_icon;
        } else {
           return R.drawable.registraion_tall_businessman_icon;
        }
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
        return R.string.tall;
    }

    @Override
    public VerificationError verifyStep() {
        if(!isChoseTall) {
            errorMessage = getString(R.string.tall_error);
            return new VerificationError(errorMessage);
        }
        SettingsManager.setTall(heightVerticalSeekBar.getProgress());
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}

