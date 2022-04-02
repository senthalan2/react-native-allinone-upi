# react-native-allinone-upi

UPI Payment using installed UPI Payment Apps in Mobile. Currently, Supported oinly on Android.

## Installation

```sh
npm install react-native-allinone-upi
```

## Usage

```js
import { initiateTransaction } from 'react-native-allinone-upi';

// ...

 initiateTransaction({
      upi: 'upi_id',  // Required
      transactionId: 'transaction_id',  // Required
      currency: 'INR',   // Currency Code (Required)
      merchantCategoryCode: 'Merchant Category Code',  // Four digit Code. (Required)
      payeeName: 'Name of the Payee',  (Required)
      amount: '1',  // Amount must be in String and must be greater than 1.00 (Required)
      note: 'test', // Additional Notes or description (Optional)
    })
      .then((res) => {
        console.log(res, 'RESPONSE');
      })
      .catch((e) => {
        console.log(e.message, 'ERROR');
      });
```

### Response Props

Key | Value | Description  
--- | --- | ---
paymentStatus | -1 or 1 or 0 | ```-1``` - DATE MISSING OR INVALID, ```1``` - SUCCESS, ```0``` - FAILURE
txnId | true or false | iT is about the Device is Approved or not. ```true``` - Approved, ```false``` - Not Approved  
txnRef |JSON DATA | The device returns XML DATA of Device Information. this parameter contains converted JSON DATA of XML DATA
missingData | [`Missing` (`Data`)](#missing-data) | Device Information
responseCode | Code return from Payment App
message | Message about Success or Failure or Invalid Data

#### `Missing` `Data`



## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
