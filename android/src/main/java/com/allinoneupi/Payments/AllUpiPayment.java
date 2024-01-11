package com.allinoneupi.Payments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Promise;

public class AllUpiPayment {

  Activity currentActivity;
  Promise promise;
  private final String upiId;
  private final String transactionId;
  private final String currency;
  private final String merchantCategoryCode;
  private final String payeeName;
  private final String amount;
  private final String note;
  final int UPI_PAYMENT_CODE;

  public AllUpiPayment(Activity currentActivity, Promise promise,
                       String upiId,
                       String transactionId,
                       String currency,
                       String merchantCategoryCode,
                       String payeeName,
                       String amount,
                       String note,
                       int paymentCode) {



    this.promise = promise;
    this.upiId = upiId;
    this.transactionId = transactionId;
    this.currency = currency;
    this.merchantCategoryCode = merchantCategoryCode;
    this.payeeName = payeeName;
    this.amount = amount;
    this.note = note;
    this.currentActivity = currentActivity;
    this.UPI_PAYMENT_CODE = paymentCode;

    startTransaction();
  }

  void startTransaction(){

    Uri.Builder uriBuilder = Uri.parse("upi://pay").buildUpon();
    uriBuilder.appendQueryParameter("pa", upiId)
      .appendQueryParameter("tr", transactionId)
      .appendQueryParameter("cu", currency)
      .appendQueryParameter("mc", merchantCategoryCode)
      .appendQueryParameter("pn", payeeName)
      .appendQueryParameter("am",amount);

    if(note != null && !note.trim().isEmpty()){
      uriBuilder.appendQueryParameter("tn",note);
    }
    Uri uri = uriBuilder.build();

    Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
    upiPayIntent.setData(uri);

    Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
    currentActivity.startActivityForResult(chooser,UPI_PAYMENT_CODE);

  }

}
