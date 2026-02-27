import NativeAllinoneUpi from './NativeAllinoneUpi';
import type { TransactionParams, TransactionResponse, UpiApp } from './types';


export const getInstalledUpiApps = async (): Promise<UpiApp[]> => {
  const apps = await NativeAllinoneUpi.getInstalledUpiApps();
  return apps as UpiApp[];
};

export const generateUpiString = (params: Omit<TransactionParams, 'appSpecific'>): string => {
  return NativeAllinoneUpi.generateUpiString(
    params.upi,
    params.transactionId,
    params.currency || 'INR',
    params.merchantCategoryCode,
    params.payeeName,
    params.amount,
    params.note || ''
  );
};

export const initiateTransaction = async (
  params: TransactionParams
): Promise<TransactionResponse> => {
  const {
    upi,
    transactionId,
    currency = 'INR',
    merchantCategoryCode,
    payeeName,
    amount,
    note = '',
    appSpecific = '',
  } = params;

  const response = await NativeAllinoneUpi.initiateTransaction(
    upi, transactionId, currency, merchantCategoryCode, payeeName, amount, note, appSpecific
  );

  return response as TransactionResponse;
};