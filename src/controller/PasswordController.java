package controller;

import model.PasswordModel;
import View.AbstractView;
import View.PasswordView;



import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.StrongTextEncryptor;
import util.EmptyOrSpaceException;
import util.Password;
import util.PasswordDB;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PasswordController extends AbstractController {
    private ArrayList<Password> passwords;
    private PasswordModel paModel;
    private PasswordDB db;
    private StrongTextEncryptor decryptor;
    private StrongTextEncryptor encryptor;
    private String password;
    private static final int MAX_ATTEMPTS = 5;

    private String RENAME_ERROR = "An error occurred while renaming ";
    private String ERROR = "There was an error! ";
    private String REMOVE_ERROR = "An error occurred while removing ";
    private String DB_ERROR = "Database error! ";



    private Map<String, String> passMap;

    public PasswordController() {

    }

    public PasswordController(PasswordDB db) {
        // Check if they have used app before
        try {
            if (db.getPasswords().isEmpty()) {
                JOptionPane.showMessageDialog(null, "It is time to create a master password. "
                        + "You will need this password in order to access passwords stored on this computer.\n"
                        + "Note: This password won't take effect until you use the app to store/generate a password. "
                        + "Once the master password is entered, it "
                        + "CANNOT be changed!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, ERROR);
            System.exit(-1);
        } catch (EmptyOrSpaceException e) {
            e.printStackTrace();
        }
        passMap = new HashMap<>();

            // Construct Pane for taking in password
        JPanel prompt = new JPanel();
        JLabel label = new JLabel("Password: ");
        JPasswordField passField = new JPasswordField(20);
        String[] options = {"Ok", "Cancel"};
        prompt.add(label);
        prompt.add(passField);

        boolean success = false;
        int attempts = 0;
        int selection = JOptionPane.showOptionDialog(null, prompt,
                "Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (selection == 0) {
            password = new String(passField.getPassword());
        }
        if (password == null || password.isEmpty()) {
            System.exit(0);
        }
        while (!success) {
            encryptor = new StrongTextEncryptor();
            encryptor.setPassword(password);
            decryptor = new StrongTextEncryptor();
            decryptor.setPassword(password);

            try {
                this.db = db;
                this.passwords = db.getPasswords();
                for (Password p : passwords) {
                    String paName = p.getPasswordName();
                    String paString = p.getPasswordString();
                    p.setPasswordName(encryptor.decrypt(paName));
                    p.setPasswordString(encryptor.decrypt(paString));
                    passMap.put(p.getPasswordName(), paName);
                }
                success = true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, DB_ERROR, "Database error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            } catch (EncryptionOperationNotPossibleException ex) {
                ex.printStackTrace();
                //If the user got here, it means they entered the wrong password
                JOptionPane.showMessageDialog(null, "Incorrect password entered");
                int result = JOptionPane.showOptionDialog(null, prompt,
                        "Password", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (result == 0) {
                    password = new String(passField.getPassword());
                } else {
                    System.exit(0);
                    attempts++;
                    if (attempts == MAX_ATTEMPTS) {
                        System.exit(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        paModel = new PasswordModel(passwords);
        setModel(paModel);
        setView(
                new PasswordView((PasswordModel) getModel(), this));
        ((AbstractView) getView()).setVisible(true);
    }

    public void operation(String option, int index) {
        /* When a button is pressed, it will pass its name as the option variable*/
        if (option.equals(PasswordView.ADD)) {
            new AddPasswordController(paModel, PasswordView.ADD, db, password);
        } else if (option.equals(PasswordView.ADDOWN)) {
            new AddPasswordController(paModel, PasswordView.ADDOWN, db, password);
        } else if (option.equals(PasswordView.COPY)) {
            try {
                String temp = ((PasswordModel) getModel()).getPassword(index).getPasswordString();
                StringSelection stringSelection = new StringSelection(temp);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog((AbstractView) getView(), "No password to copy to clipboard");
            }
        } else if (option.equals(PasswordView.REMOVE)) {
            try {
                Password passToRemove = paModel.getPassword(index);
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you wish to remove "
                                + passToRemove.getPasswordName()
                                + "?", "Remove Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    try {
                        db.removePassword(passMap.get(passToRemove.getPasswordName()));
                        paModel.removePassword(index);
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog((AbstractView) getView(), REMOVE_ERROR + passToRemove.getPasswordName(), "Database error", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog((AbstractView) getView(), "No passwords to remove, list is empty.");
                e.printStackTrace();
            } catch (Exception e) {

            }
        } else if (option.equals(PasswordView.RENAME)) {
            String newName = JOptionPane.showInputDialog("Please enter the new name of the password");
            if (newName.isEmpty())
                JOptionPane.showMessageDialog((AbstractView) getView(), "Password name cannot be blank");
            else if (newName.contains(" "))
                JOptionPane.showMessageDialog((AbstractView) getView(), "Please do not include any spaces in the name of the password\n\nNew password name not set");
            else
                for (Password p : paModel.getPasswords())
                    if (p.getPasswordName().equals(newName))
                        JOptionPane.showMessageDialog((AbstractView) getView(), "Password with this name already exists");

            Password password = ((PasswordModel) getModel()).getPassword(index);
            try {
                String encrypted = encryptor.encrypt(newName);
                db.renamePassword(passMap.get(password.getPasswordName()), encrypted);
                password.setPasswordName(newName);
                ((PasswordModel) getModel()).renamePassword(password, index);
            } catch (Exception e) {
                JOptionPane.showMessageDialog((AbstractView) getView(), RENAME_ERROR + password.getPasswordName(), DB_ERROR, JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else if (option.equals(PasswordView.EXIT)) {
            System.exit(0);
        }
    }
}


