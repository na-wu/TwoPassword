package model;

import util.Password;

import java.util.ArrayList;


public class PasswordModel extends AbstractModel {
    
    public ArrayList<Password> ListOfPasswords;

    private ArrayList<Password> passwords;

    public PasswordModel(ArrayList<Password> pass) {
        this.passwords = pass;
    }

    private void sortPasswordsByName() {

        if(passwords.size() > 1) {
            passwords.sort(new Password.ComparePassword());
        } else {
            return;
        }

    }

    public Object[] getNames() {
        ArrayList<String> temp = new ArrayList<>();
        for (Password p : passwords)
            temp.add(p.getPasswordName());

        return temp.toArray();
    }

    public Password getPassword(int index) {
        return passwords.get(index);
    }

    public void renamePassword(Password newP, int index) {
        passwords.set(index, newP);
        sortPasswordsByName();
        // An add ModelEvent can handle renaming as well
        notifyChanged(new ModelEvent(this, index, "add"));
    }

    public ArrayList<Password> getPasswords() {
        return this.passwords;
    }

    public void removePassword(int index) throws ArrayIndexOutOfBoundsException {
        passwords.remove(index);
        notifyChanged(new ModelEvent(this, index, "remove"));
    }

    public void add(Password password) throws IllegalArgumentException {
        for (Password p : passwords)
            if (p.getPasswordName().equals(password.getPasswordName()))
                throw new IllegalArgumentException("Password with name " + password.getPasswordName() + " already exists");
        passwords.add(password);
        sortPasswordsByName();
        notifyChanged(new ModelEvent(this, 1, "add"));
    }

}
