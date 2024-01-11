import { Platform } from 'react-native';

export interface UPITransactionProps {
  upi: string;
  transactionId: string;
  currency: string | 'INR';
  merchantCategoryCode: string;
  payeeName: string;
  amount: string;
  note?: string | '';
}
const AMOUNT_REG = /^([0-9]+)?(?:[.][0-9]{1,2})?$/;
export function UPIPayment(
  paymentData: UPITransactionProps,
  AllinoneUpi: {
    initiateTransaction: (
      arg0: string,
      arg1: string,
      arg2: string,
      arg3: string,
      arg4: string,
      arg5: string,
      arg6: string | undefined
    ) => any;
  }
) {
  if (
    parseInt(paymentData.amount) >= 1 &&
    AMOUNT_REG.test(paymentData.amount)
  ) {
    if (Platform.OS === 'android') {
      return AllinoneUpi.initiateTransaction(
        paymentData.upi,
        paymentData.transactionId,
        paymentData.currency,
        paymentData.merchantCategoryCode,
        paymentData.payeeName,
        paymentData.amount,
        paymentData.note
      );
    } else {
      return Promise.reject('Not Supported');
    }
  } else {
    return Promise.resolve({
      paymentStatus: -1,
      missingData: 'AMOUNT',
      message: 'Invalid Amount',
    });
  }
}
