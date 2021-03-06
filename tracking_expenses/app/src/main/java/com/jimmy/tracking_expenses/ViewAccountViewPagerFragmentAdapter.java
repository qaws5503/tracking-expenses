package com.jimmy.tracking_expenses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewAccountViewPagerFragmentAdapter extends FragmentStateAdapter {
    public ViewAccountViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String message = "error";
        switch (position){
            case 0:
                message = "assets";
                break;
            case 1:
                message = "liabilities";
                break;
        }
        return AddAccountFragment.newInstance(message);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
