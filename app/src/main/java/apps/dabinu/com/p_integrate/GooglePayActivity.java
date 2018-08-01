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
    Before you continue, make sure you add these dependecies to your build.gradle file:

    implementation 'com.google.android.gms:play-services-wallet:15.0.1'
    implementation 'com.android.support:support-v4:27.1.1'
    implementation 'com.android.support:appcompat-v7:27.1.1'
*/

    private PaymentsClient mPaymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991; // Randomly-picked code [You can use this].

//    Let's create an object for our inner class PaymentUtil
      PaymentsUtil paymentsUtil = new PaymentsUtil();

//    This is a progress dialog
    ProgressDialog progressDialog;

    //Price, make sure you include shipping and taxes
    String price = "2000";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay);

        //Initialize Payment client
        mPaymentsClient = new PaymentsUtil().createPaymentsClient(this);

        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        mPaymentsClient = paymentsUtil.createPaymentsClient(this);

        progressDialog = new ProgressDialog(this);

        Button pay = findViewById(R.id.pay_button);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                progressDialog.setMessage("Checking whether Google pay is available on your device...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                checkWhetherGooglePayIsAvailable(v);
            }
        });

    }


    private void checkWhetherGooglePayIsAvailable(final View v){
        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
        // OnCompleteListener to be triggered when the result of the call is known.
        paymentsUtil.isReadyToPay(mPaymentsClient).addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try{
                            // If isReadyToPay returned true, show the button and hide the "checking" text. Otherwise,
                            // notify the user that Pay with Google is not available.
                            // Please adjust to fit in with your current user flow. You are not required to explicitly
                            // let the user know if isReadyToPay returns false.

                            if(task.getResult(ApiException.class)){
                                progressDialog.setMessage("Processing payment...");
                                requestPayment(v);
                            }
                            else{
                                progressDialog.cancel();
                                progressDialog.dismiss();
                                new AlertDialog.Builder(getApplicationContext())
                                        .setMessage("Sorry, Google play is not available on this device.")
                                        .setCancelable(true)
                                        .show();
                            }
                        } catch (ApiException exception){
                            // Process error
                            Log.w("isReadyToPay failed", exception);

                            new AlertDialog.Builder(getApplicationContext())
                                    .setMessage("Sorry, An error occurred.")
                                    .setCancelable(true)
                                    .show();
                        }
                    }
                });
    }


    // This method is called when the Pay with Google button is clicked.
    public void requestPayment(View view){

        TransactionInfo transaction = paymentsUtil.createTransaction(price);
        PaymentDataRequest request = paymentsUtil.createPaymentDataRequest(transaction);
        Task<PaymentData> futurePaymentData = mPaymentsClient.loadPaymentData(request);

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        AutoResolveHelper.resolveTask(futurePaymentData, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case Activity.RESULT_CANCELED:
                        // Nothing to here normally - the user simply cancelled without selecting a
                        // payment method.
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status.getStatusCode());
                        break;
                }


                break;
        }
    }


    private void handlePaymentSuccess(PaymentData paymentData){
        // PaymentMethodToken contains the payment information, as well as any additional
        // requested information, such as billing and shipping address.
        //
        // Refer to your processor's documentation on how to proceed from here.
        PaymentMethodToken token = paymentData.getPaymentMethodToken();

        // getPaymentMethodToken will only return null if PaymentMethodTokenizationParameters was
        // not set in the PaymentRequest.
        if (token != null) {
            // If the gateway is set to example, no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (token.getToken().equals("examplePaymentMethodToken")){
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Gateway name set to \"example\" - please modify " +
                                "Constants.java and replace it with your own gateway.")
                        .setPositiveButton("OK", null)
                        .create();
                alertDialog.show();
            }

            String billingName = paymentData.getCardInfo().getBillingAddress().getName();
            Toast.makeText(this, "Successfully received payment data!", Toast.LENGTH_LONG).show();

            // Use token.getToken() to get the token string.
            Log.d("PaymentData", "PaymentMethodToken received");
        }
    }


    private void handleError(int statusCode){
        // At this stage, the user has already seen a popup informing them an error occurred.
        // Normally, only logging is required.
        // statusCode will hold the value of any constant from CommonStatusCode or one of the
        // WalletConstants.ERROR_CODE_* constants.
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode));
    }






    /**
     * Contains helper static methods for dealing with the Payments API.
     * <p>
     * Many of the parameters used in the code are optional and are set here merely to call out their
     * existence. Please consult the documentation to learn more and feel free to remove ones not
     * relevant to your implementation.
     */
    public class PaymentsUtil{
        private final BigDecimal MICROS = new BigDecimal(1000000d);

        private PaymentsUtil() {
        }

        /**
         * Creates an instance of {@link PaymentsClient} for use in an {@link Activity} using the
         * environment and theme set in {@link Constants}.
         *
         * @param activity is the caller's activity.
         */
        public PaymentsClient createPaymentsClient(Activity activity) {
            Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                    .setEnvironment(new Constants().PAYMENTS_ENVIRONMENT)
                    .build();
            return Wallet.getPaymentsClient(activity, walletOptions);
        }

        /**
         * Builds {@link PaymentDataRequest} to be consumed by {@link PaymentsClient#loadPaymentData}.
         *
         * @param transactionInfo contains the price for this transaction.
         */
        public PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo) {
            PaymentMethodTokenizationParameters.Builder paramsBuilder =
                    PaymentMethodTokenizationParameters.newBuilder()
                            .setPaymentMethodTokenizationType(
                                    WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                            .addParameter("gateway", new Constants().GATEWAY_TOKENIZATION_NAME);
            for (Pair<String, String> param : new Constants().GATEWAY_TOKENIZATION_PARAMETERS) {
                paramsBuilder.addParameter(param.first, param.second);
            }

            return createPaymentDataRequest(transactionInfo, paramsBuilder.build());
        }

        /**
         * Builds {@link PaymentDataRequest} for use with DIRECT integration to be consumed by
         * {@link PaymentsClient#loadPaymentData}.
         * <p>
         * Please refer to the documentation for more information about DIRECT integration. The type of
         * integration you use depends on your payment processor.
         *
         * @param transactionInfo contains the price for this transaction.
         */
        public PaymentDataRequest createPaymentDataRequestDirect(TransactionInfo transactionInfo) {
            PaymentMethodTokenizationParameters params =
                    PaymentMethodTokenizationParameters.newBuilder()
                            .setPaymentMethodTokenizationType(
                                    WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_DIRECT)

                            // Omitting the publicKey will result in a request for unencrypted data.
                            // Please refer to the documentation for more information on unencrypted
                            // requests.
                            .addParameter("publicKey", new Constants().DIRECT_TOKENIZATION_PUBLIC_KEY)
                            .build();

            return createPaymentDataRequest(transactionInfo, params);
        }

        private PaymentDataRequest createPaymentDataRequest(TransactionInfo transactionInfo, PaymentMethodTokenizationParameters params) {
            PaymentDataRequest request =
                    PaymentDataRequest.newBuilder()
                            .setPhoneNumberRequired(false)
                            .setEmailRequired(true)
                            .setShippingAddressRequired(true)

                            // Omitting ShippingAddressRequirements all together means all countries are
                            // supported.
                            .setShippingAddressRequirements(
                                    ShippingAddressRequirements.newBuilder()
                                            .addAllowedCountryCodes(new Constants().SHIPPING_SUPPORTED_COUNTRIES)
                                            .build())

                            .setTransactionInfo(transactionInfo)
                            .addAllowedPaymentMethods(new Constants().SUPPORTED_METHODS)
                            .setCardRequirements(
                                    CardRequirements.newBuilder()
                                            .addAllowedCardNetworks(new Constants().SUPPORTED_NETWORKS)
                                            .setAllowPrepaidCards(true)
                                            .setBillingAddressRequired(true)

                                            // Omitting this parameter will result in the API returning
                                            // only a "minimal" billing address (post code only).
                                            .setBillingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                                            .build())
                            .setPaymentMethodTokenizationParameters(params)

                            // If the UI is not required, a returning user will not be asked to select
                            // a card. Instead, the card they previously used will be returned
                            // automatically (if still available).
                            // Prior whitelisting is required to use this feature.
                            .setUiRequired(true)
                            .build();

            return request;
        }

        /**
         * Determines if the user is eligible to Pay with Google by calling
         * {@link PaymentsClient#isReadyToPay}. The nature of this check depends on the methods set in
         * {@link Constants#SUPPORTED_METHODS}.
         * <p>
         * If {@link WalletConstants#PAYMENT_METHOD_CARD} is specified among supported methods, this
         * function will return true even if the user has no cards stored. Please refer to the
         * documentation for more information on how the check is performed.
         *
         * @param client used to send the request.
         */
        public Task<Boolean> isReadyToPay(PaymentsClient client) {
            IsReadyToPayRequest.Builder request = IsReadyToPayRequest.newBuilder();
            for (Integer allowedMethod : new Constants().SUPPORTED_METHODS) {
                request.addAllowedPaymentMethod(allowedMethod);
            }
            return client.isReadyToPay(request.build());
        }

        /**
         * Builds {@link TransactionInfo} for use with {@link PaymentsUtil#createPaymentDataRequest}.
         * <p>
         * The price is not displayed to the user and must be in the following format: "12.34".
         * {@link PaymentsUtil#microsToString} can be used to format the string.
         *
         * @param price total of the transaction.
         */
        public TransactionInfo createTransaction(String price) {
            return TransactionInfo.newBuilder()
                    .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                    .setTotalPrice(price)
                    .setCurrencyCode(new Constants().CURRENCY_CODE)
                    .build();
        }

        /**
         * Converts micros to a string format accepted by {@link PaymentsUtil#createTransaction}.
         *
         * @param micros value of the price.
         */
        public String microsToString(long micros) {
            return new BigDecimal(micros).divide(MICROS).setScale(2, RoundingMode.HALF_EVEN).toString();
        }
    }



    public class Constants{
        // This file contains several constants you must edit before proceeding.
        //
        // Please take a look at PaymentsUtil.java to see where the constants are used and to
        // potentially remove ones not relevant to your integration.
        // Required changes:
        // 1.  Update SUPPORTED_NETWORKS and SUPPORTED_METHODS if required (consult your processor if
        //     unsure).
        // 2.  Update CURRENCY_CODE to the currency you use.
        // 3.  Update SHIPPING_SUPPORTED_COUNTRIES to list the countries where you currently ship. If
        //     this is not applicable to your app, remove the relevant bits from PaymentsUtil.java.
        // 4.  If you're integrating with your processor / gateway directly, update
        //     GATEWAY_TOKENIZATION_NAME and GATEWAY_TOKENIZATION_PARAMETERS per the instructions they
        //     provided. You don't need to update DIRECT_TOKENIZATION_PUBLIC_KEY.
        // 5.  If you're using direct integration, please consult the documentation to learn about
        //     next steps.

        // Changing this to ENVIRONMENT_PRODUCTION will make the API return real card information.
        // Please refer to the documentation to read about the required steps needed to enable
        // ENVIRONMENT_PRODUCTION.
        public final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

        // The allowed networks to be requested from the API. If the user has cards from networks not
        // specified here in their account, these will not be offered for them to choose in the popup.
        public final List<Integer> SUPPORTED_NETWORKS = Arrays.asList(
                WalletConstants.CARD_NETWORK_AMEX,
                WalletConstants.CARD_NETWORK_DISCOVER,
                WalletConstants.CARD_NETWORK_VISA,
                WalletConstants.CARD_NETWORK_MASTERCARD
        );

        public final List<Integer> SUPPORTED_METHODS = Arrays.asList(
                // PAYMENT_METHOD_CARD returns to any card the user has stored in their Google Account.
                WalletConstants.PAYMENT_METHOD_CARD,

                // PAYMENT_METHOD_TOKENIZED_CARD refers to EMV tokenized credentials stored in the
                // Google Pay app, assuming it's installed.
                // Please keep in mind tokenized cards may exist in the Google Pay app without being
                // added to the user's Google Account.
                WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD
        );

        // Required by the API, but not visible to the user.
        public final String CURRENCY_CODE = "USD";

        // Supported countries for shipping (use ISO 3166-1 alpha-2 country codes).
        // Relevant only when requesting a shipping address.
        public final List<String> SHIPPING_SUPPORTED_COUNTRIES = Arrays.asList(
                "US",
                "GB"
        );

        // The name of your payment processor / gateway. Please refer to their documentation for
        // more information.
        public final String GATEWAY_TOKENIZATION_NAME = "example";

        // Custom parameters required by the processor / gateway.
        // In many cases, your processor / gateway will only require a gatewayMerchantId.
        // Please refer to your processor's documentation for more information. The number of parameters
        // required and their names vary depending on the processor.
        public final List<Pair<String, String>> GATEWAY_TOKENIZATION_PARAMETERS = Arrays.asList(
                Pair.create("gatewayMerchantId", "exampleGatewayMerchantId")

                // Your processor may require additional parameters.
        );

        // Only used for DIRECT tokenization. Can be removed when using GATEWAY tokenization.
        public final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

        private Constants() {
        }

    }


}