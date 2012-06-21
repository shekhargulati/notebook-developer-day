package com.summit.notebook.social.account;

public class UsernameAlreadyInUseException extends RuntimeException {
    
    public UsernameAlreadyInUseException(String username) {
        super("The username '" + username + "' is already in use.");
    }
}
