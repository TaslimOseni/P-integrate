package apps.dabinu.com.p_integrate;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodToken;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.ShippingAddressRequirements;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;



public class GooglePayActivity extends AppCompatActivity{


/*
    ---------------------------------------------------------------------------------

    STOP:

    Before you continue, make sure you add these dependencies to your build.gradle file:

    implementation 'com.google.android.gms:play-services-wallet:15.0.1'
    implementation 'com.android.support:support-v4:27.1.1'
    implementation 'com.android.support:appcompat-v7:27.1.1'

    ---------------------------------------------------------------------------------

    SUMMARY:

    When the user clicks the 'pay' button, the following sequence of activities happen:

    - the 'checkIfGooglePayIsAvailable()' method checks whether Google pay is enabled on the device.
    - if the above returns true, the 'loadPayment()' method loads the payment request.
    - In the 'onActivityResult()' from step 2 above, we can decipher whether the payment was successful or not.
    - Methods 'handlePaymentSuccess()' and 'handleError()' handle the success or failure from step 3 above.

    ----------------------------------------------------------------------------------
    COMPOSITES:

    This Java class contains two inner classes: UtilityMethods and AllUtilityConstants.

    These classes contain utility methods and values I used in many parts of this code.

*/





    private PaymentsClient mPaymentsClient;
    private static final int REQUEST_CODE = 991; // Randomly-picked code [You can use this].

    //    Object of one of our inner classes: PaymentUtil
    private UtilityMethods utilityMethods = new UtilityMethods();

    //The price goes here, make sure you include shipping and taxes
    String price = "2000";  //Randomly-picked value

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay);

//        This following line is very important, you must initialize the Payment client before anything else
        mPaymentsClient = utilityMethods.createPaymentsClient(this);


//        Initializing the Progress dialog
        progressDialog = new ProgressDialog(this);


//        Pay button:
        Button pay = findViewById(R.id.pay_button);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                progressDialog.setMessage("Checking whether Google pay is available on your device...");
                progressDialog.setCancelable(false);
                progressDialog.show();

