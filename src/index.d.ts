declare module "react-native-allinone-upi" {

    export type MissingDataTypes = 'AMOUNT' | "UPI_ID" | "TRANSACTION_ID" | "CURRENCY" | "MERCHANT_CATEGORY_CODE" | "PAYEE_NAME";

    export enum UpiPaymentStatus {
        DATA_MISSING = -1,
        FAILED = 0,
        SUCCESS = 1

    }
    export interface UpiApp {
        name: string;
        packageName?: string; // Android
        scheme?: string;      // iOS
    }

    export interface TransactionParams {
        upi: string;
        transactionId: string;
        currency?: string;
        merchantCategoryCode: string;
        payeeName: string;
        amount: string;
        note?: string;
        appSpecific?: string; // e.g. 'com.google.android.apps.nbu.paisa.user' or 'tez://'
    }

    export interface TransactionResponse {
        paymentStatus: UpiPaymentStatus
        txnId?: string;
        txnRef?: string;
        missingData?: MissingDataTypes;
        responseCode?: string;
        message: string;
    }


    export function initiateTransaction(upiData: TransactionParams): Promise<TransactionResponse>;
    export function generateUpiString(upiData: Omit<TransactionParams, 'appSpecific'>): string;
    export function getInstalledUpiApps(): Promise<UpiApp[]>


}