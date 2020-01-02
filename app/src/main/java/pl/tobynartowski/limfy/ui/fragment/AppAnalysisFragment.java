package pl.tobynartowski.limfy.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.model.Analysis;
import pl.tobynartowski.limfy.utils.DataUtils;

public class AppAnalysisFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_analysis, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout layout = view.findViewById(R.id.app_analysis_layout);
        TextView averageValue = view.findViewById(R.id.app_analysis_average_value);
        TextView averageDescription = view.findViewById(R.id.app_analysis_average_description);

        TextView firstDiseaseValue = view.findViewById(R.id.app_analysis_disease1_value);
        TextView firstDiseaseDescription = view.findViewById(R.id.app_analysis_disease1_description);

        TextView secondDiseaseValue = view.findViewById(R.id.app_analysis_disease2_value);
        TextView secondDiseaseDescription = view.findViewById(R.id.app_analysis_disease2_description);

        TextView thirdDiseaseValue = view.findViewById(R.id.app_analysis_disease3_value);
        TextView thirdDiseaseDescription = view.findViewById(R.id.app_analysis_disease3_description);

        List<Analysis> analyses = DataUtils.getInstance().getAnalyses();
        if (getContext() != null) {
            if (analyses != null && analyses.size() >= 3) {
                view.findViewById(R.id.app_analysis_no_data).setVisibility(View.GONE);
                analyses.sort(Comparator.comparing(Analysis::getPercentage).reversed());

                if (analyses.get(0) != null && analyses.get(0).getPercentage() != 0) {
                    firstDiseaseValue.setText("o " + String.format(Locale.getDefault(), "%.2f%%",
                            Math.abs(analyses.get(0).getPercentage())) + (analyses.get(0).getPercentage() > 0 ? " wyższe" : " niższe"));
                    firstDiseaseDescription.setText(analyses.get(0).getDisease().getName().toLowerCase());
                }

                if (analyses.get(1) != null && analyses.get(1).getPercentage() != 0) {
                    secondDiseaseValue.setText("o " + String.format(Locale.getDefault(), "%.2f%%",
                            Math.abs(analyses.get(1).getPercentage())) + (analyses.get(1).getPercentage() > 0 ? " wyższe" : " niższe"));
                    secondDiseaseDescription.setText(analyses.get(1).getDisease().getName().toLowerCase());
                }

                if (analyses.get(2) != null && analyses.get(2).getPercentage() != 2) {

                    thirdDiseaseValue.setText("o " + String.format(Locale.getDefault(), "%.2f%%",
                            Math.abs(analyses.get(2).getPercentage())) + (analyses.get(2).getPercentage() > 0 ? " wyższe" : " niższe"));
                    thirdDiseaseDescription.setText(analyses.get(2).getDisease().getName().toLowerCase());
                }

                double overall = analyses.stream()
                        .mapToDouble(Analysis::getPercentage)
                        .average()
                        .orElse(0.0);

                if (overall <= -30) {
                    averageValue.setText(getResources().getString(R.string.app_analysis_value_best));
                    averageDescription.setText(getResources().getString(R.string.app_analysis_value_best_description));
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                } else if (overall > -30 && overall <= -15) {
                    averageValue.setText(getResources().getString(R.string.app_analysis_value_good));
                    averageDescription.setText(getResources().getString(R.string.app_analysis_value_good_description));
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                } else if (overall > -15 && overall <= 10) {
                    averageValue.setText(getResources().getString(R.string.app_analysis_value_ok));
                    averageDescription.setText(getResources().getString(R.string.app_analysis_value_ok_description));
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteGray));
                } else if (overall > 10 && overall <= 30) {
                    averageValue.setText(getResources().getString(R.string.app_analysis_value_bad));
                    averageDescription.setText(getResources().getString(R.string.app_analysis_value_bad_description));
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
                } else {
                    averageValue.setText(getResources().getString(R.string.app_analysis_value_worst));
                    averageDescription.setText(getResources().getString(R.string.app_analysis_value_worst_description));
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
                }
            } else {
                view.findViewById(R.id.app_analysis_no_data).setVisibility(View.VISIBLE);
                view.findViewById(R.id.app_analysis_linear_layout).setVisibility(View.GONE);
                layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorWhiteGray));
            }
        }
    }
}
