package com.supprojectstarter.forms;

import com.supprojectstarter.beans.Group;
import com.supprojectstarter.beans.User;
import com.supprojectstarter.dao.DAOException;
import com.supprojectstarter.dao.DAOGroup;
import com.supprojectstarter.dao.DAOUser;
import com.supprojectstarter.sps.App;
import com.supprojectstarter.sps.R;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import java.util.ArrayList;
import java.util.List;

public final class RegisterForm {
    private static final String ENCRYPTION_ALGORITHM = "SHA-256";
    private String result;
    private List<String> errors = new ArrayList<String>();

    public String getResult() {
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public User registerUser(String email, String password, String confirmation, String name, String firstname) {
        User user = new User();
        Group groupUser = new Group();
        Group groupAdmin = new Group();
        groupUser.setLabel("User");
        groupAdmin.setLabel("Admin");
        // Test if the groups are already created in database, if not then create them
        if (DAOGroup.getInstance().findByName("Admin") == null) {
            DAOGroup.getInstance().insert(groupAdmin);
        }
        if (DAOGroup.getInstance().findByName("User") == null) {
            DAOGroup.getInstance().insert(groupUser);
        }
        // The first user to signin is set to Admin, after that others are normal users by default
        if (DAOUser.getInstance().findAll().isEmpty()) {
            user.setGroup(DAOGroup.getInstance().findByName("Admin"));
        } else {
            user.setGroup(DAOGroup.getInstance().findByName("User"));
        }
        try {
            processEmail(email, user);
            processPassword(password, confirmation, user);
            processName(name, user);
            processFirstname(firstname, user);
            if (errors.isEmpty()) {
                DAOUser.getInstance().insert(user);
                result = App.getContext().getResources().getString(R.string.ER_201);
            } else {
                result = App.getContext().getResources().getString(R.string.ER_508);
            }
        } catch (DAOException e) {
            result = App.getContext().getResources().getString(R.string.ER_509);
            e.printStackTrace();
        }
        return user;
    }

    private void processEmail(String email, User user) {
        try {
            validationEmail(email);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        user.setMailAddress(email);
    }

    private void processName(String name, User user) {
        try {
            validationName(name);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        user.setName(name);
    }

    private void processFirstname(String Firstname, User user) {
        try {
            validationFirstname(Firstname);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        user.setFirstname(Firstname);
    }

    private void processPassword(String password, String confirmation, User user) {
        try {
            validationPassword(password, confirmation);
        } catch (FormValidationException e) {
            setErreur(e.getMessage());
        }
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
        passwordEncryptor.setPlainDigest(false);
        String EncryptedPassword = passwordEncryptor.encryptPassword(password);
        user.setPassword(EncryptedPassword);
    }

    private void validationEmail(String emailParam) throws FormValidationException {
        String email = emailParam.trim();
        if (!email.equals("")) {
            if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_503));
            } else if (DAOUser.getInstance().findByName(email) != null) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_510));
            }
        } else {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_505));
        }
    }

    private void validationPassword(String passwordParam, String confirmationParam) throws FormValidationException {
        String password = passwordParam.trim();
        String confirmation = confirmationParam.trim();
        if (!password.equals("") && !confirmation.equals("")) {
            if (!password.equals(confirmation)) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_511));
            } else if (password.length() < 3 || confirmation.length() < 3) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_506));
            }
        } else {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_512));
        }
    }

    private void validationName(String nameParam) throws FormValidationException {
        String name = nameParam.trim();
        if (!name.equals("") && name.length() < 3) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_513));
        }
    }

    private void validationFirstname(String firstnameParam) throws FormValidationException {
        String firstname = firstnameParam.trim();
        if (!firstname.equals("") && firstname.length() < 3) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_514));
        }
    }

    private void setErreur(String codeError) {
        errors.add(codeError);
    }
}
