import { NativeModules, Platform } from 'react-native';
import { UPIPayment } from './UPIPayment';

const LINKING_ERROR =
  `The package 'react-native-allinone-upi' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AllinoneUpi = NativeModules.AllinoneUpi
  ? NativeModules.AllinoneUpi
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export const initiateTransaction = (paymentData) => {
  return UPIPayment(paymentData, AllinoneUpi);
};
