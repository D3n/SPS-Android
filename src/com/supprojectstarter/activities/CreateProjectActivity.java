package com.supprojectstarter.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import com.supprojectstarter.dao.DAOCategory;
import com.supprojectstarter.dao.DAOProject;
import com.supprojectstarter.forms.AddProjectForm;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.AlertDialogManager;
import com.supprojectstarter.utilities.SessionManager;

import java.util.List;

public class CreateProjectActivity extends Activity {
    private String mailAddress;
    private String name;
    private String category;
    private String description;
    private String amount;
    private String dateStart;
    private String dateEnd;
    private String spinnerHeader;
    private Button b_create = null;
    private EditText et_name = null;
    private Spinner sp_category = null;
    private EditText et_description = null;
    private EditText et_amount = null;
    private EditText et_date_start = null;
    private EditText et_date_end = null;
    private List<String> categoriesNames;
    private AddProjectForm form = null;
    private SessionManager session;
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_project_screen);
        session = new SessionManager(getApplicationContext());
        b_create = (Button) findViewById(R.id.create_project_b);
        et_name = (EditText) findViewById(R.id.create_project_name_et);
        et_description = (EditText) findViewById(R.id.create_project_description_et);
        et_amount = (EditText) findViewById(R.id.create_project_amount_et);
        et_date_start = (EditText) findViewById(R.id.create_project_date_start_et);
        et_date_end = (EditText) findViewById(R.id.create_project_date_end_et);
        sp_category = (Spinner) findViewById(R.id.create_project_categories_sp);
        spinnerHeader = getString(R.string.categories_spinner_header);
        categoriesNames = (DAOCategory.getInstance().findAllNames());
        categoriesNames.add(0, spinnerHeader);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesNames) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(spinnerAdapter);
        sp_category.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categoriesNames.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        b_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailAddress = session.getUserDetails().get(SessionManager.getKeyEmail());
                name = et_name.getText().toString();
                category = sp_category.getSelectedItem().toString();
                description = et_description.getText().toString();
                amount = et_amount.getText().toString();
                dateStart = et_date_start.getText().toString();
                dateEnd = et_date_end.getText().toString();
                new CreateProject().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        alertLeaving(CreateProjectActivity.this);
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
                CreateProjectActivity.this.finish();
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

    class CreateProject extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            form = new AddProjectForm();
            form.createProject(mailAddress, name, amount, description, dateStart, dateEnd, category);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (form.getErrors().isEmpty()) {
                SplashActivity.setProjects(DAOProject.getInstance().findAll());
                alertCreateProject(CreateProjectActivity.this);
            } else {
                String alertTitle = getString(R.string.alert_oops);
                String error = form.getErrors().get(0);
                alert.showAlertDialog(CreateProjectActivity.this, alertTitle, error);
            }
        }

        private void alertCreateProject(Context context) {
            String okButton = getString(R.string.button_ok);
            String alertMessage = getString(R.string.ER_203);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(alertMessage);
            builder.setCancelable(false);
            builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent i = new Intent(CreateProjectActivity.this, TabHostActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
