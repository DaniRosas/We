package com.we.wemvp.login.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.we.wemvp.R;
import com.we.wemvp.login.LoginPresenterImpl;
import com.we.wemvp.login.ui.fragments.LoginFragment;
import com.we.wemvp.login.ui.fragments.ToSFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private Context mContext;

    private Button signinButton;
    private Button loginButton;

    private LoginContract.Presenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        signinButton = (Button) findViewById(R.id.signupToolbarButton);

        loginButton = (Button) findViewById(R.id.loginToolbarButton);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.fragment_container, loginFragment).commit();



        loginPresenter = new LoginPresenterImpl(loginFragment);
        loginPresenter.onCreate();
        //loginPresenter.checkForAuthenticatedUser();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
    }

    public LoginContract.Presenter getPresenter(){
        return (LoginContract.Presenter) loginPresenter;
    }


    public void goToToSFragment() {
        FragmentManager fragmentManager3 = getFragmentManager();
        FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();

        ToSFragment tosFragment = new ToSFragment();
        fragmentTransaction3.replace(R.id.fragment_container, tosFragment).commit();

        loginButton.setVisibility(View.GONE);
        signinButton.setVisibility(View.GONE);
    }

}

