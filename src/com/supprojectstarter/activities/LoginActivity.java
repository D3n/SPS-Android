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
import android.widget.TextView;
import com.supprojectstarter.beans.User;
import com.supprojectstarter.forms.LoginForm;
import com.supprojectstarter.sps.R;
import com.supprojectstarter.utilities.AlertDialogManager;
import com.supprojectstarter.utilities.SessionManager;

public class LoginActivity extends Activity {
    private String mailAddress;
    private String password;
    private Button b_login = null;
    private EditText et_mail_address = null;
    private EditText et_password = null;
    private TextView tv_register = null;
    private User user = null;
    private LoginForm loginForm = null;
    private SessionManager session;
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        session = new SessionManager(getApplicationContext());
        b_login = (Button) findViewById(R.id.login_b);
        et_mail_address = (EditText) findViewById(R.id.login_email_et);
        et_password = (EditText) findViewById(R.id.login_password_et);
        tv_register = (TextView) findViewById(R.id.login_register_tv);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailAddress = et_mail_address.getText().toString();
                password = et_password.getText().toString();
                new Login().execute();
            }
        });
    }

    class Login extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            loginForm = new LoginForm();
            user = loginForm.connectUser(mailAddress, password);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (loginForm.getErrors().isEmpty()) {
                String email = user.getMailAddress();
                String name = user.getName();
                String firstname = user.getFirstname();
                session.createLoginSession(email, name, firstname);
                alertLogin(LoginActivity.this);
            } else {
                String alertTitle = getString(R.string.alert_oops);
                String error = loginForm.getErrors().get(0);
                alert.showAlertDialog(LoginActivity.this, alertTitle, error);
            }
        }

        private void alertLogin(Context context) {
            String okButton = getString(R.string.button_ok);
            String alertMessage = getString(R.string.ER_200);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(alertMessage);
            builder.setCancelable(false);
            builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    Intent i = new Intent(LoginActivity.this, TabHostActivity.class);
                    startActivity(i);
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
