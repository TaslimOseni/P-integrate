package apps.dabinu.com.p_integrate;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class SplashActivity extends AppCompatActivity {


    private EditText emailField, cardNumberField, expiryMonthField, expiryYearField, cvvField;
    private String your_api_key;
    private Button payButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //This part is very compulsory
        PaystackSdk.initialize(this.getApplicationContext());
        PaystackSdk.setPublicKey(your_api_key);



        //init layout
        emailField = findViewById(R.id.user_email_address);
        cardNumberField = findViewById(R.id.user_card_number);
        expiryMonthField = findViewById(R.id.user_expiry_month);
        expiryYearField = findViewById(R.id.user_expiry_year);
        cvvField = findViewById(R.id.user_cvv);
        payButton = findViewById(R.id.pay_button);



        //handle the pressing of the pay button
        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(validateUI() && isInternetAvailable()){
//                    performCharge1();
//                }
//                else{
//                    do your thing
//                }
            }
        });
    }



}