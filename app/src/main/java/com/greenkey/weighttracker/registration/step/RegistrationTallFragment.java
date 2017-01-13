package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.greenkey.weighttracker.app.SettingsManager;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationTallFragment extends Fragment implements Step {
    public static final int MAX_SEEK_BAR_VALUE = 250;
    public static final int START_SEEK_BAR_VALUE = 175;
    public static final int MAX_ICON_TALL = 175;
    public DisplayMetrics displayMetrics;
    public int coefficient;

    int currentTall;
    ImageView humanImageView;
    boolean isChoseTall = false;
    String errorMessage;
    AppCompatSeekBar heightVerticalSeekBar;

    public RegistrationTallFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_tall_fragment, container, false);
        humanImageView = (ImageView) view.findViewById(R.id.human_image_view);
        heightVerticalSeekBar = (AppCompatSeekBar) view.findViewById(R.id.height_vertical_seek_bar);
        final TextView registrationTallTextView = (TextView) view.findViewById(R.id.registration_tall);

        displayMetrics = getResources().getDisplayMetrics();
        coefficient = (int) Math.round(displayMetrics.widthPixels / START_SEEK_BAR_VALUE / 1.5);

        heightVerticalSeekBar.setMax(MAX_SEEK_BAR_VALUE);
        currentTall = SettingsManager.getTall();

        if(currentTall == 0) {
            heightVerticalSeekBar.setProgress(START_SEEK_BAR_VALUE);
            currentTall = START_SEEK_BAR_VALUE;
        }
        else {
            heightVerticalSeekBar.setProgress(currentTall);
            isChoseTall = true;
        }

        registrationTallTextView.setText(currentTall + " см");
        setIconTall(currentTall);
        humanImageView.setImageResource(getImageForTall(currentTall));

        heightVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!isChoseTall)
                    isChoseTall = true;

                currentTall = progress;
                registrationTallTextView.setText(currentTall + " см");
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

