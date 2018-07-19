package apps.dabinu.com.p_integrate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SplashActivity extends AppCompatActivity {


    private EditText emailField, cardNumberField, expiryMonthField, expiryYearField, cvvField;
    private String your_api_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //init layout
        emailField = findViewById(R.id.user_email_address);
        cardNumberField = findViewById(R.id.user_card_number);
        expiryMonthField = findViewById(R.id.user_expiry_month);
        expiryYearField = findViewById(R.id.user_expiry_year);
        cvvField = findViewById(R.id.user_cvv);


        //This part is very compulsory
//        PaystackSdk.initialize(this.getApplicationContext());
//        PaystackSdk.setPublicKey(your_api_key);

    }
}
