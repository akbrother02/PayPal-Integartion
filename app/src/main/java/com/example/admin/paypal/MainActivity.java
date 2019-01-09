package com.example.admin.paypal;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.paypal.config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONException;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE=7171;
    public static PayPalConfiguration configuration=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.CLIENT_ID);
    Button button;
    EditText editText;
    String amount="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editamount);

        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        startService(intent);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    private void processPayment() {
    amount=editText.getText().toString();
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","Sending",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PAYPAL_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Parcelable[] paymentConfirmation=data.getParcelableArrayExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation!=null) {
                        String paymentDetails=paymentConfirmation.toString();
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails",paymentDetails)
                                .putExtra("PaymentAmount",amount)
                        );
                }
                else if(resultCode==Activity.RESULT_CANCELED) {
                    Toast.makeText(this,"Cancel",Toast.LENGTH_SHORT).show();
                }
            }
            else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID)
                Toast.makeText(this,"Invalid",Toast.LENGTH_SHORT).show();
        }
    }
}
