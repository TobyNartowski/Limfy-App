package pl.tobynartowski.limfy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.ui.activity.AppViewActivity;

public class AppSettingsFragment extends Fragment {

    private static int userHeight = 183;
    private static int userWeight = 83;

    private NumberPicker heightPicker;
    private NumberPicker weightPicker;
    private Button changedButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        changedButton = view.findViewById(R.id.app_settings_button_change);
        setButtonState(changedButton, false);

        heightPicker = view.findViewById(R.id.app_settings_height_picker);
        heightPicker.setMinValue(100);
        heightPicker.setMaxValue(250);
        heightPicker.setValue(userHeight);

        weightPicker = view.findViewById(R.id.app_settings_weight_picker);
        weightPicker.setMinValue(30);
        weightPicker.setMaxValue(150);
        weightPicker.setValue(userWeight);

        heightPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                setButtonState(changedButton, heightPicker.getValue() != userHeight || weightPicker.getValue() != userWeight));
        weightPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                setButtonState(changedButton, heightPicker.getValue() != userHeight || weightPicker.getValue() != userWeight));

        changedButton.setOnClickListener(v -> {
            setButtonState(changedButton, false);
            userHeight = heightPicker.getValue();
            userWeight = weightPicker.getValue();
        });

        view.findViewById(R.id.app_settings_button_logout).setOnClickListener(v -> {
            AppViewActivity activity = ((AppViewActivity) getActivity());
            if (activity != null) {
                activity.logout();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        heightPicker.setValue(userHeight);
        weightPicker.setValue(userWeight);
        setButtonState(changedButton, false);
        super.onPause();
    }

    private void setButtonState(Button button, boolean state) {
        if (state) {
            button.setEnabled(true);
            button.setBackgroundTintList(null);
        } else {
            button.setEnabled(false);
            button.setBackgroundTintList(getResources().getColorStateList(R.color.colorWhiteGray, null));
        }
    }
}
