package com.supprojectstarter.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.supprojectstarter.beans.User;
import com.supprojectstarter.forms.EditProfilForm;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.AlertDialogManager;
import com.supprojectstarter.utilities.SessionManager;

import java.util.HashMap;

public class EditProfileActivity extends Activity {
    private String mailAddress;
    private String password;
    private String confirmation;
    private String name;
    private String firstname;
    private EditProfilForm form = null;
    private User user = null;
    private SessionManager session;
    private HashMap<String, String> userDetails;
    private Button b_editprofil = null;
    private EditText et_password = null;
    private EditText et_confirmation = null;
    private EditText et_name = null;
    private EditText et_firstname = null;
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_screen);
        session = new SessionManager(getApplicationContext());
        userDetails = session.getUserDetails();
        b_editprofil = (Button) findViewById(R.id.edit_profil_b);
        et_password = (EditText) findViewById(R.id.edit_profil_password_et);
        et_confirmation = (EditText) findViewById(R.id.edit_profil_password_confirmation_et);
        et_name = (EditText) findViewById(R.id.edit_profil_name_et);
        et_name.setText(userDetails.get(SessionManager.getKeyName()));
        et_firstname = (EditText) findViewById(R.id.edit_profil_firstname_et);
        et_firstname.setText(userDetails.get(SessionManager.getKeyFirstname()));
        b_editprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();
                firstname = et_firstname.getText().toString();
                password = et_password.getText().toString();
                confirmation = et_confirmation.getText().toString();
                new EditProfil().execute();
            }
        });
    }

    class EditProfil extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            form = new EditProfilForm();
            mailAddress = userDetails.get(SessionManager.getKeyEmail());
            user = form.editProfil(mailAddress, password, confirmation, name, firstname);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (form.getErrors().isEmpty()) {
                String name = user.getName();
                String firstname = user.getFirstname();
                session.modifyLoginSession(name, firstname);
                alertEditProfil(EditProfileActivity.this);
            } else {
                String alertTitle = getString(R.string.alert_oops);
                String error = form.getErrors().get(0);
                alert.showAlertDialog(EditProfileActivity.this, alertTitle, error);
            }
        }

        private void alertEditProfil(Context context) {
            String okButton = getString(R.string.button_ok);
            String alertMessage = getString(R.string.ER_202);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(alertMessage);
            builder.setCancelable(false);
            builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent i = new Intent(EditProfileActivity.this, TabHostActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
