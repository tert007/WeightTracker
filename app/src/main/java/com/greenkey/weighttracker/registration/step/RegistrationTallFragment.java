package com.greenkey.weighttracker.registration.step;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.greenkey.weighttracker.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class RegistrationTallFragment extends Fragment implements Step {
    public static final int MAX_SEEK_BAR_VALUE = 250;
    public static final int START_SEEK_BAR_VALUE = 175;
    int startProgress = START_SEEK_BAR_VALUE;

    public RegistrationTallFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registration_tall_fragment, container, false);
        final ImageView humanImageView = (ImageView) view.findViewById(R.id.human_image_view);
        final AppCompatSeekBar heightVerticalSeekBar = (AppCompatSeekBar) view.findViewById(R.id.height_vertical_seek_bar);
        final TextView registrationTallTextView = (TextView) view.findViewById(R.id.registration_tall);

        heightVerticalSeekBar.setMax(MAX_SEEK_BAR_VALUE);
        heightVerticalSeekBar.setProgress(START_SEEK_BAR_VALUE);
        humanImageView.getLayoutParams().height = startProgress * 3;

        heightVerticalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                registrationTallTextView.setText(progress + " см");
                if (progress < 200 && progress > 20) {
                    if (progress >= 20 && progress < 100) {
                        humanImageView.setImageResource(R.drawable.registration_tall_chicken_icon);
                    } else if (progress >= 100 && progress < 180) {
                        humanImageView.setImageResource(R.drawable.registration_tall_boy_icon);
                    } else {
                        humanImageView.setImageResource(R.drawable.registraion_tall_businessman_icon);
                    }
                    humanImageView.getLayoutParams().height = progress * 3;
                }
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
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}

