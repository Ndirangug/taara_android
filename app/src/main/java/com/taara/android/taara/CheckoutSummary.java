package com.taara.android.taara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

public class CheckoutSummary extends AppCompatActivity {

    public static String[][] cartItems;
    final double VAT_RATE = 0.16, SERVICE_CHARGE = 0.005;
    double subTotal = 0, vatTotal = 0, serviceCharge = 0, total = 0;
    TextView txtSubtotal, txtServiceCharge, txtTotal, txtVat;
    EditText edtPhone;
    String itemIds;
    Intent intent;
    SharedPreferences sharedPreferencesUserSession;
    StringBuilder stringBuilder;

    String firstname, secondname, email, phone, storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_summary);
        stringBuilder = new StringBuilder();
        intent = getIntent();
        storeId = intent.getStringExtra("STORE_ID");
        txtServiceCharge = findViewById(R.id.amtServiceChargeCheckout);
        txtSubtotal = findViewById(R.id.amtSubtotalSummary);
        txtTotal = findViewById(R.id.amtCheckoutTotal);
        txtVat = findViewById(R.id.amtVatCheckout);
        edtPhone = findViewById(R.id.edtPhoneCheckout);


        sharedPreferencesUserSession = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        firstname = sharedPreferencesUserSession.getString("FIRST_NAME", "FIRST NAME");
        email = sharedPreferencesUserSession.getString("EMAIL", "EMAIL");
        phone = sharedPreferencesUserSession.getString("PHONE", "PHONE");
        secondname = sharedPreferencesUserSession.getString("SECOND_NAME", "SECOND NAME");

        edtPhone.setText(phone);

        /*index 0 => title
        index 1 => description
        index 2 => price
        index 3 => rfid
        index 4 => VAT*/
        cartItems = Cart.cartItems;


        try {
            for (int i = 0; cartItems[i][0] != null; i++) {
                subTotal += Double.parseDouble(cartItems[i][2]);
                if (cartItems[i][4].equals("16")) {
                    vatTotal += VAT_RATE * Double.parseDouble(cartItems[i][2]);
                }
                //itemIds += cartItems[i][3] + ",";
                itemIds = stringBuilder.append(cartItems[i][3]).append(",").toString();
            }

            Log.i("rfids", itemIds);
            serviceCharge = SERVICE_CHARGE * subTotal;
            BigDecimal serviceChargeRounded = BigDecimal.valueOf(serviceCharge).setScale(2, BigDecimal.ROUND_HALF_UP);
            serviceCharge = Double.valueOf(String.valueOf(serviceChargeRounded));
            total = subTotal + vatTotal + serviceCharge;
//             totalRounded = BigDecimal.valueOf(total).round(MathContext.DECIMAL64).setScale(2);
//             subtotalRounded = BigDecimal.valueOf(subTotal).round(MathContext.DECIMAL64).setScale(2);
//             vatlRounded = BigDecimal.valueOf(vatTotal).round(MathContext.DECIMAL64).setScale(2);

            txtVat.setText("Kshs " + String.valueOf(vatTotal));
            txtTotal.setText(String.valueOf(total));
            txtSubtotal.setText("Kshs " + String.valueOf(subTotal));
            txtServiceCharge.setText("Kshs " + String.valueOf(serviceChargeRounded));
        } catch (NullPointerException e) {
            e.printStackTrace();
            Snackbar.make(edtPhone.getRootView(), e.toString(), Snackbar.LENGTH_SHORT).show();
        } catch (ArithmeticException e) {
            e.printStackTrace();

        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btnProceedPay) {


            Intent intent = new Intent(getApplicationContext(), IpayGateway.class);
            intent.putExtra("PHONE", edtPhone.getText().toString())
                    .putExtra("TOTAL", total)
                    .putExtra("EMAIL", email)
                    .putExtra("STORE_ID", storeId)
                    .putExtra("RFIDS", itemIds);
            startActivity(intent);
        }

    }


    public void back(View view) {
        onBackPressed();
    }
}
