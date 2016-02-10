package com.supprojectstarter.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.SessionManager;

public class TabHostActivity extends TabActivity {
    private TabHost tabHost;
    private TabSpec tabSpec;
    private String homeTabName;
    private String categoriesTabName;
    private String newProjectTabName;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host_screen);
        tabHost = getTabHost();
        session = new SessionManager(getApplicationContext());
        homeTabName = getString(R.string.tab_title_home);
        categoriesTabName = getString(R.string.tab_title_categories);
        newProjectTabName = getString(R.string.tab_title_new_project);
        Intent homeIntent = new Intent(TabHostActivity.this, HomeActivity.class);
        tabSpec = tabHost.newTabSpec(homeTabName);
        tabSpec.setIndicator(homeTabName, getResources().getDrawable(R.drawable.ic_home));
        tabSpec.setContent(homeIntent);
        tabHost.addTab(tabSpec);
        Intent categoriesIntent = new Intent(TabHostActivity.this, CategoriesActivity.class);
        tabSpec = tabHost.newTabSpec(categoriesTabName);
        tabSpec.setIndicator(categoriesTabName, getResources().getDrawable(R.drawable.ic_category));
        tabSpec.setContent(categoriesIntent);
        tabHost.addTab(tabSpec);
        if (session.isLoggedIn()) {
            Intent createProjectIntent = new Intent(TabHostActivity.this, CreateProjectActivity.class);
            tabSpec = tabHost.newTabSpec(newProjectTabName);
            tabSpec.setIndicator(newProjectTabName, getResources().getDrawable(R.drawable.ic_newproject));
            tabSpec.setContent(createProjectIntent);
            tabHost.addTab(tabSpec);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.menu_register);
        MenuItem login = menu.findItem(R.id.menu_login);
        MenuItem logout = menu.findItem(R.id.menu_logout);
        MenuItem profil = menu.findItem(R.id.menu_profil);
        register.setVisible(!session.isLoggedIn());
        login.setVisible(!session.isLoggedIn());
        logout.setVisible(session.isLoggedIn());
        profil.setVisible(session.isLoggedIn());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        switch (item.getItemId()) {
            case R.id.menu_login:
                i = new Intent(TabHostActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.menu_register:
                i = new Intent(TabHostActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.menu_logout:
                i = new Intent(TabHostActivity.this, TabHostActivity.class);
                session.logoutUser();
                startActivity(i);
                finish();
                return true;
            case R.id.menu_profil:
                i = new Intent(TabHostActivity.this, EditProfileActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
