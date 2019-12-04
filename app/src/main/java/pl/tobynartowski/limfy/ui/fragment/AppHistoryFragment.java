package pl.tobynartowski.limfy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

import pl.tobynartowski.limfy.R;

public class AppHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Random random = new Random(47);
//        for (int i = 0; i < 10; i++) {
//            LinearLayout entry = (LinearLayout) View.inflate(getContext(), R.layout.layout_history_entry, null);
//            ((TextView) entry.findViewById(R.id.app_history_entry_heartbeat)).setText(Integer.toString(random.nextInt(10) + 70));
//            ((TextView) entry.findViewById(R.id.app_history_entry_activity)).setText(Integer.toString(random.nextInt(300) + 1000));
//            ((TextView) entry.findViewById(R.id.app_history_entry_date)).setText((29 - i) + ".10.2018");
//            ((LinearLayout) view.findViewById(R.id.app_history_entry_container)).addView(entry);
//        }

        view.findViewById(R.id.app_history_no_data).setVisibility(View.VISIBLE);
        view.findViewById(R.id.app_history_measurements).setVisibility(View.GONE);

        super.onViewCreated(view, savedInstanceState);
    }
}
