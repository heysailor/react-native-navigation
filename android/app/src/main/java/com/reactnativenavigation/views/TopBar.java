package com.reactnativenavigation.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Callback;
import com.reactnativenavigation.params.ContextualMenuParams;
import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.utils.ViewUtils;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TopBar extends AppBarLayout {

    private TitleBar titleBar;
    private ContextualMenu contextualMenu;
    private RelativeLayout titleBarAndContextualMenuContainer;
    private TopTabs topTabs;

    public TopBar(Context context) {
        super(context);
        setFitsSystemWindows(true);
        setId(ViewUtils.generateViewId());
        createLayout();
    }

    private void createLayout() {
        titleBarAndContextualMenuContainer = new RelativeLayout(getContext());
        addView(titleBarAndContextualMenuContainer);
    }

    public void addTitleBarAndSetButtons(List<TitleBarButtonParams> rightButtons,
                                         TitleBarLeftButtonParams leftButton,
                                         LeftButtonOnClickListener leftButtonOnClickListener,
                                         String navigatorEventId, boolean overrideBackPressInJs) {
        titleBar = new TitleBar(getContext());
        titleBarAndContextualMenuContainer.addView(titleBar, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        titleBar.setRightButtons(rightButtons, navigatorEventId);
        titleBar.setLeftButton(leftButton, leftButtonOnClickListener, navigatorEventId, overrideBackPressInJs);
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
    }

    public void setSubtitle(String subtitle) {
        titleBar.setSubtitle(subtitle);
    }

    public void setStyle(StyleParams styleParams) {
        if (styleParams.topBarColor.hasColor()) {
            setBackgroundColor(styleParams.topBarColor.getColor());
        }
        if (styleParams.topBarTransparent) {
            setTransparent();
        }
        setVisibility(styleParams.topBarHidden ? GONE : VISIBLE);
        titleBar.setStyle(styleParams);
        setTopTabsStyle(styleParams);
    }

    private void setTransparent() {
        setBackgroundColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(0);
            setOutlineProvider(null);
        }
    }

    public void setTitleBarRightButtons(String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        titleBar.setRightButtons(titleBarButtons, navigatorEventId);
    }

    public TabLayout initTabs() {
        topTabs = new TopTabs(getContext());
        addView(topTabs);
        return topTabs;
    }

    public void setTitleBarLeftButton(String navigatorEventId,
                                      LeftButtonOnClickListener leftButtonOnClickListener,
                                      TitleBarLeftButtonParams titleBarLeftButtonParams,
                                      boolean overrideBackPressInJs) {
        titleBar.setLeftButton(titleBarLeftButtonParams, leftButtonOnClickListener, navigatorEventId,
                overrideBackPressInJs);
    }

    private void setTopTabsStyle(StyleParams style) {
        if (topTabs == null) {
            return;
        }
        topTabs.setTopTabsTextColor(style);
        topTabs.setSelectedTabIndicatorStyle(style);
    }

    public void showContextualMenu(final ContextualMenuParams params, StyleParams styleParams, Callback onButtonClicked) {
        contextualMenu = new ContextualMenu(getContext(), params, styleParams, onButtonClicked);
        titleBarAndContextualMenuContainer.addView(contextualMenu, new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        ViewUtils.runOnPreDraw(contextualMenu, new Runnable() {
            @Override
            public void run() {
                titleBar.hide();
                contextualMenu.show();
            }
        });
    }

    public void onContextualMenuHidden() {
        titleBar.show();
    }

    public void dismissContextualMenu() {
        if (contextualMenu != null) {
            contextualMenu.dismiss();
            contextualMenu = null;
            titleBar.show();
        }
    }
}
