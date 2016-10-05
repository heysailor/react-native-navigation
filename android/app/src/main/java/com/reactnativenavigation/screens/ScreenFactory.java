package com.reactnativenavigation.screens;

import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.LeftButtonOnClickListener;

public class ScreenFactory {
    public static Screen create(AppCompatActivity activity,
                                ScreenParams screenParams,
                                LeftButtonOnClickListener leftButtonOnClickListener) {
        if (screenParams.isFragmentScreen()) {
            return new FragmentScreen(activity, screenParams, leftButtonOnClickListener);
        } else if (screenParams.hasTopTabs()) {
            return new ViewPagerScreen(activity, screenParams, leftButtonOnClickListener);
        } else {
            return new SingleScreen(activity, screenParams, leftButtonOnClickListener);
        }
    }
}
