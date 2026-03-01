import Foundation
import UIKit
import React

@objc(AllinoneUpiSwift)
public class AllinoneUpiSwift: NSObject {

    @objc
  public func getInstalledUpiApps(resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        let upiApps = [
            ["scheme": "tez://", "name": "Google Pay"],
            ["scheme": "phonepe://", "name": "PhonePe"],["scheme": "paytmmp://", "name": "Paytm"],["scheme": "bhim://", "name": "BHIM"]
        ]
        
      var installedApps: [[String: String]] = []
        DispatchQueue.main.async {
            for app in upiApps {
                if let scheme = app["scheme"], let url = URL(string: scheme), UIApplication.shared.canOpenURL(url) {
                    installedApps.append(app)
                }
            }
            resolve(installedApps)
        }
    }

    @objc
    public func generateUpiString(
        upiId: String, transactionId: String, currency: String,
        merchantCategoryCode: String, payeeName: String,
        amount: String, note: String
    ) -> String {
        let encodedPayee = payeeName.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        let encodedNote = note.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        return "upi://pay?pa=\(upiId)&pn=\(encodedPayee)&tr=\(transactionId)&am=\(amount)&cu=\(currency)&mc=\(merchantCategoryCode)&tn=\(encodedNote)"
    }

    @objc
    public func initiateTransaction(
        upiId: String, transactionId: String, currency: String,
        merchantCategoryCode: String, payeeName: String,
        amount: String, note: String, appSpecific: String,
        resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock
    ) {
        let baseString = generateUpiString(
            upiId: upiId, transactionId: transactionId, currency: currency,
            merchantCategoryCode: merchantCategoryCode, payeeName: payeeName,
            amount: amount, note: note
        )
        
        var urlString = baseString
        if !appSpecific.isEmpty && appSpecific != "upi://" {
            urlString = baseString.replacingOccurrences(of: "upi://", with: appSpecific)
        }
        
        guard let url = URL(string: urlString) else {
            resolve(["paymentStatus": 0, "message": "Invalid URL generated"])
            return
        }
        
        DispatchQueue.main.async {
            if UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url, options: [:]) { success in
                    if success {
                        resolve([
                            "paymentStatus": 1,
                            "txnId": transactionId,
                            "txnRef": "IOS_VERIFY_ON_SERVER",
                            "responseCode": "00",
                            "message": "SUCCESS"
                        ])
                    } else {
                        resolve(["paymentStatus": 0, "message": "Failed to open UPI app"])
                    }
                }
            } else {
                resolve(["paymentStatus": 0, "message": "UPI App not installed"])
            }
        }
    }
}

