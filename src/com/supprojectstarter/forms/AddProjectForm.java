package com.supprojectstarter.forms;

import com.supprojectstarter.beans.Category;
import com.supprojectstarter.beans.Project;
import com.supprojectstarter.beans.User;
import com.supprojectstarter.dao.DAOCategory;
import com.supprojectstarter.dao.DAOException;
import com.supprojectstarter.dao.DAOProject;
import com.supprojectstarter.dao.DAOUser;
import com.supprojectstarter.sps.App;
import com.supprojectstarter.sps.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public final class AddProjectForm {
    private String result;
    private List<String> errors = new ArrayList<String>();
    private DateTime sDGlobal;
    private DateTime eDGlobal;
    private String spinnerHeader = App.getContext().getResources().getString(R.string.categories_spinner_header);
    private String regexDateTime = "^((((31\\/(0?[13578]|1[02]))|((29|30)\\/(0?[1,3-9]|1[0-2])))\\/(1[6-9]|[2-9]\\d)?\\d{2})|(29\\/0?2\\/(((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))|(0?[1-9]|1\\d|2[0-8])\\/((0?[1-9])|(1[0-2]))\\/((1[6-9]|[2-9]\\d)?\\d{2})) (20|21|22|23|[0-1]?\\d):([0-5]?)\\d$";
    private User user;

    public String getResult() {
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void createProject(String mailAddress, String name, String amount, String description, String startDate, String endDate, String category) {
        Project project = new Project();
        int id = -1;
        DateTime creationDate = new DateTime();
        try {
            processName(name, project);
            processAmount(amount, project);
            processDescription(description, project);
            processStartDate(startDate, project);
            processEndDate(endDate, project);
            processCategory(category, project);
            user = DAOUser.getInstance().findByName(mailAddress);
            project.setCreatorUser(user);
            project.CreationDateWithDateTime(creationDate);
            if (user.getGroup().getLabel().equals("Admin")) {
                setErreur(App.getContext().getResources().getString(R.string.ER_203));
            }
            if (errors.isEmpty()) {
                DAOProject.getInstance().insert(project);
                project.setId(id);
                result = App.getContext().getResources().getString(R.string.ER_203);
            } else {
                result = App.getContext().getResources().getString(R.string.ER_517);
            }
        } catch (DAOException e) {
            result = App.getContext().getResources().getString(R.string.ER_518);
            e.printStackTrace();
        }
        //return project;
    }

    private void processName(String name, Project project) {
        try {
            validationName(name);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        project.setName(name);
    }

    private void processCategory(String category, Project project) {
        try {
            validationCategory(category);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        if (errors.isEmpty()) {
            Category cat = DAOCategory.getInstance().findByName(category);
            project.setCategory(cat);
        }
    }

    private void processDescription(String description, Project project) {
        try {
            validationDescription(description);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        project.setDescription(description);
    }

    private void processAmount(String amount, Project project) {
        int a = -1;
        try {
            a = validationAmount(amount);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        project.setAmountP(a);
    }

    private void processStartDate(String startDate, Project project) {
        DateTime sD = null;
        try {
            sD = validationStartDate(startDate);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        project.StartDateWithDateTime(sD);
        sDGlobal = sD;
    }

    private void processEndDate(String endDate, Project project) {
        DateTime eD = null;
        try {
            eD = validationEndDate(endDate);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        project.EndDateWithDateTime(eD);
    }

    private void validationName(String nameParam) throws FormValidationException {
        String name = nameParam.trim();
        if (name.equals("") || name.length() < 3) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_522));
        } else if (DAOProject.getInstance().findByName(name) != null) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_523));
        }
    }

    private void validationCategory(String categoryParam) throws FormValidationException {
        if (categoryParam.equals(spinnerHeader)) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_519));
        }
    }

    private int validationAmount(String amountParam) throws FormValidationException {
        int a = -1;
        String amount = amountParam.trim();
        if (amount.equals("")) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_524));
        } else {
            try {
                a = Integer.parseInt(amount);
                if (a <= 0) {
                    throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_525));
                }
            } catch (Exception e) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_525));
            }
        }
        return a;
    }

    private void validationDescription(String descriptionParam) throws FormValidationException {
        String description = descriptionParam.trim();
        if (description.equals("") || description.length() < 10) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_526));
        }
    }

    private DateTime validationStartDate(String startDateParam) throws FormValidationException {
        DateTimeFormatter formatter;
        DateTime dt;
        String startDate = startDateParam.trim();
        if (startDate.equals("")) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_527));
        } else if (!startDate.matches(regexDateTime)) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_530));
        } else {
            formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
            dt = formatter.parseDateTime(startDate);
        }
        return dt;
    }

    private DateTime validationEndDate(String endDateParam) throws FormValidationException {
        DateTimeFormatter formatter;
        DateTime dt;
        String endDate = endDateParam.trim();
        if (endDate.equals("")) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_528));
        } else if (!endDate.matches(regexDateTime)) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_531));
        } else {
            formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
            dt = formatter.parseDateTime(endDate);
            eDGlobal = dt;
        }
        if (eDGlobal.isBefore(sDGlobal)) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_529));
        }
        return dt;
    }

    private void setErreur(String message) {
        errors.add(message);
    }
}
