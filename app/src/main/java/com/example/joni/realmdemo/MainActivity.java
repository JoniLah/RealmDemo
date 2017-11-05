package com.example.joni.realmdemo;

import com.example.joni.realmdemo.module.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {


    private String un = null;
    private String pw = null;
    private boolean usernameNotNull = false;
    private boolean passwordNotNull = false;

    protected static String USER_USERNAME = "";
    protected static String USER_PASSWORD = "";
    protected static boolean REMEMBER_ME = false;
    protected static boolean AUTO_LOGIN = false;
    public static final String PREFS_NAME = "RememberMePrefsFile";

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        USER_USERNAME = settings.getString("pref_name", null);
        USER_PASSWORD = settings.getString("pref_password", null);
        REMEMBER_ME = settings.getBoolean("pref_checkbox", false);
        AUTO_LOGIN = settings.getBoolean("pref_auto_login", false);


        // Now get a handle to any View contained
        // within the main layout you are using
        View mainView = findViewById(R.id.rootLayoutMain);

        // Find the root view
        View root = mainView.getRootView();

        // Set the color
        int color = ContextCompat.getColor(getApplicationContext(), R.color.background);
        root.setBackgroundColor(color);

        // Opens the Realm database
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        if (AUTO_LOGIN) {
            requestLogin(USER_USERNAME, USER_PASSWORD);
        }

        // Gets the phones currently used language
        String language = Locale.getDefault().getDisplayLanguage().toString();

        // Sets the font typeface that is obtained from the Assets > fonts -folders
        Typeface loginFont = Typeface.createFromAsset(getAssets(), "fonts/rainbow_bridge.ttf");

        final EditText username = (EditText) findViewById(R.id.inputUsername);
        username.setGravity(Gravity.CENTER);
        username.setHint(R.string.str_username);
        if (USER_USERNAME != null && !USER_USERNAME.equals("")) {
            username.setText(USER_USERNAME);
        }
        final EditText password = (EditText) findViewById(R.id.inputPassword);
        password.setGravity(Gravity.CENTER);
        password.setHint(R.string.str_password);
        if (USER_PASSWORD != null && !USER_PASSWORD.equals("")) {
            password.setText(USER_PASSWORD);
        }
        TextView texLogin = (TextView) findViewById(R.id.texLogin);
        texLogin.setText("SandBox");
        texLogin.setTypeface(loginFont);
        final CheckBox rememberMe = (CheckBox) findViewById(R.id.cbRememberMe);
        rememberMe.setText(R.string.str_remember_me);
        if (REMEMBER_ME) {
            rememberMe.setChecked(true);
        }
        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRememberMe(rememberMe);
                rememberLogin();
            }
        });

        final CheckBox autoLogin = (CheckBox) findViewById(R.id.cbAutoLogin);
        autoLogin.setText(R.string.str_auto_login);
        autoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAutoLogin(autoLogin);
            }
        });

        TextView texNotRegistered = (TextView) findViewById(R.id.texNotRegistered);
        texNotRegistered.setText(R.string.str_dont_have_an_account);

        TextView texRegister = (TextView)findViewById(R.id.texRegister);
        if (Build.VERSION.SDK_INT >= 24) {
            texRegister.setText(Html.fromHtml(getString(R.string.str_register_here), Html.FROM_HTML_MODE_LEGACY)); // for 24 api and more
        } else {
            texRegister.setText(Html.fromHtml(getString(R.string.str_register_here))); // or for older api
        }
        texRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, Register.class);
                startActivity(registerIntent);
            }
        });

        Button login = (Button) findViewById(R.id.btnLogin);
        login.setText(R.string.str_login);
        login.setGravity(Gravity.CENTER);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                un = String.valueOf(username.getText());
                pw = String.valueOf(password.getText());

                if (!un.equals("") || un != null) {
                    usernameNotNull = true;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.str_username_empty, Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                }

                if (!pw.equals("") || pw != null) {
                    passwordNotNull = true;
                } else {
                    Toast.makeText(getApplicationContext(), R.string.str_password_empty, Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                }

                if (usernameNotNull && passwordNotNull) {
                    requestLogin(un, pw);
                    if (!rememberMe.isChecked()) {
                        password.setText("");
                    }
                }
            }
        });

    }

    public void requestLogin(String username, String password) {
        User dbUser = realm.where(User.class).equalTo("username", username).findFirst();

        if (dbUser.getUsername().equals(username) && dbUser.getPassword().equals(password)) {
            // Exists
            hideSoftKeyboard(MainActivity.this);

            Intent profilePage = new Intent(MainActivity.this, ProfilePage.class);
            profilePage.putExtra(USER_USERNAME, dbUser.getUsername());
            if (REMEMBER_ME) {
                USER_USERNAME = dbUser.getUsername();
                USER_PASSWORD = dbUser.getPassword();
                rememberLogin();
            }
            startActivity(profilePage);
        } else {
            // Not exist
            Toast.makeText(getApplicationContext(), R.string.str_incorrect_credentials, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
        rememberLogin(); // Set SharedPreferences
    }

    private void checkRememberMe(CheckBox cb) {
        if (cb.isChecked()) {
            REMEMBER_ME = true;
        } else {
            REMEMBER_ME = false;
        }
    }

    private void setAutoLogin(CheckBox cb) {
        if (cb.isChecked()) {
            SharedPreferences autoLoginPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorAutoLoginPref = autoLoginPref.edit();
            editorAutoLoginPref.putBoolean("pref_auto_login", true);
            editorAutoLoginPref.apply();
            editorAutoLoginPref.commit();
        } else {
            SharedPreferences autoLoginPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorAutoLoginPref = autoLoginPref.edit();
            editorAutoLoginPref.putBoolean("pref_auto_login", false);
            editorAutoLoginPref.apply();
            editorAutoLoginPref.commit();
        }
    }

    private void rememberLogin() {

        if (REMEMBER_ME) {

            SharedPreferences rememberUsername = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorUsername = rememberUsername.edit();
            editorUsername.putString("pref_name", USER_USERNAME);
            editorUsername.commit();

            SharedPreferences rememberPassword = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorPassword = rememberPassword.edit();
            editorPassword.putString("pref_password", USER_PASSWORD);
            editorPassword.commit();

            SharedPreferences rememberCheckBox = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorCheckBox = rememberCheckBox.edit();
            editorCheckBox.putBoolean("pref_checkbox", REMEMBER_ME);
            editorCheckBox.apply();
            editorCheckBox.commit();

        } else {

            SharedPreferences rememberUsername = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorUsername = rememberUsername.edit();
            editorUsername.putString("pref_name", "");
            editorUsername.commit();

            SharedPreferences rememberPassword = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorPassword = rememberPassword.edit();
            editorPassword.putString("pref_password", "");
            editorPassword.commit();

            SharedPreferences rememberCheckBox = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editorCheckBox = rememberCheckBox.edit();
            editorCheckBox.putBoolean("pref_checkbox", REMEMBER_ME);
            editorCheckBox.apply();
            editorCheckBox.commit();
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) getSystemService(activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
