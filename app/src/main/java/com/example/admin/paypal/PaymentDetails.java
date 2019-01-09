package com.example.admin.paypal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView textView1, textView2, textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        textView1 = (TextView) findViewById(R.id.Ammount);
        textView2 = (TextView) findViewById(R.id.textstatus);
        textView3 = (TextView) findViewById(R.id.trancationid);
        Intent intent = getIntent();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response, String paymentAmount) {
        try {
            textView1.setText(response.getString(String.format("$%s", paymentAmount)));
            textView2.setText(response.getString("state"));
            textView3.setText(response.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
