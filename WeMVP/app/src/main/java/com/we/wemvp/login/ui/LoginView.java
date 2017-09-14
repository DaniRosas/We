package com.we.wemvp.login.ui;

/**
 * Created by DaniRosas on 14/9/17.
 */

public interface LoginView {

    //To avoid double clicks, bad user experiencies, etc

    void enableInputs();
    void disableInputs();
    void showProgress();
    void hideProgress();

    //To connect with the presenter
    void handleSignUp();
    void handleSignIn();

    //To navigate to the main layer
    void navigateToMainScreen();
    void loginError(String error);


    void newUserSuccess();
    void newUserError(String error);
}
