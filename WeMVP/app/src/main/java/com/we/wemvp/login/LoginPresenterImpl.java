package com.we.wemvp.login;

import com.we.wemvp.libs.EventBus;
import com.we.wemvp.libs.GreenRobotEventBus;
import com.we.wemvp.login.events.LoginEvent;
import com.we.wemvp.login.ui.LoginView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by DaniRosas on 14/9/17.
 */

public class LoginPresenterImpl implements LoginPresenter {
    EventBus eventBus;
    LoginView loginView;
    LoginInteractor loginInteractor;


    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.eventBus = GreenRobotEventBus.getInstance();
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateLogin(String email, String password) {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }
        loginInteractor.doSignIn(email, password);
    }

    @Override
    public void registerNewUser(String email, String password) {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }
        loginInteractor.doSignUp(email, password);
    }

    //Check if view exist, and if it exist, call methods to disable inputs and show progressbar
    @Override
    public void checkForAuthenticatedUser() {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }

        loginInteractor.checkAlreadyAuthenticated();
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    //Assignation to null the view to avoid memory leack
    @Override
    public void onDestroy() {
        loginView = null;
        eventBus.unregister(this);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()) {
            case LoginEvent.onSignInError:
                onSignInError(event.getErrorMesage());
                break;
            case LoginEvent.onSignInSuccess:
                onSignInSuccess();
                break;
            case LoginEvent.onSignUpError:
                onSignUpError(event.getErrorMesage());
                break;
            case LoginEvent.onSignUpSuccess:
                onSignUpSuccess();
                break;
            case LoginEvent.onFailedToRecoverSession:
                onFailedToRecoverSession();
                break;
        }
    }

    private void onSignInSuccess() {
        if (loginView != null) {
            loginView.navigateToMainScreen();
        }
    }

    private void onSignUpSuccess() {
        if (loginView != null) {
            loginView.newUserSuccess();
        }
    }

    private void onSignInError(String error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInputs();
            loginView.loginError(error);
        }
    }

    private void onSignUpError(String error) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInputs();
            loginView.newUserError(error);
        }
    }

    private void onFailedToRecoverSession() {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInputs();
        }
    }
}
