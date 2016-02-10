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
import com.supprojectstarter.forms.RegisterForm;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.AlertDialogManager;
import com.supprojectstarter.utilities.SessionManager;

public class RegisterActivity extends Activity {
    private String mailAddress;
    private String password;
    private String confirmation;
    private String name;
    private String firstname;
    private Button b_register = null;
    private EditText et_mail_address = null;
    private EditText et_password = null;
    private EditText et_confirmation = null;
    private EditText et_name = null;
    private EditText et_firstname = null;
    private User user = null;
    private RegisterForm form = null;
    private SessionManager session;
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        session = new SessionManager(getApplicationContext());
        b_register = (Button) findViewById(R.id.register_b);
        et_mail_address = (EditText) findViewById(R.id.register_email_et);
        et_password = (EditText) findViewById(R.id.register_password_et);
        et_confirmation = (EditText) findViewById(R.id.register_password_confirmation_et);
        et_name = (EditText) findViewById(R.id.register_name_et);
        et_firstname = (EditText) findViewById(R.id.register_firstname_et);
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailAddress = et_mail_address.getText().toString();
                name = et_name.getText().toString();
                firstname = et_firstname.getText().toString();
                password = et_password.getText().toString();
                confirmation = et_confirmation.getText().toString();
                new Register().execute();
            }
        });
    }

    class Register extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            form = new RegisterForm();
            user = form.registerUser(mailAddress, password, confirmation, name, firstname);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (form.getErrors().isEmpty()) {
                String email = user.getMailAddress();
                String name = user.getName();
                String firstname = user.getFirstname();
                session.createLoginSession(email, name, firstname);
                alertRegister(RegisterActivity.this);
            } else {
                String alertTitle = getString(R.string.alert_oops);
                String error = form.getErrors().get(0);
                alert.showAlertDialog(RegisterActivity.this, alertTitle, error);
            }
        }

        private void alertRegister(Context context) {
            String okButton = getString(R.string.button_ok);
            String alertMessage = getString(R.string.ER_201);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(alertMessage);
            builder.setCancelable(false);
            builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent i = new Intent(RegisterActivity.this, TabHostActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
