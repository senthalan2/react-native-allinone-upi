package com.allinoneupi

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = AllinoneUpiModule.NAME)
class AllinoneUpiModule(reactContext: ReactApplicationContext) :
  NativeAllinoneUpiSpec(reactContext), ActivityEventListener {

  companion object {
    const val NAME = NativeAllinoneUpiSpec.NAME
    const val UPI_PAYMENT_CODE = 210
    const val SUCCESS_CODE = 1
    const val FAILURE_CODE = 0
    const val DATA_MISSING_CODE = -1
  }

  private var handlePromise: Promise? = null

  init {
    reactContext.addActivityEventListener(this)
  }

  override fun getName(): String = NAME

  override fun generateUpiString(
    upiId: String, transactionId: String, currency: String,
    merchantCategoryCode: String, payeeName: String, amount: String, note: String
  ): String {
    return "upi://pay?pa=$upiId&pn=${Uri.encode(payeeName)}&tr=$transactionId&am=$amount&cu=$currency&mc=$merchantCategoryCode&tn=${Uri.encode(note)}"
  }

  override fun getInstalledUpiApps(promise: Promise) {
    try {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse("upi://pay"))
      val pm = reactApplicationContext.packageManager
      val resolveInfoList = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

      val apps = Arguments.createArray()
      for (resolveInfo in resolveInfoList) {
        val map = Arguments.createMap()
        map.putString("packageName", resolveInfo.activityInfo.packageName)
        map.putString("name", resolveInfo.loadLabel(pm).toString())
        apps.pushMap(map)
      }
      promise.resolve(apps)
    } catch (e: Exception) {
      promise.reject("FETCH_ERROR", e.message)
    }
  }

  override fun initiateTransaction(
    upiId: String, transactionId: String, currency: String,
    merchantCategoryCode: String, payeeName: String,
    amount: String, note: String, appSpecific: String,
    promise: Promise
  ) {
    handlePromise = promise
    val responseData = Arguments.createMap()

    // Missing Data Validation
    if (upiId.trim().isEmpty()) return resolveError(responseData, "UPI_ID", "UPI ID cannot be null")
    if (transactionId.trim().isEmpty()) return resolveError(responseData, "TRANSACTION_ID", "Transaction ID cannot be null")
    if (amount.trim().isEmpty() || amount == "0") return resolveError(responseData, "AMOUNT", "Amount should be greater than 0")

    val url = generateUpiString(upiId, transactionId, currency, merchantCategoryCode, payeeName, amount, note)
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)

    if (appSpecific.trim().isNotEmpty() && appSpecific != "upi://") {
      intent.setPackage(appSpecific)
    }

    val activity = currentActivity
    if (activity != null) {
      try {
        val chooser = if (appSpecific.isNotEmpty()) intent else Intent.createChooser(intent, "Pay with")
        activity.startActivityForResult(chooser, UPI_PAYMENT_CODE)
      } catch (e: Exception) {
        handlePromise?.reject("APP_NOT_FOUND", "No UPI App found or App not installed")
        handlePromise = null
      }
    } else {
      handlePromise?.reject("FAILURE", "Activity doesn't exist")
      handlePromise = null
    }
  }

  private fun resolveError(map: WritableMap, missingData: String, message: String) {
    map.putInt("paymentStatus", DATA_MISSING_CODE)
    map.putString("missingData", missingData)
    map.putString("message", message)
    handlePromise?.resolve(map)
    handlePromise = null
  }

  override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
    if (handlePromise == null || requestCode != UPI_PAYMENT_CODE) return
    val responseData = Arguments.createMap()

    if (data == null) {
      responseData.putInt("paymentStatus", FAILURE_CODE)
      responseData.putString("message", "Cancelled by user")
      handlePromise?.resolve(responseData)
      handlePromise = null
      return
    }

    try {
      val response = data.getStringExtra("response") ?: "Status=FAILURE"
      val resMap = response.split("&").associate {
        val parts = it.split("=")
        parts[0] to (if (parts.size > 1) parts[1] else "")
      }

      val status = resMap["Status"] ?: "FAILURE"

      responseData.putString("txnId", resMap["txnId"])
      responseData.putString("txnRef", resMap["txnRef"])
      responseData.putString("responseCode", resMap["responseCode"])

      if (status.equals("SUCCESS", ignoreCase = true) || status.equals("submitted", ignoreCase = true)) {
        responseData.putInt("paymentStatus", SUCCESS_CODE)
        responseData.putString("message", "SUCCESS")
      } else {
        responseData.putInt("paymentStatus", FAILURE_CODE)
        responseData.putString("message", status)
      }
      handlePromise?.resolve(responseData)
    } catch (e: Exception) {
      handlePromise?.reject("FAILURE", e.message)
    }
    handlePromise = null
  }

  override fun onNewIntent(intent: Intent) {}
}
