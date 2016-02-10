package com.supprojectstarter.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.*;
import com.supprojectstarter.beans.Category;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.dao.DAOCategory;
import com.supprojectstarter.dao.DAOProject;
import com.supprojectstarter.sps.R;

import java.util.List;

@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity {
    private static final int SPLASHEND = 0;
    private static final int SPLASHTIMER = 1500;
    private static List<Project> projects;
    private static List<Category> categories;
    private boolean connected;
    private Handler splashHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case SPLASHEND:
                    if (connected) {
                        new GetLists().execute();
                    } else {
                        alertConnection(SplashActivity.this);
                        break;
                    }
            }
            super.handleMessage(message);
        }
    };

    public static List<Project> getProjects() {
        return projects;
    }

    public static void setProjects(List<Project> list) {
        projects = list;
    }

    public static List<Category> getCategories() {
        return categories;
    }

    public static void setCategories(List<Category> list) {
        categories = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        connected = isNetworkAvailable();
        Message message = new Message();
        message.what = SPLASHEND;
        splashHandler.sendMessageDelayed(message, SPLASHTIMER);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void retry() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onFinish() {
                if (isNetworkAvailable()) {
                    new GetLists().execute();
                } else {
                    alertConnection(SplashActivity.this);
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }

    private void alertConnection(Context context) {
        String okButton = getString(R.string.button_ok);
        String quitButton = getString(R.string.button_quit);
        String alertTitle = getString(R.string.alert_oops);
        String alertMessage = getString(R.string.alert_no_connection);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(alertTitle);
        builder.setMessage(alertMessage);
        builder.setCancelable(false);
        builder.setNegativeButton(quitButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SplashActivity.this.finish();
            }
        });
        builder.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                retry();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    class GetLists extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args) {
            projects = DAOProject.getInstance().findAll();
            categories = DAOCategory.getInstance().findAll();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(SplashActivity.this, TabHostActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
