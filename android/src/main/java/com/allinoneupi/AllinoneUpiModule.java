package com.allinoneupi;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.allinoneupi.Payments.AllUpiPayment;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = AllinoneUpiModule.NAME)
public class AllinoneUpiModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  public static final String NAME = "AllinoneUpi";
  final int UPI_PAYMENT_CODE = 210;
  final int SUCCESS_CODE = 1;
  final int FAILURE_CODE = 0;
  final int DATA_MISSING_CODE = -1;
  Promise handlePromise;
  public AllinoneUpiModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(this);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }

  @Override
  public void onNewIntent(Intent intent) {

  }

  @ReactMethod
  public void initiateTransaction(String upiId,
                                  String transactionId,
                                  String currency,
                                  String merchantCategoryCode,
                                  String payeeName,
                                  String amount,
                                  String note,
                                  Promise promise) {

    handlePromise = promise;
    WritableMap responseData = Arguments.createMap();

    if(upiId == null || upiId.trim().isEmpty()){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "UPI_ID");
      responseData.putString("message","UPI ID cannot be null");
      handlePromise.resolve(responseData);
      return;
    }
    else if(transactionId == null || transactionId.trim().isEmpty()){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "TRANSACTION_ID");
      responseData.putString("message","Transaction ID cannot be null");
      handlePromise.resolve(responseData);
      return;
    }
    else if(currency == null || currency.trim().isEmpty()){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "CURRENCY");
      responseData.putString("message","Currency cannot be null");
      handlePromise.resolve(responseData);
      return;
    }
    else if(merchantCategoryCode == null || merchantCategoryCode.trim().isEmpty()){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "MERCHANT_CATEGORY_CODE");
      responseData.putString("message","Merchant Category Code cannot be null");
      handlePromise.resolve(responseData);
      return;
    }
    else if(payeeName == null || payeeName.trim().isEmpty()){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "PAYEE_NAME");
      responseData.putString("message","Payee Name cannot be null");
      handlePromise.resolve(responseData);
      return;
    }
    else if(amount == null || amount.trim().isEmpty()){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "AMOUNT");
      responseData.putString("message","Amount cannot be null");
      handlePromise.resolve(responseData);
      return;
    }
    else if(amount.equals("0")){
      responseData.putInt("paymentStatus", DATA_MISSING_CODE);
      responseData.putString("missingData", "AMOUNT");
      responseData.putString("message","Amount should be greater than 0");
      handlePromise.resolve(responseData);
      return;
    }
    AllUpiPayment upiPayment = new AllUpiPayment(getCurrentActivity(), handlePromise,upiId,transactionId,currency, merchantCategoryCode, payeeName, amount, note,UPI_PAYMENT_CODE);
  }


  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    WritableMap responseData = Arguments.createMap();


    try{
      if(data == null){
        handlePromise.reject("FAILURE","No Action Taken.Null from UPI");
        return;
      }
      if(requestCode == UPI_PAYMENT_CODE){
        if(data.getStringExtra("Status").trim().equals("SUCCESS")){
          responseData.putInt("paymentStatus", SUCCESS_CODE);
          responseData.putString("txnId", data.getStringExtra("txnId"));
          responseData.putString("txnRef", data.getStringExtra("txnRef"));
          responseData.putString("responseCode", data.getStringExtra("responseCode"));
          responseData.putString("message",data.getStringExtra("Status"));
          handlePromise.resolve(responseData);
        }
        else{
          responseData.putInt("paymentStatus", FAILURE_CODE);
          responseData.putString("txnId", data.getStringExtra("txnId"));
          responseData.putString("txnRef", data.getStringExtra("txnRef"));
          responseData.putString("responseCode", data.getStringExtra("responseCode"));
          responseData.putString("message",data.getStringExtra("Status"));
          handlePromise.resolve(responseData);
        }
      }
      else{
        handlePromise.reject("FAILURE","Request Code Mismatch");
      }
    }
    catch (Exception e){
      if(handlePromise != null){
        handlePromise.reject("FAILURE",e.getMessage());
      }
    }

  }

}
