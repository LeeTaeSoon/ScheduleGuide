package com.example.nrst1.scheduleguide;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nrst1 on 2017-05-25.
 */

public class ActionBarHandler {
    private Context context;
    private ActionBar actionBar;

    private View customView;

    ActionBarHandler(Context context, ActionBar actionBar) {
        this.context = context;
        this.actionBar = actionBar;
    }

    public void setBasicActionBar() {
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        customView = LayoutInflater.from(context).inflate(R.layout.actionbar, null);
        actionBar.setCustomView(customView, lp1);

        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        ImageView homeButton = (ImageView) customView.findViewById(R.id.actionBarLogo);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 메인화면으로 이동
            }
        });
    }

    public void setDrawerMenu(final DrawerLayout drawerLayout, final View menuView) {
        ImageView menuButton = (ImageView) customView.findViewById(R.id.actionBarMenu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(menuView)) drawerLayout.closeDrawer(menuView);
                else drawerLayout.openDrawer(menuView);
            }
        });
    }

    public void setTitle(String title) {
        TextView titleView = (TextView) customView.findViewById(R.id.actionBarText);
        titleView.setText(title);
    }
}
