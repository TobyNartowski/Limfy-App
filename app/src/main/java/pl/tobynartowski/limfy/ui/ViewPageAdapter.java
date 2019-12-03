package pl.tobynartowski.limfy.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import pl.tobynartowski.limfy.ui.fragment.AppDetailsFragment;

public class ViewPageAdapter extends FragmentStateAdapter {

    private static final int CARD_ITEM_SIZE = 2;

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AppDetailsFragment(AppDetailsFragment.AppDetailsWhich.APP_DETAILS_HEARTBEAT);
            case 1:
                return new AppDetailsFragment(AppDetailsFragment.AppDetailsWhich.APP_DETAILS_ACTIVITY);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
