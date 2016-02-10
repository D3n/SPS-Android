package com.supprojectstarter.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.supprojectstarter.beans.Contribution;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.beans.User;
import com.supprojectstarter.dao.DAOContribution;
import com.supprojectstarter.dao.DAOProject;
import com.supprojectstarter.dao.DAOUser;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.AlertDialogManager;
import com.supprojectstarter.utilities.SessionManager;

import java.util.HashMap;

public class ProjectDetailActivity extends Activity {
    private static final String TAG_USER_EMAIL = "email";
    private static final String TAG_IDP = "id";
    private static final String TAG_PERCENTAGEP = "percentage";
    private int id;
    private int totalContributions = 0;
    private String percentage;
    private String contributionAmount;
    private User user;
    private Project project;
    private SessionManager session;
    private AlertDialogManager alert = new AlertDialogManager();
    private TextView tv_name = null;
    private TextView tv_percentage = null;
    private TextView tv_amount = null;
    private TextView tv_creator = null;
    private TextView tv_startDate = null;
    private TextView tv_endDate = null;
    private TextView tv_description = null;
    private TextView tv_success = null;
    private LinearLayout ll_contribute = null;
    private EditText et_contribute = null;
    private Button b_contribute = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_detail_screen);
        session = new SessionManager(getApplicationContext());
        tv_name = (TextView) findViewById(R.id.detail_name_tv);
        tv_percentage = (TextView) findViewById(R.id.detail_percentage_tv);
        tv_amount = (TextView) findViewById(R.id.detail_amount_tv);
        tv_success = (TextView) findViewById(R.id.detail_success_tv);
        tv_creator = (TextView) findViewById(R.id.detail_creator_tv);
        tv_startDate = (TextView) findViewById(R.id.detail_sdate_tv);
        tv_endDate = (TextView) findViewById(R.id.detail_edate_tv);
        tv_description = (TextView) findViewById(R.id.detail_description_tv);
        ll_contribute = (LinearLayout) findViewById(R.id.contribute_ll);
        Intent intent = getIntent();
        if (intent.hasExtra(TAG_IDP) && intent.hasExtra(TAG_PERCENTAGEP)) {
            id = getIntent().getIntExtra(TAG_IDP, id);
            percentage = getIntent().getStringExtra(TAG_PERCENTAGEP);
        }
        project = DAOProject.getInstance().find(id);
        if (session.isLoggedIn() == true) {
            ll_contribute.setVisibility(View.VISIBLE);
        } else {
            ll_contribute.setVisibility(View.INVISIBLE);
        }
        et_contribute = (EditText) findViewById(R.id.contribute_et);
        b_contribute = (Button) findViewById(R.id.contribute_b);
        b_contribute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                contributionAmount = et_contribute.getText().toString();
                String alertTitle = getString(R.string.alert_error);
                String alertMessage = getString(R.string.alert_null_contribution);
                if (contributionAmount.length() == 0) {
                    alert.showAlertDialog(ProjectDetailActivity.this, alertTitle, alertMessage);
                } else if (Integer.parseInt(contributionAmount) == 0) {
                    alert.showAlertDialog(ProjectDetailActivity.this, alertTitle, alertMessage);
                } else {
                    new Contribute().execute();
                }
            }
        });
        HashMap<String, String> userDetails = session.getUserDetails();
        user = DAOUser.getInstance().findByName(userDetails.get(TAG_USER_EMAIL));
        for (Contribution c : project.getContributions()) {
            totalContributions += c.getAmountC();
        }
        if (totalContributions >= project.getAmountP()) {
            tv_success.setText(R.string.detail_success);
            tv_success.setTextColor(Color.GREEN);
        }
        tv_name.setText(project.getName());
        tv_percentage.setText(percentage);
        tv_amount.setText(Integer.toString(totalContributions) + "€ / " + project.getAmountP() + "€");
        tv_creator.setText(project.getCreatorUser().getFirstname() + " " + project.getCreatorUser().getName());
        tv_startDate.setText(project.getStartDate());
        tv_endDate.setText(project.getEndDate());
        tv_description.setText(project.getDescription());
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
        switch (item.getItemId()) {
            case R.id.menu_login:
                Intent i = new Intent(ProjectDetailActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.menu_register:
                i = new Intent(ProjectDetailActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.menu_logout:
                i = new Intent(ProjectDetailActivity.this, TabHostActivity.class);
                session.logoutUser();
                startActivity(i);
                finish();
                return true;
            case R.id.menu_profil:
                i = new Intent(ProjectDetailActivity.this, EditProfileActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class Contribute extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Contribution contribution = new Contribution();
            contribution.setProject(project);
            contribution.setContributorUser(user);
            contribution.setAmountC(Integer.parseInt(contributionAmount));
            DAOContribution.getInstance().insert(contribution);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            SplashActivity.setProjects(DAOProject.getInstance().findAll());
            alertThanks(ProjectDetailActivity.this);
        }

        private void alertThanks(Context context) {
            String okButton = getString(R.string.button_ok);
            String alertTitle = getString(R.string.alert_thanks);
            String alertMessage = getString(R.string.alert_contribution_ok);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(alertTitle);
            builder.setMessage(alertMessage);
            builder.setCancelable(false);
            builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent i = new Intent(ProjectDetailActivity.this, TabHostActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
