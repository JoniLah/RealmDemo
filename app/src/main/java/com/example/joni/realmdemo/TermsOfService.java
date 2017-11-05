package com.example.joni.realmdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Joni on 19.8.2017.
 */

public class TermsOfService extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsofservice);

        TextView terms = (TextView) findViewById(R.id.texTermsOfService);
        terms.setText(Html.fromHtml("<center><b>Terms and Conditions (\"Terms\")</b></center>" +
                "<br>" +
                "<br>" +
                "Last updated: 19.8.2017" +
                "<br>" +
                "<br>" +
                "Please read these Terms of Conditions (\"Terms\", \"Terms and Conditions\") carefully before using<br>" +
                "the RealmDemo application operated by Joni Lähdesmäki.<br>" +
                "<br>" +
                "Your access to use and use of the Service is conditioned on your acceptanse of and compliance with<br>" +
                "these Terms. These Terms apply to all visitors, users and others who access or use the Service.<br>" +
                "<br>" +
                "By accessing or using the Service you agree to be bound by these Terms. If you disagee with any<br>" +
                "part of the terms then you may not access the Service.<br>" +
                "<br>" +
                "You are willing to pay Joni Lähdesmäki, the owner of the Service, a 500€ per month to fund his wallet because he is a poor man.<br><br>" +
                "<i>We are not in responsible for the possible payments due to usage of our Service</i>.<br>" +
                ""));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(height * .6));

        Button btnOkTOS = (Button) findViewById(R.id.btnOkTOS);
        btnOkTOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the terms of service activity
            }
        });
    }
}
