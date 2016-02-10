package com.supprojectstarter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.supprojectstarter.beans.Category;
import com.supprojectstarter.beans.Contribution;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.dao.DAOCategory;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryDetailActivity extends Activity implements OnItemClickListener {
    private static final String TAG_IDP = "id";
    private static final String TAG_NAMEP = "name";
    private static final String TAG_PERCENTAGEP = "percentage";
    private static final String TAG_DESCRIPTIONP = "description";
    private static final String TAG_CATEGORY_NAME = "categoryName";
    private static ArrayList<HashMap<String, String>> projectsList;
    private String categoryName;
    private Category category;
    private List<Project> projects = null;
    private HashMap<String, String> map;
    private ListView lv = null;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_detail_screen);
        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        if (intent.hasExtra(TAG_CATEGORY_NAME)) {
            categoryName = intent.getStringExtra(TAG_CATEGORY_NAME);
        }
        category = DAOCategory.getInstance().findByName(categoryName);
        projects = category.getProjects();
        projectsList = new ArrayList<HashMap<String, String>>();
        for (Project p : projects) {
            double totalContributions = 0;
            double projectAmount = 0;
            map = new HashMap<String, String>();
            map.put(TAG_IDP, Integer.toString(p.getId()));
            map.put(TAG_NAMEP, p.getName());
            String projectDescription = p.getDescription();
            if (projectDescription.length() < 70) {
                map.put(TAG_DESCRIPTIONP, projectDescription);
            } else {
                map.put(TAG_DESCRIPTIONP, projectDescription.substring(0, 70) + "...");
            }
            projectAmount = p.getAmountP();
            for (Contribution c : p.getContributions()) {
                totalContributions = totalContributions + c.getAmountC();
            }
            String projectPercentage = Math.round((totalContributions / projectAmount * 100) * 100.0) / 100.0 + " %";
            map.put(TAG_PERCENTAGEP, projectPercentage);
            projectsList.add(map);
        }
        View header = getLayoutInflater().inflate(R.layout.project_tile_header_layout, null);
        TextView headerTV = (TextView) header.findViewById(R.id.home_header_tv);
        headerTV.setText(categoryName);
        lv = (ListView) findViewById(R.id.category_projects_lv);
        lv.addHeaderView(header, null, false);
        SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(), projectsList, R.layout.project_tile_layout,
                new String[]{TAG_NAMEP, TAG_PERCENTAGEP, TAG_DESCRIPTIONP},
                new int[]{R.id.tile_title_tv, R.id.tile_percentage_tv, R.id.tile_description_tv});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(CategoryDetailActivity.this, ProjectDetailActivity.class);
        HashMap<String, String> project = projectsList.get(position - 1);
        i.putExtra(TAG_IDP, Integer.parseInt((project.get(TAG_IDP))));
        i.putExtra(TAG_PERCENTAGEP, project.get(TAG_PERCENTAGEP));
        startActivity(i);
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
                i = new Intent(CategoryDetailActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.menu_register:
                i = new Intent(CategoryDetailActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.menu_logout:
                i = new Intent(CategoryDetailActivity.this, TabHostActivity.class);
                session.logoutUser();
                startActivity(i);
                finish();
                return true;
            case R.id.menu_profil:
                i = new Intent(CategoryDetailActivity.this, EditProfileActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
