package com.example.joni.realmdemo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joni.realmdemo.module.User;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Joni on 15.8.2017.
 */

public class Register extends AppCompatActivity {

    private Realm realm;
    private Pattern regexPattern;
    private Matcher regMatcher;
    private Calendar bdayCal;
    private MaterialEditText birthday;
    private LinearLayout rootLayoutRegister;
    private boolean keyboardShowing;
    private boolean subscribed = false;
    private int allowedAge = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rootLayoutRegister = (LinearLayout) findViewById(R.id.rootLayoutRegister);
        rootLayoutRegister.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                rootLayoutRegister.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootLayoutRegister.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    keyboardShowing = true;
                }
                else {
                    // keyboard is closed
                    keyboardShowing = false;
                }
            }
        });

        // Now get a handle to any View contained
        // within the main layout you are using
        View registerView = findViewById(R.id.rootLayoutRegister);

        // Find the root view
        View root = registerView.getRootView();

        // Set the color
        int color = ContextCompat.getColor(getApplicationContext(), R.color.background);
        root.setBackgroundColor(color);

        Realm.init(this);
        try {
            realm = Realm.getDefaultInstance();
        } catch (Exception e) {
            // Get a Realm instance for this thread
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
        }

        final TextView tos = (TextView) findViewById(R.id.texTOS);
        tos.setText(Html.fromHtml(getString(R.string.str_tos)));
        tos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, TermsOfService.class));
            }
        });



        final MaterialEditText username = (MaterialEditText) findViewById(R.id.inputRegisterUsername);
        username.setHint(R.string.str_username);
        final MaterialEditText password = (MaterialEditText) findViewById(R.id.inputRegisterPassword);
        password.setHint(R.string.str_password);
        final MaterialEditText email = (MaterialEditText) findViewById(R.id.inputEmail);
        email.setHint(R.string.str_email);
        final MaterialEditText passwordConfirm = (MaterialEditText) findViewById(R.id.inputConfirmPassword);
        passwordConfirm.setHint(R.string.str_confirm_password);
        final MaterialEditText emailConfirm = (MaterialEditText) findViewById(R.id.inputConfirmEmail);
        emailConfirm.setHint(R.string.str_confirm_email);
        final MaterialEditText forename = (MaterialEditText) findViewById(R.id.inputRegisterForename);
        forename.setHint(R.string.str_forename);
        final MaterialEditText lastname = (MaterialEditText) findViewById(R.id.inputRegisterLastname);
        lastname.setHint(R.string.str_lastname);
        final MaterialEditText phoneNumber = (MaterialEditText) findViewById(R.id.inputPhoneNumber);
        phoneNumber.setHint(R.string.str_phone_number);

        birthday = (MaterialEditText) findViewById(R.id.inputDate);
        birthday.setHint(R.string.str_date_of_birth);
        bdayCal = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                bdayCal.set(Calendar.YEAR, year);
                bdayCal.set(Calendar.MONTH, monthOfYear);
                bdayCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dt = new DatePickerDialog(Register.this, date, bdayCal
                        .get(Calendar.YEAR), bdayCal.get(Calendar.MONTH),
                        bdayCal.get(Calendar.DAY_OF_MONTH));
                dt.getDatePicker().setMaxDate(new Date().getTime()); // Disallow users to set birthdate past this day
                dt.show();
            }
        });
        DatePicker dateP = new DatePicker(Register.this);
        dateP.setMaxDate(new Date().getTime());

        final CheckBox acceptTOS = (CheckBox) findViewById(R.id.cbTerms);
        acceptTOS.setText(R.string.str_accept_tos);
        acceptTOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardShowing) {
                    hideSoftKeyboard(Register.this);
                }
            }
        });

        final CheckBox subscribe = (CheckBox) findViewById(R.id.cbSubscribe);
        subscribe.setText(R.string.str_offer_sandbox);
        subscribed = (subscribe.isChecked());

        // Country Code Picker for the mobile phone number
        final CountryCodePicker ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingNameCode("FI");
        ccp.resetToDefaultCountry();


        final Spinner gender = (Spinner) findViewById(R.id.spinGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);

        Button register = (Button) findViewById(R.id.btnRegister);
        register.setText(R.string.str_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean passwordMatch = (String.valueOf(password.getText()).equals(String.valueOf(passwordConfirm.getText())));
                boolean emailMatch = (String.valueOf(email.getText()).equals(String.valueOf(emailConfirm.getText())));
                boolean acceptedTOS = acceptTOS.isChecked();
                boolean correctForename = (validateName(String.valueOf(forename.getText())));
                boolean correctLastname = (validateName(String.valueOf(lastname.getText())));
                String correctEmail = validateEmailAddress(String.valueOf(email.getText()));
                String correctPhonenumber = validateMobileNumber(String.valueOf(phoneNumber.getText()));
                if (passwordMatch && emailMatch && acceptedTOS && correctForename && correctLastname) {
                    if (!String.valueOf(username.getText()).equals("") &&
                            !String.valueOf(password.getText()).equals("") &&
                            !String.valueOf(email.getText()).equals("") &&
                            !String.valueOf(phoneNumber.getText()).equals("") &&
                            !String.valueOf(forename.getText()).equals("") &&
                            !String.valueOf(lastname.getText()).equals("") &&
                            !String.valueOf(birthday.getText()).equals("") &&
                            correctEmail.equals("Valid Email Address") &&
                            correctPhonenumber.equals("Valid Mobile Number") &&
                            Integer.parseInt(getAge(String.valueOf(birthday.getText()))) >= allowedAge) {
                        String ifTaken = checkIfTaken(String.valueOf(username.getText()), String.valueOf(email.getText()), String.valueOf(phoneNumber.getText()));
                        if (ifTaken.equals("")) {
                            if (strongPassword(String.valueOf(password.getText()))) {
                                try {
                                    // Calculate the amount of users and set the ID for the new user
                                    int userAmount = 0;
                                    RealmResults<User> userID = realm.where(User.class).findAll();
                                    for (User users : userID) {
                                        System.out.println("ID " + users);
                                        userAmount += 1;
                                    }
                                    putIntoDatabase(userAmount, String.valueOf(username.getText()), String.valueOf(password.getText()),
                                            String.valueOf(email.getText()), String.valueOf(phoneNumber.getText()),
                                            String.valueOf(ccp.getSelectedCountryCode()), String.valueOf(forename.getText()),String.valueOf(lastname.getText()),
                                            String.valueOf(birthday.getText()), gender.getSelectedItem().toString(), subscribed);
                                } catch (Exception exc) {
                                    exc.printStackTrace();
                                } finally {
                                    query();
                                    realm.close();
                                    Intent main = new Intent(Register.this, MainActivity.class);
                                    startActivity(main);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.str_password_must_contain ,Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), ifTaken, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String exceptionString = "";
                        if (String.valueOf(username.getText()).equals("")) {
                            exceptionString += R.string.str_username_empty;
                        }
                        if (String.valueOf(birthday.getText()).equals("")) {
                            exceptionString += R.string.str_date_of_birth_empty;
                        }
                        if (String.valueOf(password.getText()).equals("")) {
                            exceptionString += R.string.str_password_empty;
                        }
                        if (String.valueOf(phoneNumber.getText()).equals("")) {
                            exceptionString += R.string.str_phone_number_empty;
                        }
                        if (String.valueOf(email.getText()).equals("")) {
                            exceptionString += R.string.str_email_empty;
                        }
                        if (String.valueOf(forename.getText()).equals("")) {
                            exceptionString += R.string.str_forename_empty;
                        }
                        if (String.valueOf(lastname.getText()).equals("")) {
                            exceptionString += R.string.str_lastname_empty;
                        }
                        if (!correctEmail.equals("Valid Email Address")) {
                            exceptionString += R.string.str_email_invalid;
                        }
                        if (!correctPhonenumber.equals("Valid Mobile Number")) {
                            exceptionString += R.string.str_phone_number_invalid;
                        }
                        if (Integer.parseInt(getAge(String.valueOf(birthday.getText()))) < allowedAge) {
                            exceptionString += R.string.str_under_age;
                        }
                        // Shows the errors
                        Toast.makeText(getApplicationContext(), exceptionString, Toast.LENGTH_LONG).show();
                    }
                } else {
                    String errorTrace = "";
                    if (!passwordMatch) {
                        errorTrace += R.string.str_passwords_dont_match;
                    }
                    if (!emailMatch) {
                        errorTrace += R.string.str_emails_dont_match;
                    }
                    if (!acceptedTOS) {
                        errorTrace += R.string.str_not_accepted_tos;
                    }
                    if (!correctForename) {
                        errorTrace += R.string.str_forename_only_letters;
                    }
                    if (!correctLastname) {
                        errorTrace += R.string.str_lastname_only_letters;
                    }

                    Toast.makeText(getApplicationContext(), errorTrace, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    /*
    * Checks whether username, email or phone number are already taken
    */
    public String checkIfTaken(String username, String email, String phonenumber) {
        String returnMessage = "";

        // Find for duplicates
        User findTakenUsernames = realm.where(User.class).equalTo("username", username).findFirst();
        User findTakenEmails = realm.where(User.class).equalTo("email", email).findFirst();
        User findTakenPhonenumbers = realm.where(User.class).equalTo("phonenumber", phonenumber).findFirst();

        // Checks if they've been taken already, return false if yes
        if (findTakenUsernames != null) returnMessage += R.string.str_username_taken;
        if (findTakenEmails != null) returnMessage += R.string.str_email_taken;
        if (findTakenPhonenumbers != null) returnMessage += R.string.str_phone_number_taken;

        return returnMessage;
    }

    public void putIntoDatabase(final int id, final String username, final String password, final String email,
                                final String phonenumber, final String countryCode,
                                final String forename, final String lastname, final String birthdate,
                                final String gender, final boolean subscription) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                User user = bgRealm.createObject(User.class);
                user.setId(id);
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setPhonenumber(phonenumber);
                user.setForename(forename);
                user.setLastname(lastname);
                user.setGender(gender);
                user.setCountryCode(countryCode);
                user.setSubscription(subscription);
                user.setBirthdate(birthdate);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Toast success = Toast.makeText(getApplicationContext(), R.string.str_account_created, Toast.LENGTH_LONG);
                success.show();
                // Send the subscription email to the new registered email address
                if (subscribed) {
                    // Send email method, deleted for now
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                String errorMsg = error.getMessage();
                Toast failure = Toast.makeText(getApplicationContext(), R.string.str_account_failed + errorMsg, Toast.LENGTH_LONG);
                failure.show();
            }
        });
    }

    private void query() {
        // Build the query looking at all users:
        RealmResults<User> queryResults = realm.where(User.class).findAll();

        // Execute the query:
        String output = "";
        for (User users : queryResults) {
            output += queryResults.toString();
        }
        System.out.println(output);
    }

    public String validateEmailAddress(String emailAddress) {

        regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        regMatcher = regexPattern.matcher(emailAddress);
        if (regMatcher.matches()) {
            return "Valid Email Address";
        } else {
            return "Invalid Email Address";
        }
    }

    public String validateMobileNumber(String mobileNumber) {
        regexPattern = Pattern.compile("^[0-9]{9,11}$");
        regMatcher = regexPattern.matcher(mobileNumber);

        if (regMatcher.matches()) {
            return "Valid Mobile Number";
        } else {
            return "Invalid Mobile Number";
        }
    }

    public boolean validateName(String name) {
        int charCount = 0;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";

        if (name.length() == 0) return false;
        for (int i = 0; i < name.length() ;i++) {
            for (int j = 0; j < alphabet.length(); j++) {
                if (name.substring(i, i + 1).equals(alphabet.substring(j, j + 1)) || name.substring(i, i + 1).equals(alphabet.substring(j, j + 1).toLowerCase())) {
                    charCount++;
                }
            }
            if(charCount != (i + 1)) return false;
        }
        return true;
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) getSystemService(activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void updateLabel() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        birthday.setText(sdf.format(bdayCal.getTime()));
    }


    private String getAge(String age) {
        Calendar today = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();

        age.trim();

        String[] parts = age.split("\\.");
        int day = Integer.parseInt(parts[0]), month = Integer.parseInt(parts[1]), year = Integer.parseInt(parts[2]);

        birthdate.set(year, month, day);

        int userAge = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthdate.get(Calendar.DAY_OF_YEAR)){
            userAge--;
        }

        Integer ageInt = new Integer(userAge);
        String ageS = ageInt.toString();

        return ageS;
        /* if( (mMonthOfYear == month && day < mDayOfMonth) || ( month < mMonthOfYear) ) //mMonthOfYear is a parameter of the method
            {
                mAge--;
            }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
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
}