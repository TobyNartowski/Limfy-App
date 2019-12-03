package pl.tobynartowski.limfy.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pl.tobynartowski.limfy.ui.fragment.AppAnalysisFragment;
import pl.tobynartowski.limfy.ui.fragment.AppDetailsFragment;
import pl.tobynartowski.limfy.ui.fragment.AppHistoryFragment;
import pl.tobynartowski.limfy.ui.fragment.AppSettingsFragment;
import pl.tobynartowski.limfy.ui.fragment.AppTodayFragment;

public class ViewPageAdapter extends FragmentStateAdapter {

    private static final int CARD_ITEM_SIZE = 6;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AppAnalysisFragment();
            case 1:
                return new AppTodayFragment();
            case 2:
                return new AppDetailsFragment(AppDetailsFragment.AppDetailsWhich.APP_DETAILS_HEARTBEAT);
            case 3:
                return new AppDetailsFragment(AppDetailsFragment.AppDetailsWhich.APP_DETAILS_ACTIVITY);
            case 4:
                return new AppHistoryFragment();
            case 5:
                return new AppSettingsFragment();
        }

        return new AppTodayFragment();
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
