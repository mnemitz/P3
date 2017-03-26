package model;

/**
 * Created by Matt Nemitz on 3/26/2017.
 *
 * Exception for when someone tries to create a user, but one already exists with the specified email
 */
public class UserExistsException extends Exception
{
    public UserExistsException(String msg)
    {
        super(msg);
    }
}
