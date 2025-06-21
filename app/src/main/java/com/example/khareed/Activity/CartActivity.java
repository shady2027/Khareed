package com.example.khareed.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.khareed.Adapter.CartAdapter;
import com.example.khareed.Helper.ChangeNumberItemsListener;
import android.widget.Toast;
import com.example.khareed.Helper.ManagmentCart;
import com.example.khareed.R;
import com.example.khareed.databinding.ActivityCartBinding;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class CartActivity extends BaseActivity implements PaymentResultListener {
    ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        calculatorCart();
        setVarialbe();
        initCartList();
    }

    private void initCartList() {
        if(managmentCart.getListCart().isEmpty()){
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVarialbe() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        binding.totalFeeTxt.setText("\u20B9" + itemTotal);
        binding.taxTxt.setText("\u20B9" + tax);
        binding.deliveryTxt.setText("\u20B9" + delivery);
        binding.totalTxt.setText("\u20B9" + total);

        binding.checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // amount that is entered by user.
               // String samount = amountEdt.getText().toString();

                // rounding off the amount.
              //  int amount = Math.round(Float.parseFloat(samount) * 100);
                int amount = (int) Math.round(total)*100;

                // initialize Razorpay account.
                Checkout checkout = new Checkout();

                // set your id as below
                checkout.setKeyID("rzp_test_XFh3Tb7XNGBvvF");

                // set image
                checkout.setImage(R.drawable.user_img);

                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "Khareed");

                    // put description
                    object.put("description", "Total payment");

                    // to set theme color
                    object.put("theme.color", "#169955");

                    // put the currency
                    object.put("currency", "INR");

                    // put amount
                    object.put("amount", amount);

                    // put mobile number
                    object.put("prefill.contact", "");

                    // put email
                    object.put("prefill.email", "" +
                            "");

                    // open razorpay to checkout activity
                    checkout.open(CartActivity.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        // this method is called on payment success.
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        // on payment failed.
        Toast.makeText(this, "Payment Unsuccessful ", Toast.LENGTH_SHORT).show();
    }
}