//                check whether Google pay is available on the device
                checkIfGooglePayIsAvailable(v);
            }
        });

    }


    private void checkIfGooglePayIsAvailable(final View v){
//        Here, we're doing a simple check to see whether Google-pay is available on the device.

        utilityMethods.isReadyToPay(mPaymentsClient).addOnCompleteListener(new OnCompleteListener<Boolean>(){
                    public void onComplete(Task<Boolean> task){
                        try{
                            if(task.getResult(ApiException.class)){
//                                This means Google-pay is available
                                progressDialog.setMessage("Processing payment...");
                                loadPayment(v);
                            }
                            else{
//                                This means Google-pay is NOT available, provide UI to alert user
                                progressDialog.cancel();
                                progressDialog.dismiss();
                                new AlertDialog.Builder(getApplicationContext())
                                        .setMessage("Sorry, Google play is not available on this device.")
                                        .setCancelable(true)
                                        .show();
                                }
                            }

                        catch(ApiException exception){
//                             Decipher the error
                            Log.w("REASON_FOR_FAILURE", exception);

                            new AlertDialog.Builder(getApplicationContext())
                                    .setMessage("Sorry, An error occurred.. try again later")
                                    .setCancelable(true)
                                    .show();
                        }
                    }
                });
    }


    public void loadPayment(View view){

        TransactionInfo transactionInfo = utilityMethods.createATransaction(price);
        PaymentDataRequest paymentDataRequest = utilityMethods.createPaymentDataRequest(transactionInfo);
        Task<PaymentData> paymentData = mPaymentsClient.loadPaymentData(paymentDataRequest);

//        At this point, a UI may be shown prompting the user to choose a payment method,
//        the line below waits for this event and loads an onActivityResult() after.
        AutoResolveHelper.resolveTask(paymentData, this, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode == REQUEST_CODE){

            switch(resultCode){

                case Activity.RESULT_OK:
//                    Triggered if payment was successful
                    progressDialog.cancel();
                    progressDialog.dismiss();
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    handlePaymentSuccess(paymentData);
                    break;

                case Activity.RESULT_CANCELED:
//                    Triggered if payment was cancelled by user
                    progressDialog.cancel();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    break;

                case AutoResolveHelper.RESULT_ERROR:
//                    Triggered if there was an error while processing payment
                    progressDialog.cancel();
                    progressDialog.dismiss();
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    handleError(status.getStatusCode());
                    break;

            }
        }

    }


    private void handlePaymentSuccess(PaymentData paymentData){

        // You can use PaymentMethodToken to get all the details of the payment transaction
        // (e.g shipping address, billing name, etc)


        PaymentMethodToken token = paymentData.getPaymentMethodToken();


    }


    private void handleError(int statusCode){
//        Log your error
    }









    public class UtilityMethods{

        private final BigDecimal MICROS = new BigDecimal(1000000d);

        private UtilityMethods(){

        }


        public PaymentsClient createPaymentsClient(Activity activity) {
            Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                    .setEnvironment(new AllUtilityConstants().PAYMENTS_ENVIRONMENT)
                    .build();
            return Wallet.getPaymentsClient(activity, walletOptions);
        }



        public PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo) {
            PaymentMethodTokenizationParameters.Builder paramsBuilder =
                    PaymentMethodTokenizationParameters.newBuilder()
                            .setPaymentMethodTokenizationType(
                                    WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                            .addParameter("gateway", new AllUtilityConstants().GATEWAY_TOKENIZATION_NAME);
            for (Pair<String, String> param : new AllUtilityConstants().GATEWAY_TOKENIZATION_PARAMETERS) {
                paramsBuilder.addParameter(param.first, param.second);
            }

            return createPaymentDataRequest(transactionInfo, paramsBuilder.build());
        }




        public PaymentDataRequest createPaymentDataRequestDirect(TransactionInfo transactionInfo) {
            PaymentMethodTokenizationParameters params =
                    PaymentMethodTokenizationParameters.newBuilder()
                            .setPaymentMethodTokenizationType(
                                    WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_DIRECT)
                            .addParameter("publicKey", new AllUtilityConstants().DIRECT_TOKENIZATION_PUBLIC_KEY)
                            .build();

            return createPaymentDataRequest(transactionInfo, params);
        }



        private PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo, PaymentMethodTokenizationParameters params) {
            PaymentDataRequest request =
                    PaymentDataRequest.newBuilder()
                            .setPhoneNumberRequired(false)
                            .setEmailRequired(true)
                            .setShippingAddressRequired(true)
                            .setShippingAddressRequirements(
                                    ShippingAddressRequirements.newBuilder()
                                            .addAllowedCountryCodes(new AllUtilityConstants().SHIPPING_SUPPORTED_COUNTRIES)
                                            .build())
                            .setTransactionInfo(transactionInfo)
                            .addAllowedPaymentMethods(new AllUtilityConstants().SUPPORTED_METHODS)
                            .setCardRequirements(
                                    CardRequirements.newBuilder()
                                            .addAllowedCardNetworks(new AllUtilityConstants().SUPPORTED_NETWORKS)
                                            .setAllowPrepaidCards(true)
                                            .setBillingAddressRequired(true)
                                            .setBillingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                                            .build())
                            .setPaymentMethodTokenizationParameters(params)
                            .setUiRequired(true)
                            .build();

            return request;
        }


        public Task<Boolean> isReadyToPay(PaymentsClient client) {
            IsReadyToPayRequest.Builder request = IsReadyToPayRequest.newBuilder();
            for (Integer allowedMethod : new AllUtilityConstants().SUPPORTED_METHODS) {
                request.addAllowedPaymentMethod(allowedMethod);
            }
            return client.isReadyToPay(request.build());
        }



        public TransactionInfo createATransaction(String price) {
            return TransactionInfo.newBuilder()
                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                    .setTotalPrice(price)
                    .setCurrencyCode(new AllUtilityConstants().CURRENCY_CODE)
                    .build();
        }



        public String microsToString(long micros) {
            return new BigDecimal(micros).divide(MICROS).setScale(2, RoundingMode.HALF_EVEN).toString();
        }
    }



    public class AllUtilityConstants {

        public final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;


        public final List<Integer> SUPPORTED_NETWORKS = Arrays.asList(
                WalletConstants.CARD_NETWORK_AMEX,
                WalletConstants.CARD_NETWORK_DISCOVER,
                WalletConstants.CARD_NETWORK_VISA,
                WalletConstants.CARD_NETWORK_MASTERCARD
        );

        public final List<Integer> SUPPORTED_METHODS = Arrays.asList(
                WalletConstants.PAYMENT_METHOD_CARD,
                WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
        );


        public final String CURRENCY_CODE = "USD";


        public final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList(
                "US",
                "GB"
        );

        public final String GATEWAY_TOKENIZATION_NAME = "example";
        public final List<Pair<String, String>> GATEWAY_TOKENIZATION_PARAMETERS = Arrays.asList(
                Pair.create("gatewayMerchantId", "exampleGatewayMerchantId")
        );


        public final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

        private AllUtilityConstants(){

        }

    }


}