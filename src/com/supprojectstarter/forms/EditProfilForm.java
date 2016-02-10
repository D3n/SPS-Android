package com.supprojectstarter.forms;

import com.supprojectstarter.beans.User;
import com.supprojectstarter.dao.DAOException;
import com.supprojectstarter.dao.DAOUser;
import com.supprojectstarter.sps.App;
import com.supprojectstarter.sps.R;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import java.util.ArrayList;
import java.util.List;

public final class EditProfilForm {
    private static final String ENCRYPTION_ALGORITHM = "SHA-256";
    private String result;
    private List<String> errors = new ArrayList<String>();

    public String getResult() {
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public User editProfil(String mailAddress, String password, String confirmation, String name, String firstname) {
        User userAuth = DAOUser.getInstance().findByName(mailAddress);
        try {
            if (!password.trim().equals("") && !confirmation.trim().equals("")) {
                processPassword(password, confirmation, userAuth);
            }
            processName(name, userAuth);
            processFirstname(firstname, userAuth);
            if (errors.isEmpty()) {
                DAOUser.getInstance().update(userAuth);
                result = App.getContext().getResources().getString(R.string.ER_202);
            } else {
                result = App.getContext().getResources().getString(R.string.ER_515);
            }
        } catch (DAOException e) {
            result = App.getContext().getResources().getString(R.string.ER_516);
            e.printStackTrace();
        }
        return userAuth;
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
            setErreur(e.getMessage());
        }
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
        passwordEncryptor.setPlainDigest(false);
        String EncryptedPassword = passwordEncryptor.encryptPassword(password);
        user.setPassword(EncryptedPassword);
    }

    private void validationPassword(String passwordParam, String confirmationParam) throws FormValidationException {
        String password = passwordParam.trim();
        String confirmation = confirmationParam.trim();
        if (!password.equals(confirmation)) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_511));
        } else if (password.length() < 3) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_506));
        }
    }

    private void validationName(String name) throws FormValidationException {
        if (name.equals("") || name.trim().length() < 3) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_513));
        }
    }

    private void validationFirstname(String firstname) throws FormValidationException {
        if (firstname.equals("") || firstname.trim().length() < 3) {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_514));
        }
    }

    private void setErreur(String message) {
        errors.add(message);
    }
}
