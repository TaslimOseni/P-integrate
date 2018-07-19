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
                if(validateUI() && isInternetAvailable()){
                    performCharge1();
                }
                else{
//                    do your thing
                }
            }
        });
    }


    private boolean validateUI(){
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String cardNumber = cardNumberField.getText().toString();
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberField.setError("Required.");
            valid = false;
        } else {
            cardNumberField.setError(null);
        }


        String expiryMonth = expiryMonthField.getText().toString();
        if (TextUtils.isEmpty(expiryMonth)) {
            expiryMonthField.setError("Required.");
            valid = false;
        } else {
            expiryMonthField.setError(null);
        }

        String expiryYear = expiryYearField.getText().toString();
        if (TextUtils.isEmpty(expiryYear)) {
            expiryYearField.setError("Required.");
            valid = false;
        } else {
            expiryYearField.setError(null);
        }

        String cvv = cvvField.getText().toString();
        if (TextUtils.isEmpty(cvv)) {
            cvvField.setError("Required.");
            valid = false;
        } else {
            cvvField.setError(null);
        }

        return valid;
    }


    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void performCharge1(){

        String email = emailField.getText().toString().trim();
        String cardNumber = cardNumberField.getText().toString().trim();
        int expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
        int expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
        String cvv = cvvField.getText().toString().trim();


        try {
            Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

            if (!(card.isValid())) {
                Toast.makeText(getApplicationContext(), "Card is not Valid", Toast.LENGTH_LONG).show();
                return;
            }


            Charge charge = new Charge();

            charge.setCard(card);
            charge.setEmail(email);

            //The value below is the amount that'd be charged..
            charge.setAmount(1000);

            performCharge2(charge);
        }


        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Failed...", Toast.LENGTH_LONG).show();
            return;
        }




    }


    public void performCharge2(Charge charge){
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback(){
            @Override
            public void onSuccess(Transaction transaction){
                Toast.makeText(getApplicationContext(), "Payment successful!!!", Toast.LENGTH_LONG).show();
                //do your remaining stuff
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                //set a progress bar or something
            }

            @Override
            public void onError(Throwable error, Transaction transaction){
                new AlertDialog.Builder(getApplicationContext())
                        .setMessage("Payment unsuccessful, please try again later")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //try again
                            }
                        })
                        .show();

            }
        });
    }

}