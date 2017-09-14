package com.we.wemvp.login;

/**
 * Created by DaniRosas on 14/9/17.
 */

public interface LoginInteractor {
    void checkAlreadyAuthenticated();
    void doSignUp(String email, String password);
    void doSignIn(String email, String password);
}
