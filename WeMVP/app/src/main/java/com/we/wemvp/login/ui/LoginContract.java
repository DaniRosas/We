package com.we.wemvp.login.ui;

import com.we.wemvp.login.events.LoginEvent;

/**
 * Created by DaniRosas on 14/9/17.
 */

public interface LoginContract {
    interface LoginView{

        //To avoid double clicks, bad user experiencies, etc

        void enableInputs();
        void disableInputs();
        void showProgress();
        void hideProgress();

        //To connect with the presenter
        void handleSignIn();
        void handleSignUp();

        //To navigate to the main layer
        void navigateToMainScreen();
        void loginError(String error);

        void newUserSuccess();
        void newUserError(String error);
    }

    interface Presenter {
        void onCreate();

        //To avoid a memory leak, when is destroyed the view is destroyed the variable assigned to the presenter
        void onDestroy();

        //Check if the user have been authenticated
        void checkForAuthenticatedUser();

        //It's associated with our library, so I agreeing in the interface so when it is not associated
        //with the library, I don't have any problem and if I have to change it, I do it from the interface
        void onEventMainThread(LoginEvent event);

        void registerNewUser(String email, String password);


        //Check if the login was correct
        void validateLogin(String email, String password);
    }

    interface SignupView {
        //To avoid double clicks, bad user experiencies, etc

        void enableInputs();
        void disableInputs();
        void showProgress();
        void hideProgress();

        //To connect with the presenter
        void handleSignUp();

        //To navigate to the main layer
        void navigateToTermsOfServiceScreen();
        void loginError(String error);

        void newUserSuccess();
        void newUserError(String error);
    }
}
