import * as React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import {
  initiateTransaction,
  generateUpiString,
  getInstalledUpiApps,
  type UpiApp,
  TransactionResponse,
} from 'react-native-allinone-upi';

export default function App() {
  const [generatedStr, setgeneratedStr] = React.useState<string>('');
  const [installedApps, setinstalledApps] = React.useState<UpiApp[]>([]);
  const [installedAppsError, setinstalledAppsError] =
    React.useState<string>('');
  const [transResponse, settransResponse] = React.useState<string>('');
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
      .then((res: TransactionResponse) => {
        settransResponse(JSON.stringify(res));
      })
      .catch((e) => {
        settransResponse(e.message);
      });
  };

  const handleGenerate = () => {
    const str = generateUpiString({
      upi: 'UPI_ID',
      transactionId: 'TXN_ID',
      currency: 'INR',
      merchantCategoryCode: 'MCC',
      payeeName: 'PAYEE_NAME',
      amount: 'AMOUNT',
      note: 'NOTE',
    });
    setgeneratedStr(str);
  };

  const handleGetApps = () => {
    getInstalledUpiApps()
      .then((res) => {
        if (res.length > 0) {
          setinstalledAppsError('');
          setinstalledApps(res);
        } else {
          setinstalledAppsError('No Apps found');
        }
      })
      .catch((e) => {
        setinstalledAppsError(e.message);
      });
  };

  const handleClearAll = () => {
    setinstalledAppsError('');
    setinstalledApps([]);
    setgeneratedStr('');
    settransResponse('');
  };

  return (
    <View style={styles.mainContainer}>
      <TouchableOpacity style={styles.button} onPress={startTransaction}>
        <Text style={styles.buttonText}>Start Payment</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.button} onPress={handleGetApps}>
        <Text style={styles.buttonText}>Get Installed Apps</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.button} onPress={handleGenerate}>
        <Text style={styles.buttonText}>Generate Upi String</Text>
      </TouchableOpacity>
      <TouchableOpacity style={styles.button} onPress={handleClearAll}>
        <Text style={styles.buttonText}>Clear All</Text>
      </TouchableOpacity>

      {installedApps.length > 0 && (
        <Text style={styles.text}>
          Installed Apps: {JSON.stringify(installedApps)}
        </Text>
      )}
      {installedAppsError && (
        <Text style={styles.text}>
          Installed Apps Error:{installedAppsError}
        </Text>
      )}

      {generatedStr && (
        <Text style={styles.text}>Generated String:{generatedStr}</Text>
      )}
      {transResponse && (
        <Text style={styles.text}>Transaction Response:{transResponse}</Text>
      )}
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
    marginVertical: 10,
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
  text: {
    textAlign: 'center',
    fontSize: 14,
    color: '#000000',
    marginVertical: 20,
    marginHorizontal: 10,
  },
});
