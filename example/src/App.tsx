import * as React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import {
  initiateTransaction,
  type UPITransactionResponseProps,
} from 'react-native-allinone-upi';

export default function App() {
  const startTransaction = () => {
    initiateTransaction({
      upi: 'UPI_ID',
      transactionId: 'TXN_ID',
      currency: 'INR',
      merchantCategoryCode: 'MCC',
      payeeName: 'PAYEE_NAME',
      amount: 'AMOUNT',
      note: 'NOTE',
    })
      .then((res: UPITransactionResponseProps) => {
        console.log(res, 'RESPONSE');
      })
      .catch((e) => {
        console.log(e.message, 'ERROR PAYMENT');
      });
  };

  return (
    <View style={styles.mainContainer}>
      <TouchableOpacity style={styles.button} onPress={startTransaction}>
        <Text style={styles.buttonText}>Start Payment</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  mainContainer: {
    flex: 1,
    backgroundColor: '#ffffff',
    paddingHorizontal: 15,
    justifyContent: 'center',
    alignItems: 'center',
  },
  button: {
    paddingHorizontal: 10,
    paddingVertical: 20,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 20,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 1,
    },
    shadowOpacity: 0.22,
    shadowRadius: 2.22,
    backgroundColor: 'tomato',
    elevation: 3,
  },
  buttonText: {
    fontWeight: 'bold',
    fontSize: 16,
    color: '#000000',
  },
});
