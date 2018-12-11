package util;

import sun.invoke.empty.Empty;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

public class Password extends EmptyOrSpaceException {
    private static final char[] CHARACTERS
            = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '_',
            '!', '@', '#', '$', '%', '&', '*', '^', '-'};


    public String passwordName;
    public String passNamePassword;

    public Password() {

    }

    public Password(String name, String password) throws EmptyOrSpaceException {
        if(name.isEmpty() || name.contains(" ") || password.isEmpty() || password.contains(" ")) {
            throw new EmptyOrSpaceException();
        }
        this.passwordName = name;
        this.passNamePassword = password;
    }

    public Password(Password p) {
        this.passwordName = p.passwordName;
        this.passNamePassword = p.passNamePassword;

    }


    //REQUIRES: length > 0 and non-empty name
    //MODIFIES: this
    //EFFECTS: generates a password based of given password length and name

    public Password generatePassword(int length, String name) throws EmptyOrSpaceException {

        name = name.trim();
        if (name.isEmpty() || name.contains(" ")) {
            throw new EmptyOrSpaceException();
        }
        char[] temp = new char[length];
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            temp[i] = CHARACTERS[rand.nextInt(CHARACTERS.length)];
            String password = new String(temp);
            this.passwordName = name;
            this.passNamePassword = password;
        }
        return this;

    }



    /*User inputs password name and their own password
     * Creates field with given name and password*/
    public Password createPasswordField(String password, String name) throws EmptyOrSpaceException {
        name = name.trim();
        password = password.trim();

        if (name.isEmpty() || name.contains(" ") || password.isEmpty() || password.contains(" ")) {
            throw new EmptyOrSpaceException();
        }
        this.passwordName = password;
        this.passwordName = name;
        return this;
    }

    public void setPasswordString(String password) throws EmptyOrSpaceException {
        if(password.contains(" ") || password.isEmpty()) {
            throw new EmptyOrSpaceException();
        }
        this.passNamePassword = password;
    }

    public String getPasswordString() {
        return this.passNamePassword;
    }


    public void setPasswordName(String name) throws EmptyOrSpaceException {
        if (name.isEmpty() || name.contains(" ")) {
            throw new EmptyOrSpaceException();
        }
        this.passwordName = name;
    }

    public String getPasswordName() {
        return this.passwordName;
    }

    static public class ComparePassword implements Comparator<Password> {

        @Override
        public int compare(Password password1, Password password2) throws NullPointerException, ClassCastException {
            return password1.getPasswordName().compareTo(password2.getPasswordName());
        }
    }

    @Override
    public String toString() {
        return "Name: " + this.getPasswordName() + ", Password: " + this.getPasswordString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        Password p = (Password) obj;
        if (!(p instanceof Password))
            throw new ClassCastException();

        return p.passNamePassword.equals(this.passNamePassword) && p.passwordName.equals(this.passwordName); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.passwordName);
        hash = 53 * hash + Objects.hashCode(this.passNamePassword);
        return hash;
    }
}
