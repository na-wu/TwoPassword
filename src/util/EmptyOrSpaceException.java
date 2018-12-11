package util;

/*
    Custom Exception thrown when input contains whitespace or is empty
 */
public class EmptyOrSpaceException extends Exception {
    public EmptyOrSpaceException() {
        super("Name or Password cannot be empty or contain white space");
    }
}
