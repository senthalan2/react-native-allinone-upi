# react-native-allinone-upi

UPI Payment using installed UPI Payment Apps in Mobile. Currently, supported only on Android.

## Installation

```sh
npm install react-native-allinone-upi
```

## Usage

```js
import { initiateTransaction } from 'react-native-allinone-upi';

// ...

initiateTransaction({
  upi: 'upi_id', // Required
  transactionId: 'transaction_id', // Required
  currency: 'INR', // Currency Code (Required)
  merchantCategoryCode: 'Merchant Category Code', // Four digit Code. (Required)
  payeeName: 'Name of the Payee', // Required
  amount: '1', // Amount must be in String and must be greater than 1.00 (Required)
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

| Key           | Value                         | Description                                                                                                           |
| ------------- | ----------------------------- | --------------------------------------------------------------------------------------------------------------------- |
| paymentStatus | -1 or 1 or 0                  | `-1` - DATA MISSING OR INVALID, `1` - SUCCESS, `0` - FAILURE                                                          |
| txnId         | String                        | Transaction ID return from the Payment App ( for Backend Process ) only return when the `paymentStatus` is `1` or `0` |
| txnRef        | String                        | Transaction Reference ID return from the Payment App ( only return when the `paymentStatus` is `1` or `0` )           |
| missingData   | [Missing Data](#missing-data) | Data which is missing or Invalid ( only return when the `paymentStatus` is `-1` )                                     |
| responseCode  | String                        | Code return from the Payment App ( only return when the `paymentStatus` is `1` or `0` )                               |
| message       | String                        | Message about Success or Failure or Invalid Data                                                                      |

#### `Missing Data`

`AMOUNT`, `UPI_ID`, `TRANSACTION_ID`, `CURRENCY`, `MERCHANT_CATEGORY_CODE`, `PAYEE_NAME`

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

## Would you like to support me?

<a href="https://www.buymeacoffee.com/senthalan2" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-red.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>
