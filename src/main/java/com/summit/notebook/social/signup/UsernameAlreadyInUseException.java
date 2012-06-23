package com.summit.notebook.social.signup;

public class UsernameAlreadyInUseException extends RuntimeException {

    public UsernameAlreadyInUseException(String username) {
        super("User already exist with username " + username);
    }

}
