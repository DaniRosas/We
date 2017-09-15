package com.we.wemvp.login.ui.fragments;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.we.wemvp.R;
import com.we.wemvp.helper.MaterialSpinner;
import com.we.wemvp.login.ui.LoginActivity;
import com.we.wemvp.login.ui.LoginContract;
import com.we.wemvp.main.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;
import static com.we.wemvp.R.id.login;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements LoginContract.LoginView, View.OnClickListener{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private LoginContract.Presenter mPresenter;

    private Context mContext;
    private LoginActivity mActivity;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mErrorSignin, messageSignup, messageLogin;
    private View mProgressView;
    private View mLoginFormView;
    private Button loginButton;
    private View mContainer;
    private EditText mNameView, mSurnameView, mAgeView;
    private MaterialSpinner citizenship;
    private View view;
    private Button signUpButton, signupToolbarButton, loginToolbarButton;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = ((LoginActivity)getActivity()).getPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        mContext = getActivity().getApplicationContext();

        setViews();

        // Set up the login form.
        populateAutoComplete();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

       /* mEmailSignInButton = (Button) view.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptSignin();
            }
        });*/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        
        setPasswordListener();

        mPresenter.checkForAuthenticatedUser();

        return view;
    }

    private void setViews() {

        signupToolbarButton = getActivity().findViewById(R.id.signupToolbarButton);
        signupToolbarButton.setOnClickListener(this);

        loginToolbarButton = getActivity().findViewById(R.id.loginToolbarButton);
        loginToolbarButton.setOnClickListener(this);

        mProgressView  = view.findViewById(R.id.login_progress);
        mNameView = view.findViewById(R.id.name);
        mSurnameView = view.findViewById(R.id.surname);
        mAgeView = view.findViewById(R.id.age);
        mPasswordView = view.findViewById(R.id.password);
        loginButton = (Button) view.findViewById(R.id.email_login_in_button);
        mErrorSignin   = (TextView) view.findViewById(R.id.error_text);
        mContainer     = view.findViewById(R.id.mainContainer);
        mLoginFormView = view.findViewById(R.id.login_form);
        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.email);
        messageSignup = view.findViewById(R.id.message_signup);
        messageLogin = view.findViewById(R.id.message_login);

        signUpButton = view.findViewById(R.id.email_signup_button);
        signUpButton.setOnClickListener(this);
        setSignupListener();

        citizenship = view.findViewById(R.id.signup_country_spinner);

        fillSpinner();
    }

    private void setPasswordListener() {
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(mPasswordView.getText().length() == 0) enableLoginButton(false);
                else enableLoginButton(true);
            }
        });
    }

    private void enableLoginButton(boolean enable) {
        if(enable){
            loginButton.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        }else{
            loginButton.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        }
        loginButton.setEnabled(enable);

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (mContext.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress();

            //loginPresenter.validateLogin(email,password);
           /* mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            */
        }
    }

    private void attemptSignin() {
        /*if (mAuthTask != null) {
            return;
        }*/

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String age = mAgeView.getText().toString();
        String country = citizenship.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid age.
        if (!TextUtils.isEmpty(age) && !isAgeValid(age)) {
            mAgeView.setError(getString(R.string.error_invalid_age));
            focusView = mAgeView;
            cancel = true;
        }

        // Check for a valid country.
        if (!TextUtils.isEmpty(country) && !isCountryValid(country)) {
            citizenship.setError(getString(R.string.error_invalid_country));
            focusView = citizenship;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress();

            // loginPresenter.registerNewUser(email,password);

           /* mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            */
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isCountryValid(String country) {
        if(country.equals("United States")) return true;
        else return false;

    }

    private boolean isAgeValid(String age) {
        int ageInt = Integer.valueOf(age);
        if(ageInt < 18) return false;
        else return true;
    }

    private void fillSpinner() {
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        String country, eeuu = "";
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                if(country.equals("United States")){
                    eeuu = country;
                }else countries.add( country );
            }
        }

        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        countries.add(0,eeuu);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,R.layout.item_spinner, countries);
        citizenship.setAdapter(adapter);
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(mContext,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private boolean anyEmptyField() {
        if(mPasswordView.getText().length() == 0 || mNameView.getText().length() == 0 ||
                mSurnameView.getText().length() == 0 || citizenship.getSelectedItem().toString().length() == 0 ||
                mEmailView.getText().length() == 0){
            return true;
        }else return false;
    }

    private void enableSignupButton(boolean enable) {
        if(enable){
            signUpButton.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
        }else{
            signUpButton.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        }
        signUpButton.setEnabled(enable);
    }

    private void setSignupListener() {
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(anyEmptyField()) enableSignupButton(false);
                else enableSignupButton(true);
            }
        });
    }

    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void showProgress() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public void handleSignIn() {
        mPresenter.validateLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
    }

    @Override
    public void handleSignUp() {
        mPresenter.registerNewUser(mEmailView.getText().toString(), mPasswordView.getText().toString());

    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public void loginError(String error) {
        mErrorSignin.setText("");
        String msgError = String.format(getString(R.string.login_error_message_signin), error);
        mErrorSignin.setText(msgError);
        mErrorSignin.setVisibility(View.VISIBLE);
        mEmailView.setText("");
        mPasswordView.setText("");
    }

    @Override
    public void newUserSuccess() {
        Toast.makeText(mContext,getString(R.string.login_notice_message_signin),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void newUserError(String error) {
        mErrorSignin.setText("");
        String msgError = String.format(getString(R.string.login_error_message_signup), error);
        mErrorSignin.setText(msgError);
        mErrorSignin.setVisibility(View.VISIBLE);
        mEmailView.setText("");
        mPasswordView.setText("");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.email_signup_button:
                attemptSignin();
                break;
            case R.id.signupToolbarButton:
                mAgeView.setVisibility(View.VISIBLE);
                citizenship.setVisibility(View.VISIBLE);
                mNameView.setVisibility(View.VISIBLE);
                mSurnameView.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);
                messageSignup.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                messageLogin.setVisibility(View.GONE);

                signupToolbarButton.setVisibility(View.GONE);
                loginToolbarButton.setVisibility(View.VISIBLE);
                break;
            case R.id.loginToolbarButton:
                mAgeView.setVisibility(View.GONE);
                citizenship.setVisibility(View.GONE);
                mNameView.setVisibility(View.GONE);
                mSurnameView.setVisibility(View.GONE);
                signUpButton.setVisibility(View.GONE);
                messageSignup.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                messageLogin.setVisibility(View.VISIBLE);

                signupToolbarButton.setVisibility(View.VISIBLE);
                loginToolbarButton.setVisibility(View.GONE);
                break;
        }
    }



    private void setInputs(boolean enabled){
        mEmailView.setEnabled(enabled);
        mEmailView.setVisibility(View.VISIBLE);
        mPasswordView.setEnabled(enabled);
        //mEmailSignInButton.setEnabled(enabled);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


}
