package pl.tobynartowski.limfy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.tobynartowski.limfy.Limfy;
import pl.tobynartowski.limfy.R;
import pl.tobynartowski.limfy.misc.SwipeTouchListener;
import pl.tobynartowski.limfy.ui.activity.AppViewActivity;

public class AppHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.app_history_layout).setOnTouchListener(new SwipeTouchListener(Limfy.getContext()) {

            @Override
            public void onSwipeTop() {
                AppViewActivity activity = (AppViewActivity) getActivity();
                if (activity != null) {
                    activity.changeToConnectActivity();
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
