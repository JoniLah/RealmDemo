package com.example.joni.realmdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.joni.realmdemo.module.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

/**
 * Created by Joni on 9.9.2017.
 */

public class UpdatePassword extends Activity {

    private Realm realm;
    private String username;
    private int passwordAttempts = 0;
    private int passwordAttemptsMax = 5;
    private int timeoutInMinutes = 5;
    private int time = timeoutInMinutes * 60;
    private boolean timeout = false;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));

        layout = (LinearLayout) findViewById(R.id.rootLayoutUpdatePassword);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle.getString(ProfilePage.THIS_USER) == null) {
            username = ProfilePage.THIS_USER;
        } else {
            username = bundle.getString(ProfilePage.THIS_USER);
        }

        final User dbUser = realm.where(User.class).equalTo("username", username).findFirst();

        final MaterialEditText oldPassword = (MaterialEditText) findViewById(R.id.inputOldPassword);
        final Button confirmOldPassword = (Button) findViewById(R.id.btnConfirmOldPassword);
        confirmOldPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfOldPasswordMatches(oldPassword.getText().toString(), dbUser, oldPassword, confirmOldPassword);
            }
        });
    }

    private void checkIfOldPasswordMatches(String password, User user, MaterialEditText oldPw, Button oldPwBtn) {
        final User tempUser = realm.where(User.class).equalTo("username", user.getUsername()).findFirst();
        if (tempUser.getPassword().equals(password) && !timeout) {
            // If the passwords matched
            passwordAttempts = 0;

            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics());

            // Hide the components
            oldPw.setVisibility(View.GONE);
            oldPwBtn.setVisibility(View.GONE);

            // Create new ones
            final EditText newPassword = new EditText(this);
            newPassword.setHint("New password");
            newPassword.setGravity(Gravity.CENTER);
            newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPassword.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, getResources().getDisplayMetrics()));

            final EditText newPasswordConfirm = new EditText(this);
            newPasswordConfirm.setHint("Confirm password");
            newPasswordConfirm.setGravity(Gravity.CENTER);
            newPasswordConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

            Button confirm = new Button(this);
            confirm.setText("Confirm");
            confirm.setGravity(Gravity.CENTER);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updatePassword(newPassword.getText().toString(), newPasswordConfirm.getText().toString(), tempUser);
                }
            });

            Button cancel = new Button(this);
            cancel.setText("Cancel");
            cancel.setGravity(Gravity.CENTER);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            layout.addView(newPassword);
            layout.addView(newPasswordConfirm);
            layout.addView(confirm);
            layout.addView(cancel);



        } else {
            if (passwordAttempts <= passwordAttemptsMax - 1) {
                Toast.makeText(getApplicationContext(), "Incorrect password!", Toast.LENGTH_LONG).show();
                oldPw.setText("");
                passwordAttempts++;
                checkForTimeout(passwordAttempts, passwordAttemptsMax);
            } else {
                Toast.makeText(getApplicationContext(), "Too many incorrect attempts! Try again in " + getTimeout() + " minutes.", Toast.LENGTH_LONG).show();
            }
        }
        if (strongPassword(password)) {

        }
    }

    private boolean strongPassword(String password) {
        boolean hasNumber = false;
        boolean hasCapitalLetter = false;
        boolean hasSmallLetter = false;
        boolean isLongEnough = false;
        int strLength = 0;
        boolean hasSymbol = false;
        char thisCharacter;
        for (int i = 0; i < password.length(); i++) {
            thisCharacter = password.charAt(i);
            strLength++;
            if (Character.isLetter(thisCharacter)) {
                if (!hasSmallLetter) {
                    if (Character.isLowerCase(thisCharacter)) {
                        hasSmallLetter = true;
                    }
                }
                if (!hasCapitalLetter) {
                    if (Character.isUpperCase(thisCharacter)) {
                        hasCapitalLetter = true;
                    }
                }
            } else if (Character.isDigit(thisCharacter)) {
                if (!hasNumber) {
                    hasNumber = true;
                }
            } else if (!Character.isDigit(thisCharacter) && !Character.isLetter(thisCharacter) && !Character.isWhitespace(thisCharacter) && !hasSymbol) {
                hasSymbol = true;
            }
        }

        if (strLength >= 6) {
            isLongEnough = true;
        }

        return (hasNumber && hasCapitalLetter && hasSmallLetter && hasSymbol && isLongEnough);
    }

    private void checkForTimeout(int attempts, int maxAttempts) {
        if (attempts >= maxAttempts && !timeout) {
            timeout = true;
            setTimeout();
        }
    }

    private void setTimeout() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        if (time > 0) {
                            time = time - 1;
                        } else {
                            timeout = false;
                            passwordAttempts = 0;
                            Thread.interrupted();
                        }
                    }
                });

            }
        }, 0, 1000);
    }

    private int getTimeout() {
        int minutes = (time % 3600) / 60;
        return minutes;
    }

    private void updatePassword(String password, String confirmPassword, User user) {
        if (password.equals(confirmPassword) && password != "") {
            if (strongPassword(password)) {
                realm.beginTransaction();
                user.setPassword(password);
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Password updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Password must contain all of these:\nAt least one symbol\nAt least one small letter\n" +
                        "At least one capital letter\nAt least one number\nAt least 6 letters long",Toast.LENGTH_LONG).show();
            }
        }
    }
}
