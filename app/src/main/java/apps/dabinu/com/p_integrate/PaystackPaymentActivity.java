package apps.dabinu.com.p_integrate;


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



public class PaystackPaymentActivity extends AppCompatActivity{


/*    Before you continue, make sure you have done the following:

1.  Create an account on http://paystack.com, complete the registration, create a new project and generate your public key.
2.  Add the line: ~implementation 'co.paystack.android:paystack:3.0.10'~ to your app-level gradle dependency.
3.  Add the line: ~<uses-permission android:name="android.permission.INTERNET" />~ to your manifest.

*/


    private String your_api_key;    //the API key generated from step 1 above.
    private EditText emailField, cardNumberField, expiryMonthField, expiryYearField, cvvField;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paystack_payment);



        //This following line is very important, you must initialize the Paystack SDK before using any Paystack class or interface.
        PaystackSdk.initialize(this.getApplicationContext());



        //Also, set your public key (from step 1 above) like this:
        PaystackSdk.setPublicKey(your_api_key);



        //initialize views
        emailField = findViewById(R.id.user_email_address);
        cardNumberField = findViewById(R.id.user_card_number);
        expiryMonthField = findViewById(R.id.user_expiry_month);
        expiryYearField = findViewById(R.id.user_expiry_year);
        cvvField = findViewById(R.id.user_cvv);


        Button payButton = findViewById(R.id.pay_button);


        //handle the onClick when the 'pay-button' is pressed
        payButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //check whether user filled all the fields and there is internet connection

                if(isUserEntryValid() && isInternetAvailable()){
                    prepareToChargeUser(2000 * 100);  //In this case, we're charging the user #2000
                }

                else{
//                    Notify the user to check his internet connection or validate his entries
                }
            }
        });
    }


    private boolean isUserEntryValid(){
        boolean isValid = true;

        //check whether the email field is empty
        if(TextUtils.isEmpty(emailField.getText().toString())){
            emailField.setError("This field is required");
            isValid = false;
        }

        //check whether the card-number field is empty
        else if(TextUtils.isEmpty(cardNumberField.getText().toString())){
            cardNumberField.setError("This field is required");
            isValid = false;
        }

        //check whether the expiry-month field is empty
        else if(TextUtils.isEmpty(expiryMonthField.getText().toString())){
            expiryMonthField.setError("This field is required");
            isValid = false;
        }

        //check whether the expiry-year field is empty
        else if(TextUtils.isEmpty(expiryYearField.getText().toString())){
            expiryYearField.setError("This field is required");
            isValid = false;
        }

        //check whether the card-cvv field is empty
        else if(TextUtils.isEmpty(cvvField.getText().toString())){
            cvvField.setError("This field is required");
            isValid = false;
        }


        return isValid;
    }


    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void prepareToChargeUser(int amountToBeCharged){

        //get all the EditText entries as String and int values for convenience
        String email = emailField.getText().toString().trim();
        String cardNumber = cardNumberField.getText().toString().trim();
        int expiryMonth = Integer.parseInt(expiryMonthField.getText().toString().trim());
        int expiryYear = Integer.parseInt(expiryYearField.getText().toString().trim());
        String cvv = cvvField.getText().toString().trim();


            /*
            For testing purposes, Paystack provides a test card.. If you're still in the testing stage of your project,
            consider using the test cards, they are awesome!

            String cardNumber = "4084084084084081";
            int expiryMonth = 11; //any month in the future
            int expiryYear = 18; // any year in the future. '2018' would work also!
            String cvv = "408";  // cvv of the test card

            */


        //Create a new Card object based on the card details
        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

        //check whether the card is valid like this:
        if (!(card.isValid())) {
            Toast.makeText(getApplicationContext(), "Card is not Valid", Toast.LENGTH_LONG).show();
            return;
        }


        //Create a new Charge object
        Charge charge = new Charge();

        charge.setCard(card);
        charge.setEmail(email);
        charge.setAmount(amountToBeCharged);

        //proceed to charge the user
        chargeTheUser(charge);


    }


    public void chargeTheUser(Charge charge){

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback(){
            @Override
            public void beforeValidate(Transaction transaction){
                //You can set a progress bar or progress dialog so that the user knows something is going on
            }


            @Override
            public void onSuccess(Transaction transaction){
                Toast.makeText(getApplicationContext(), "Payment successful!!!", Toast.LENGTH_LONG).show();
                //Payment successful; proceed to give your user the product/service they paid for.
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