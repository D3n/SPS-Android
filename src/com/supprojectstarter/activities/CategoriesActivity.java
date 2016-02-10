package com.supprojectstarter.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.supprojectstarter.beans.Category;
import com.supprojectstarter.beans.Contribution;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.dao.DAOCategory;
import com.supprojectstarter.listitems.EntryAdapter;
import com.supprojectstarter.listitems.EntryItem;
import com.supprojectstarter.listitems.Item;
import com.supprojectstarter.listitems.SectionItem;
import com.supprojectstarter.sps.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends ListActivity {
    private static final String TAG_CATEGORY_NAME = "categoryName";
    private static final String TAG_IDP = "id";
    private static final String TAG_PERCENTAGEP = "percentage";
    private String spinnerHeader;
    private Spinner categoriesSpinner = null;
    private List<String> categoriesNames;
    private List<Category> categories;
    private List<Project> projects;
    private ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_screen);
        categoriesSpinner = (Spinner) findViewById(R.id.categories_spinner);
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
        categoriesSpinner.setAdapter(spinnerAdapter);
        categoriesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = categoriesNames.get(position);
                if (!categoryName.equals(spinnerHeader)) {
                    Intent i = new Intent(CategoriesActivity.this, CategoryDetailActivity.class);
                    i.putExtra(TAG_CATEGORY_NAME, categoryName);
                    startActivity(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        categories = DAOCategory.getInstance().findAll();

        for (Category category : categories) {
            projects = category.getProjects();
            String categoryName = category.getName();
            items.add(new SectionItem(categoryName));

            for (Project project : projects) {
                double totalContributions = 0;
                double projectAmount = 0;
                int projectId = project.getId();
                String projectName = project.getName();
                String projectDescription = project.getDescription();
                if (projectDescription.length() > 70) {
                    projectDescription = projectDescription.substring(0, 70) + "...";
                }
                projectAmount = project.getAmountP();
                for (Contribution contribution : project.getContributions()) {
                    totalContributions = totalContributions + contribution.getAmountC();
                }
                String projectPercentage = Math.round((totalContributions / projectAmount * 100) * 100.0) / 100.0 + " %";
                items.add(new EntryItem(projectId, projectName, projectPercentage, projectDescription));
            }
        }
        EntryAdapter adapter = new EntryAdapter(CategoriesActivity.this, items);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listview, View view, int position, long id) {
        if (!items.get(position).isSection()) {
            EntryItem item = (EntryItem) items.get(position);
            Intent i = new Intent(CategoriesActivity.this, ProjectDetailActivity.class);
            i.putExtra(TAG_IDP, item.id);
            i.putExtra(TAG_PERCENTAGEP, item.percentage);
            startActivity(i);
        }
        super.onListItemClick(listview, view, position, id);
    }

    @Override
    public void onBackPressed() {
        alertLeaving(CategoriesActivity.this);
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
                CategoriesActivity.this.finish();
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
