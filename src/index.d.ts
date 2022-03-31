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

     export interface UPITransactionResponseProps {
        paymentStatus : number,
        txnId : string,
        txnRef : string,
        missingData : string | undefined,
        responseCode : string,
        message : string

     } 

    export function initiateTransaction(upiData:UPITransactionProps) : Promise< UPITransactionResponseProps >;

}