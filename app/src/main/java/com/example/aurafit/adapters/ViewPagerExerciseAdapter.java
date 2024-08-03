package com.example.aurafit.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise.StepsFragment;
import com.example.aurafit.ui.bottom_nav_fragments.view.physical_fitness.exercise.TimerFragment;

public class ViewPagerExerciseAdapter extends FragmentPagerAdapter {
    private final String exerciseSteps; // Assuming exerciseSteps is a formatted string of steps

    public ViewPagerExerciseAdapter(@NonNull FragmentManager fm, String exerciseSteps) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.exerciseSteps = exerciseSteps;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return StepsFragment.newInstance(exerciseSteps);
        } else {
            return new TimerFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Steps";
        } else {
            return "Start Exercise";
        }
    }
}
