package pl.tobynartowski.limfy.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.net.URI;
import java.util.Calendar;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.api.RetrofitClient;
import pl.tobynartowski.limfy.model.BodyData;
import pl.tobynartowski.limfy.model.Gender;
import pl.tobynartowski.limfy.ui.activity.AppViewActivity;
import pl.tobynartowski.limfy.ui.activity.RegisterDetailsActivity;
import pl.tobynartowski.limfy.utils.UserUtils;
import pl.tobynartowski.limfy.utils.ViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppSettingsFragment extends Fragment {

    private static int userHeight = 183;
    private static int userWeight = 83;
    private static Gender gender = Gender.MALE;

    private NumberPicker heightPicker;
    private NumberPicker weightPicker;
    private RadioGroup radioGenderButton;
    private RadioButton maleButton, femaleButton;
    private Button changedButton;
    private TextView dateText;

    private Integer year;
    private boolean newUser = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        changedButton = view.findViewById(R.id.app_settings_button_change);
        setButtonState(changedButton, false);

        radioGenderButton = view.findViewById(R.id.radioSexButton);
        maleButton = view.findViewById(R.id.radioMale);
        femaleButton = view.findViewById(R.id.radioFemale);

        heightPicker = view.findViewById(R.id.app_settings_height_picker);
        heightPicker.setMinValue(100);
        heightPicker.setMaxValue(250);

        weightPicker = view.findViewById(R.id.app_settings_weight_picker);
        weightPicker.setMinValue(30);
        weightPicker.setMaxValue(150);

        dateText = view.findViewById(R.id.app_settings_age_text);
        Button logoutButton = view.findViewById(R.id.app_settings_button_logout);

        if (UserUtils.getInstance(Limfy.getContext()).getId() != null) {
            userHeight = 183;
            userWeight = 83;
            gender = Gender.MALE;
            newUser = false;

            if (gender == Gender.MALE) {
                maleButton.setChecked(true);
            } else {
                femaleButton.setChecked(true);
            }

            heightPicker.setOnValueChangedListener((picker, oldVal, newVal) -> checkButtonState());
            weightPicker.setOnValueChangedListener((picker, oldVal, newVal) -> checkButtonState());

            changedButton.setOnClickListener(v -> {
                setButtonState(changedButton, false);
                userHeight = heightPicker.getValue();
                userWeight = weightPicker.getValue();
                gender = radioGenderButton.getCheckedRadioButtonId() == maleButton.getId() ? Gender.MALE : Gender.FEMALE;
            });

            logoutButton.setOnClickListener(v -> {
                AppViewActivity activity = ((AppViewActivity) getActivity());
                if (activity != null) {
                    activity.logout();
                }
            });
        } else {
            userHeight = 150;
            userWeight = 60;
            gender = null;
            newUser = true;

            if (getContext() != null) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setOnClickListener(v -> {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog dialog = new DatePickerDialog(
                            getContext(),
                            R.style.DialogTheme,
                            (view1, year, month, dayOfMonth) -> {
                                if (year > 2015 || year < 1900) {
                                    ViewUtils.showToast(getContext(), getResources().getString(R.string.app_settings_age_error));
                                } else {
                                    dateText.setText(getResources().getString(R.string.app_settings_age_format, dayOfMonth, month, year));
                                    dateText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                                    this.year = year;
                                    checkButtonState();
                                }
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                    );
                    dialog.show();
                });
            }

            logoutButton.setText(R.string.app_settings_button_cancel);
            logoutButton.setOnClickListener(v -> {
                if (getActivity() instanceof RegisterDetailsActivity) {
                    RegisterDetailsActivity activity = (RegisterDetailsActivity) getActivity();
                    activity.onBackPressed();
                } else {
                    throw new IllegalStateException();
                }
            });

            changedButton.setText(getResources().getString(R.string.app_settings_button_change_new));
            changedButton.setOnClickListener(v -> {
                if (getActivity() != null && getActivity().getIntent() != null) {
                    String self = getActivity().getIntent().getStringExtra("self");
                    if (self == null) {
                        self = "";
                    }
                    RegisterDetailsActivity activity = (RegisterDetailsActivity) getActivity();

                    RetrofitClient.getInstance().getApi().addBodyData(new BodyData(
                            maleButton.isChecked() ? Gender.MALE : Gender.FEMALE,
                            weightPicker.getValue(),
                            heightPicker.getValue(),
                            year,
                            URI.create(self)
                    )).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 201) {
                                activity.switchToLoginActivity();
                            } else {
                                activity.handleError();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            activity.handleError();
                        }
                    });
                } else {
                    throw new IllegalStateException();
                }
            });
        }

        radioGenderButton.setOnCheckedChangeListener((group, checkedId) -> checkButtonState());

        heightPicker.setValue(userHeight);
        weightPicker.setValue(userWeight);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        heightPicker.setValue(userHeight);
        weightPicker.setValue(userWeight);

        if (gender == Gender.MALE) {
            maleButton.setChecked(true);
            femaleButton.setChecked(false);
        } else {
            femaleButton.setChecked(true);
            maleButton.setChecked(false);
        }

        setButtonState(changedButton, false);
        super.onPause();
    }

    private void checkButtonState() {
        if (!newUser) {
            boolean sameGenderCheck = gender == Gender.MALE ? maleButton.isChecked() : femaleButton.isChecked();
            setButtonState(changedButton, heightPicker.getValue() != userHeight || weightPicker.getValue() != userWeight || !sameGenderCheck);
        } else {
            setButtonState(changedButton, radioGenderButton.getCheckedRadioButtonId() != -1 && year != null);
        }
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
