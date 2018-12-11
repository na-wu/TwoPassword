package controller;

import View.AbstractView;
import View.AddOView;
import View.AddView;
import View.PasswordView;
import model.PasswordModel;
import org.jasypt.util.text.StrongTextEncryptor;
import util.EmptyOrSpaceException;
import util.Password;
import util.PasswordDB;

import javax.swing.*;
import java.sql.SQLException;

public class AddPasswordController extends PasswordController {

    private PasswordModel passwords;
    private Boolean caughtError;
    private Password pass;
    private PasswordDB db;
    private StrongTextEncryptor encryptor;

    public AddPasswordController(PasswordModel passwords, String mode, PasswordDB db, String key) {
        this.passwords = passwords;
        this.db = db;
        pass = new Password();
        setModel(this.passwords);
        encryptor = new StrongTextEncryptor();
        encryptor.setPassword(key);

        if(mode.equals(PasswordView.ADD)) {
            setView(new AddView(this.passwords, this));
        } else if(mode.equals(PasswordView.ADDOWN)) {
            setView(new AddOView(this.passwords, this));
        }
        ((AbstractView) getView()).setVisible(true);
    }

    public void operation(String option, String name, int length, String userPass) {
        switch (option) {
            case AddView.GEN:
                if (addPassword(name, length, userPass, AddView.GEN))
                    ((AbstractView) getView()).setVisible(true); //If an exception was caught, leave view open so user may make change
                else
                    ((AbstractView) getView()).setVisible(false); //otherwise, close view
                break;
            case AddOView.ADD:
                if (addPassword(name, length, userPass, AddOView.ADD))
                    ((AbstractView) getView()).setVisible(true); //If an exception was caught, leave view open so user may make change
                else
                    ((AbstractView) getView()).setVisible(false); //otherwise, close view
                break;
        }

    }

    private Boolean addPassword(String name, int length, String userPass, String mode) {
        Boolean error = false;
        caughtError = false;
        try {
            if (mode.equals(AddView.GEN)) {
                try {
                    pass.generatePassword(length, name);
                    Password encrypted = new Password(pass);
                    encrypted.setPasswordName(encryptor.encrypt(name));
                    encrypted.setPasswordString(encryptor.encrypt(encrypted.getPasswordString()));
                    db.addPassword(encrypted);
                    this.passwords.add(pass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (mode.equals(AddOView.ADD)) {
                try {
                    pass.createPasswordField(userPass, name);
                    Password encrypted = new Password(pass);
                    encrypted.setPasswordName(encryptor.encrypt(name));
                    encrypted.setPasswordString(encryptor.encrypt(encrypted.getPasswordString()));
                    db.addPassword(encrypted);
                    this.passwords.add(pass);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (EmptyOrSpaceException g) {
            JOptionPane.showMessageDialog((AbstractView) getView(), g.getMessage());
            error = true;
        }

        return error;
    }

}
