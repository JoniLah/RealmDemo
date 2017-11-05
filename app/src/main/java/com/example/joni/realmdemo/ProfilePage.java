package com.example.joni.realmdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joni.realmdemo.module.User;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

/*import android.provider.Settings.Secure;
// Gets the unique Android ID
private String android_id = Secure.getString(getContext().getContentResolver(),
        Secure.ANDROID_ID);*/

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

import static android.provider.MediaStore.AUTHORITY;

/**
 * Created by Joni on 17.8.2017.
 */

public class ProfilePage extends AppCompatActivity {

    private Realm realm;
    private String username = "";
    private LinearLayout layout;
    private Boolean exit = false;
    private Pattern regexPattern;
    private Matcher regMatcher;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    protected static String THIS_USER = "";
    public static final String PREFS_NAME = "RememberMePrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Gets and sets the username from the previous Intent (Login screen)
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString(MainActivity.USER_USERNAME) == null) {
            username = MainActivity.USER_USERNAME;
        } else {
            username = bundle.getString(MainActivity.USER_USERNAME);
        }

        // Now get a handle to any View contained
        // within the main layout you are using
        final View profileView = findViewById(R.id.rootLayoutProfile);

        // Find the root view
        View root = profileView.getRootView();

        // Set the color
        int color = ContextCompat.getColor(getApplicationContext(), R.color.background);
        root.setBackgroundColor(color);

        // Initializes the Realm database
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        // Gets the correct user from the database
        final User dbUser = realm.where(User.class).equalTo("username", username).findFirst();

        layout = (LinearLayout) findViewById(R.id.rootLayoutProfile);
        TextView profileUserName = (TextView) findViewById(R.id.texUsername);
        profileUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size));
        profileUserName.setText(dbUser.getUsername());
        profileUserName.setGravity(Gravity.LEFT);

        final MaterialEditText displayEmail = (MaterialEditText) findViewById(R.id.inDisplayEmail);
        displayEmail.setEnabled(false);
        displayEmail.setText(String.valueOf(dbUser.getEmail()));
        displayEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    displayEmail.setEnabled(false);
                    String thisEmail = displayEmail.getText().toString();
                    if (validateEmailAddress(thisEmail).equals("Valid Email Address")) {
                        User userUpdateEmail = realm.where(User.class).equalTo("email", thisEmail).findFirst();
                        if (userUpdateEmail != null) {
                            if (thisEmail.equals(dbUser.getEmail().toString())) {
                                Toast.makeText(getApplicationContext(), "This email is already registered by you!", Toast.LENGTH_LONG).show();
                                displayEmail.setText(String.valueOf(dbUser.getEmail()));
                            } else {
                                Toast.makeText(getApplicationContext(), "This email is already registered by other account!", Toast.LENGTH_LONG).show();
                                displayEmail.setText(String.valueOf(dbUser.getEmail()));
                            }
                        } else {
                            // Send the changed variable to the database
                            realm.beginTransaction();
                            dbUser.setEmail(String.valueOf(displayEmail.getText()));
                            realm.commitTransaction();
                            Toast.makeText(getApplicationContext(), "Email successfully updated!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email address!", Toast.LENGTH_LONG).show();
                        displayEmail.setText(String.valueOf(dbUser.getEmail()));
                    }
                }
                return false;
            }
        });
        //displayEmail.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(this));

        final ImageButton editEmail = (ImageButton) findViewById(R.id.btnEditEmail);
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEmail.setEnabled(true);
                displayEmail.requestFocus();
                openKeyboard();
            }
        });

        final MaterialEditText displayPhonenumber = (MaterialEditText) findViewById(R.id.inDisplayPhonenumber);
        displayPhonenumber.setEnabled(false);
        displayPhonenumber.setText(String.valueOf(dbUser.getPhonenumber()));
        displayPhonenumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    displayPhonenumber.setEnabled(false);
                    String thisPhonenumber = displayPhonenumber.getText().toString();
                    if (validateMobileNumber(thisPhonenumber).equals("Valid Mobile Number")) {
                        User userUpdatePhonenumber = realm.where(User.class).equalTo("phonenumber", thisPhonenumber).findFirst();
                        if (userUpdatePhonenumber != null) {
                            if (thisPhonenumber.equals(dbUser.getPhonenumber().toString())) {
                                Toast.makeText(getApplicationContext(), "This phone number is already registered by you!", Toast.LENGTH_LONG).show();
                                displayPhonenumber.setText(String.valueOf(dbUser.getPhonenumber()));
                            } else {
                                Toast.makeText(getApplicationContext(), "This phone number is already registered by other account!", Toast.LENGTH_LONG).show();
                                displayPhonenumber.setText(String.valueOf(dbUser.getPhonenumber()));
                            }
                        } else {
                            // Send the changed variable to the database
                            realm.beginTransaction();
                            dbUser.setPhonenumber(String.valueOf(displayPhonenumber.getText()));
                            realm.commitTransaction();
                            Toast.makeText(getApplicationContext(), "Phone number successfully updated!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Phone number!", Toast.LENGTH_LONG).show();
                        displayPhonenumber.setText(String.valueOf(dbUser.getPhonenumber()));
                    }
                }
                return false;
            }
        });

        final ImageButton editPhonenumber = (ImageButton) findViewById(R.id.btnEditPhonenumber);
        editPhonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPhonenumber.setEnabled(true);
                displayPhonenumber.requestFocus();
                openKeyboard();
            }
        });

        TextView welcomeText = (TextView)findViewById(R.id.texWelcome);
        welcomeText.append(dbUser.getForename() + " " + dbUser.getLastname() + "!");
        welcomeText.setGravity(Gravity.CENTER);

        CircleImageView profilePicture = (CircleImageView) findViewById(R.id.imgProfilePicture);
        // Sets the image to the new position
        float profilePictureX = profilePicture.getX();
        float profilePictureY = profilePicture.getY();
        profilePicture.setX(profilePictureX + 16);
        profilePicture.setY(profilePictureY + 2);

        final ImageView addPhoto = (ImageView) findViewById(R.id.imgAddPhoto);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onPickImage(profileView);


                /*
                Intent imgFolder = new Intent();
                imgFolder.setAction(Intent.ACTION_VIEW);
                imgFolder.setDataAndType(Uri.fromFile(new File(android.os.Environment.DIRECTORY_DCIM)), "image/*");
                startActivity(imgFolder);
                /*
                if (checkCameraHardware(getApplicationContext())) {
                    // If the device has a camera
                    getCameraInstance();
                    addNewProfilePicture(dbUser, "PATH HERE");
                } else {
                    Toast.makeText(getApplicationContext(), "Camera not detected in this mobile!", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        TextView ageText = (TextView) findViewById(R.id.texProfileAge);
        ageText.setTypeface(null, Typeface.BOLD);
        final TextView ageDisplay = (TextView) findViewById(R.id.texDisplayAge) ;
        ageDisplay.setText(calculateAge(String.valueOf(dbUser.getBirthdate())));
        ageDisplay.setTypeface(null, Typeface.ITALIC);

        TextView dateofBirthText = (TextView) findViewById(R.id.texProfileDateofBirth);
        dateofBirthText.setTypeface(null, Typeface.BOLD);
        dateofBirthText.setGravity(Gravity.RIGHT);
        TextView dateofBirthDisplay = (TextView) findViewById(R.id.texDisplayDateofBirth);
        dateofBirthDisplay.setText(dbUser.getBirthdate());
        dateofBirthDisplay.setTypeface(null, Typeface.ITALIC);
        dateofBirthDisplay.setGravity(Gravity.RIGHT);

        TextView countryText = (TextView) findViewById(R.id.texProfileCountry);
        countryText.setTypeface(null, Typeface.BOLD);
        TextView countryDisplay = (TextView) findViewById(R.id.texDisplayCountry);
        CountryCheck check = new CountryCheck();
        countryDisplay.setText(check.returnCountry(String.valueOf(dbUser.getCountryCode())));
        countryDisplay.setTypeface(null, Typeface.ITALIC);

        TextView genderText = (TextView) findViewById(R.id.texProfileGender);
        genderText.setTypeface(null, Typeface.BOLD);
        genderText.setGravity(Gravity.RIGHT);
        TextView genderDisplay = (TextView) findViewById(R.id.texDisplayGender);
        genderDisplay.setText(String.valueOf(dbUser.getGender()));
        genderDisplay.setTypeface(null, Typeface.ITALIC);
        genderDisplay.setGravity(Gravity.RIGHT);

        final CheckBox subscribed = (CheckBox) findViewById(R.id.cbSubscribed);
        if (dbUser.isSubscription()) {
            subscribed.setChecked(true);
        } else {
            subscribed.setChecked(false);
        }
        subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subscribed.isChecked()) {
                    realm.beginTransaction();
                    dbUser.setSubscription(true);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();
                    dbUser.setSubscription(false);
                    realm.commitTransaction();
                }
            }
        });

        System.out.println(dbUser.toString());

        Button changePassword = (Button) findViewById(R.id.btnChangePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePasswordIntent = new Intent(ProfilePage.this, UpdatePassword.class);
                updatePasswordIntent.putExtra(THIS_USER, dbUser.getUsername());
                startActivity(updatePasswordIntent);
            }
        });

        final CountryCodePicker ccp = (CountryCodePicker) findViewById(R.id.ccpProfile);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                realm.beginTransaction();
                dbUser.setCountryCode(ccp.getSelectedCountryCode());
                realm.commitTransaction();
            }
        });
        ccp.setDefaultCountryUsingPhoneCode(Integer.parseInt(dbUser.getCountryCode()));
        ccp.resetToDefaultCountry();
        final String currentCountry = String.valueOf(ccp.getSelectedCountryCode());


        Button logOut = (Button) findViewById(R.id.btnLogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ProfilePage.this, MainActivity.class);
                Toast.makeText(getApplicationContext(), "Successfully logged out.", Toast.LENGTH_SHORT).show();
                startActivity(login);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("pref_auto_login");
                editor.commit();
            }
        });

        hideSoftKeyboard(ProfilePage.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    /*
    * Finishes the program if the Back Button is pressed (do not return to login screen)
    */
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            }, 500);
        }
    }

    public void openKeyboard() {
        // Finds the default input service
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(layout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
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

    public String calculateAge(String age) {
        Calendar today = Calendar.getInstance();
        Calendar birthdate = Calendar.getInstance();

        age.trim();

        String[] parts = age.split("\\."); // Splits the dots from the integers
        int day = Integer.parseInt(parts[0]), month = Integer.parseInt(parts[1]), year = Integer.parseInt(parts[2]);

        birthdate.set(year, month, day);

        int userAge = today.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthdate.get(Calendar.DAY_OF_YEAR)){
            userAge--;
        }

        Integer ageInt = new Integer(userAge);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // setEnabled here
            // This method checks if back key is pressed, it should disable the EditText
        }
        return super.onKeyDown(keyCode, event);
    }

    /** Check if this device has a camera */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    @SuppressWarnings("deprecation")
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            //c = Camera.open(); // attempt to get a Camera instance
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            //c = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            System.out.println("Couldn't open a camera (in use or does not exist).");
        }
        return c; // returns null if camera is unavailable
    }

    private void addNewProfilePicture(User user, String path) {

        //Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        // Checks if the picture is default picture
        boolean hasDefaultPicture = true;
        if (user.getProfilePicture() == null) {
            hasDefaultPicture = true;
        } else {
            hasDefaultPicture = false;
        }

        try {
            realm.beginTransaction();
            user.setProfilePicture(encodedImage);
            realm.commitTransaction();
            if (hasDefaultPicture) {
                Toast.makeText(getApplicationContext(), "Successfully created a new profile picture!", Toast.LENGTH_SHORT).show();
                updateProfilePicture(user, bm);
            } else {
                Toast.makeText(getApplicationContext(), "Successfully updated the profile picture!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }


    }

    private void updateProfilePicture(User user, Bitmap bm) {
        // Updates the profile picture into an image
        user.getProfilePicture(); // You need to encode the string as well
        CircleImageView profilePicture = (CircleImageView) findViewById(R.id.imgProfilePicture);
        profilePicture.setImageBitmap(bm);
    }

    public void onPickImage(View view) {
        //Intent i=new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(this, AUTHORITY, f));

        //i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) getSystemService(activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
