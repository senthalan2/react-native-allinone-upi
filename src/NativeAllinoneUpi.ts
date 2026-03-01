import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Spec extends TurboModule {
  // 1. Fetch installed apps
  getInstalledUpiApps(): Promise<Object[]>;

  // 2. Main Transaction Method
  initiateTransaction(
    upiId: string,
    transactionId: string,
    currency: string,
    merchantCategoryCode: string,
    payeeName: string,
    amount: string,
    note: string,
    appSpecific: string
  ): Promise<Object>;

  // 3. NEW: Synchronous method to generate UPI URI for QR codes
  generateUpiString(
    upiId: string,
    transactionId: string,
    currency: string,
    merchantCategoryCode: string,
    payeeName: string,
    amount: string,
    note: string
  ): string;
}

export default TurboModuleRegistry.getEnforcing<Spec>('AllinoneUpi');
