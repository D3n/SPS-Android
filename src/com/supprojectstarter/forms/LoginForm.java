package com.supprojectstarter.forms;

import com.supprojectstarter.beans.User;
import com.supprojectstarter.dao.DAOException;
import com.supprojectstarter.dao.DAOUser;
import com.supprojectstarter.sps.App;
import com.supprojectstarter.sps.R;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import java.util.ArrayList;
import java.util.List;

public final class LoginForm {
    private static final String ENCRYPTION_ALGORITHM = "SHA-256";
    private String result;
    private List<String> errors = new ArrayList<String>();

    public String getResult() {
        return result;
    }

    public List<String> getErrors() {
        return errors;
    }

    public User connectUser(String email, String password) {
        User user = null;
        try {
            if (processEmail(email)) {
                user = DAOUser.getInstance().findByName(email);
                processPassword(password, user);
            }
            if (errors.isEmpty()) {
                result = App.getContext().getResources().getString(R.string.ER_200);
            } else {
                result = App.getContext().getResources().getString(R.string.ER_500);
            }
        } catch (DAOException e) {
            result = App.getContext().getResources().getString(R.string.ER_501);
            e.printStackTrace();
        }
        return user;
    }

    private Boolean processEmail(String email) {
        try {
            validationEmail(email);
        } catch (FormValidationException e) {
            setError(e.getMessage());
        }
        if (errors.isEmpty()) {
            return true;
        }
        return false;
    }

    private void processPassword(String password, User user) {
        try {
            validationPassword(password);
        } catch (FormValidationException e) {
            setError(e.getMessage());
        }
        ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
        passwordEncryptor.setAlgorithm(ENCRYPTION_ALGORITHM);
        passwordEncryptor.setPlainDigest(false);
        if (!passwordEncryptor.checkPassword(password, user.getPassword()) && errors.isEmpty()) {
            setError(App.getContext().getResources().getString(R.string.ER_502));
        }
    }

    private void validationEmail(String emailParam) throws FormValidationException {
        String email = emailParam.toString();
        if (!email.equals("")) {
            if (!email.matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_503));
            } else if (DAOUser.getInstance().findByName(email) == null) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_504));
            }
        } else {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_505));
        }
    }

    private void validationPassword(String passwordParam) throws FormValidationException {
        String password = passwordParam.toString();
        if (!password.equals("")) {
            if (password.length() < 3) {
                throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_506));
            }
        } else {
            throw new FormValidationException(App.getContext().getResources().getString(R.string.ER_507));
        }
    }

    private void setError(String codeError) {
        errors.add(codeError);
    }
}
