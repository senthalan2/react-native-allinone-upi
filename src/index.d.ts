declare module "react-native-allinone-upi" {

  export interface UPITransactionProps {
    upi: string;
    transactionId: string;
    currency: string | 'INR';
    merchantCategoryCode: string;
    payeeName: string;
    amount: string;
    note?: string | null;
  }

  export type MissingDataTypes = 'AMOUNT' | "UPI_ID" | "TRANSACTION_ID" | "CURRENCY" | "MERCHANT_CATEGORY_CODE" | "PAYEE_NAME";

  export interface UPITransactionResponseProps {
    paymentStatus: number,
    txnId: string,
    txnRef: string,
    missingData?: MissingDataTypes | undefined,
    responseCode: string,
    message: string

  }

  export function initiateTransaction(upiData: UPITransactionProps): Promise<UPITransactionResponseProps>;

}