
# 💸 react-native-allinone-upi

A powerful and easy-to-use React Native library for integrating Unified Payments Interface (UPI) payments. 

This package allows you to detect installed UPI applications on a user's device, dynamically generate UPI intent strings, and seamlessly initiate UPI transactions for both Android and iOS.

---

## ✨ Features

- 🎯 **App-Specific Targeting:** Force transactions through a specific app (e.g., GPay, PhonePe).
- 📱 **Detect Installed Apps**: Fetch a list of UPI apps currently installed on the user's device.
- 🔗 **QR Code URI Generation:** Synchronously generate raw `upi://pay` strings.
- 💸 **Initiate Transactions**: Launch UPI payments with detailed parameter configurations.
- 🛡 **Strongly Typed**: Full TypeScript support out of the box with precise status and error handling.

---

## 📦 Installation

```bash
npm install react-native-allinone-upi
# or
yarn add react-native-allinone-upi
```

### iOS Setup (Required)

Due to strict iOS security policies, you must explicitly declare which UPI schemes your app is allowed to open. Add the following to your `ios/YourAppName/Info.plist`:

```xml
<key>LSApplicationQueriesSchemes</key>
<array>
  <string>tez</string>
  <string>phonepe</string>
  <string>paytmmp</string>
  <string>bhim</string>
  <string>upi</string>
</array>
```

Then install pods:
```bash
cd ios && pod install
```

### Android Setup (Required)
Android 11+ requires package visibility configurations. Add the `<queries>` block to your `android/app/src/main/AndroidManifest.xml` (outside the `<application>` tag):

```xml
<queries>
    <intent>
        <action android:name="android.intent.action.VIEW" />
        <data android:scheme="upi" android:host="pay" />
    </intent>
</queries>
```

---

## 🚀 Usage

### 1. Initiate a Payment

Triggers the UPI transaction. On Android, it opens the app and returns the callback. On iOS, it opens the app.

```typescript
import { initiateTransaction, UpiPaymentStatus } from 'react-native-allinone-upi';

const payNow = async () => {
  try {
    const response = await initiateTransaction({
      upi: 'merchant@upi', // Required
      transactionId: 'TXN123456789', // Required
      currency: 'INR', // Optional, Defaults to INR
      merchantCategoryCode: '0000', // Required
      payeeName: 'John Doe Store', // Required
      amount: '10.50', // Required
      note: 'Order Payment', // Optional
      appSpecific: 'phonepe://' // Optional: Target specific app
    });

    if (response.paymentStatus === UpiPaymentStatus.SUCCESS) {
      console.log('Payment Successful or Submitted:', response.txnId);
    } else if (response.paymentStatus === UpiPaymentStatus.DATA_MISSING) 	{
      console.log('Missing Data:', response.missingData, response.message);
    } else {
      console.log('Payment Failed:', response.message);
    }
  } catch (error) {
    console.error('Error initiating transaction:', error);
  }
};
```

### 2. Get Installed UPI Apps

Fetch a list of available UPI apps currently installed on the user's device.

```typescript
import { getInstalledUpiApps, UpiApp } from 'react-native-allinone-upi';

const fetchApps = async () => {
  try {
    const apps: UpiApp[] = await getInstalledUpiApps();
    console.log(apps);
    // Output example: 
    // Android:[{ name: 'Google Pay', packageName: 'com.google.android.apps.nbu.paisa.user' }]
    // iOS: [{ name: 'PhonePe', scheme: 'phonepe://' }]
  } catch (error) {
    console.error('Error fetching apps:', error);
  }
};

```

### 3. Generate Raw UPI String (For QR Codes)

Synchronous method to generate a standard UPI URI. Perfect for pairing with QR code libraries.

```typescript
import { generateUpiString } from 'react-native-allinone-upi';

const upiString = generateUpiString({
  upi: 'merchant@upi',
  transactionId: 'TXN1234567890',
  merchantCategoryCode: '0000',
  payeeName: 'John Doe',
  amount: '100.50',
  note: 'Order Payment'
});

console.log(upiString); 
// upi://pay?pa=merchant@upi&pn=John%20Doe&tr=TXN1234567890&mc=0000&am=100.50&tn=Order%20Payment
```

---

## 📊 API Reference

### `TransactionParams`
| Property | Type | Required | Description |
| :--- | :--- | :---: | :--- |
| `upi` | `string` | ✅ | The Merchant/Payee UPI ID |
| `transactionId` | `string` | ✅ | Unique transaction reference ID |
| `merchantCategoryCode` | `string` | ✅ | 4-digit Merchant Category Code |
| `payeeName` | `string` | ✅ | Name of the Payee/Merchant |
| `amount` | `string` | ✅ | Transaction amount (e.g., `"1.00"`) |
| `currency` | `string` | ❌ | Currency code (Defaults to `"INR"`) |
| `note` | `string` | ❌ | Transaction note/description |
| `appSpecific` | `string` | ❌ | Pass a package name (Android) or scheme (iOS) to target a specific app directly |

### `TransactionResponse`
| Property | Type | Description |
| :--- | :--- | :--- |
| `paymentStatus` | `UpiPaymentStatus` | `-1` (Data Missing), `0` (Failed), or `1` (Success) |
| `message` | `string` | Descriptive message about the payment result |
| `txnId` | `string` | Transaction ID returned from the Payment App (if available) |
| `txnRef` | `string` | Transaction Reference ID returned from the Payment App (if available) |
| `responseCode` | `string` | Bank/UPI Response Code (if available) |
| `missingData` | `MissingDataTypes` | Denotes which field was missing (only when status is `-1`) |

### `UpiPaymentStatus` (Enum)
| Key | Value | Description |
| :--- | :--- | :--- |
| `DATA_MISSING` | `-1` | Required inputs were omitted |
| `FAILED` | `0` | Payment was cancelled, declined, or failed |
| `SUCCESS` | `1` | Payment was successful (Android) or App opened successfully (iOS) |

### `MissingDataTypes`

`AMOUNT` | `UPI_ID` | `TRANSACTION_ID` | `CURRENCY` | `MERCHANT_CATEGORY_CODE` | `PAYEE_NAME`

---

## ⚠️ Important Note on Platform Differences

### Android
On Android, the library uses `startActivityForResult`. When the user completes the payment and returns to your app, the UPI app passes the transaction data back directly. The package will instantly resolve with `paymentStatus: 1` or `0` based on the real transaction status.

### iOS
iOS handles intents securely via `UIApplication.shared.openURL` and **does not** return an automatic callback with transaction data to the originating app. 
Therefore, on iOS, a `paymentStatus: 1` simply means the **UPI app was successfully opened**. 
**You must verify the final status of the transaction via your backend server webhook** using the `transactionId` you provided.

---

## Contributing

See the [Contributing](https://github.com/senthalan2/react-native-allinone-upi/?tab=contributing-ov-file) to learn how to contribute to the repository and the development workflow.

## 📄 License
MIT 

## Would you like to support me?

<a href="https://www.buymeacoffee.com/senthalan2" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-red.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>
