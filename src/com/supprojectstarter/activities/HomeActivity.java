package com.supprojectstarter.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.supprojectstarter.beans.Contribution;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.AlertDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends Activity implements OnItemClickListener {
    private static final String TAG_IDP = "id";
    private static final String TAG_NAMEP = "name";
    private static final String TAG_PERCENTAGEP = "percentage";
    private static final String TAG_DESCRIPTIONP = "description";
    private static ArrayList<HashMap<String, String>> projectsList;
    private List<Project> projects;
    private HashMap<String, String> map;
    private ListView lv = null;
    private AlertDialogManager alert = new AlertDialogManager();

    public static ArrayList<HashMap<String, String>> getProjectsList() {
        return projectsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        if (!SplashActivity.getProjects().isEmpty()) {
            projects = SplashActivity.getProjects();
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
            lv = (ListView) findViewById(R.id.projects_lv);
            View header = getLayoutInflater().inflate(R.layout.project_tile_header_layout, null);
            lv.addHeaderView(header, null, false);
            SimpleAdapter adapter = new SimpleAdapter(this.getBaseContext(), projectsList, R.layout.project_tile_layout,
                    new String[]{TAG_NAMEP, TAG_PERCENTAGEP, TAG_DESCRIPTIONP},
                    new int[]{R.id.tile_title_tv, R.id.tile_percentage_tv, R.id.tile_description_tv});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        } else {
            String alertTitle = getString(R.string.alert_oops);
            String error = getString(R.string.home_header_no_projects);
            alert.showAlertDialog(HomeActivity.this, alertTitle, error);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(HomeActivity.this, ProjectDetailActivity.class);
        HashMap<String, String> project = projectsList.get(position - 1);
        i.putExtra(TAG_IDP, Integer.parseInt((project.get(TAG_IDP))));
        i.putExtra(TAG_PERCENTAGEP, project.get(TAG_PERCENTAGEP));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        alertLeaving(HomeActivity.this);
    }

    private void alertLeaving(Context context) {
        String okButton = getString(R.string.button_cancel);
        String quitButton = getString(R.string.button_quit);
        String alertTitle = getString(R.string.alert_warning);
        String alertMessage = getString(R.string.alert_leaving_application);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(alertTitle);
        builder.setMessage(alertMessage);
        builder.setCancelable(false);
        builder.setNegativeButton(quitButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HomeActivity.this.finish();
            }
        });
        builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
