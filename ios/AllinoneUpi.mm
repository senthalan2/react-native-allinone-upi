#import "AllinoneUpi.h"
#import <UIKit/UIKit.h>

@implementation AllinoneUpi

RCT_EXPORT_MODULE()

- (void)getInstalledUpiApps:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    NSArray *upiApps = @[
        @{@"scheme": @"tez://", @"name": @"Google Pay"},
        @{@"scheme": @"phonepe://", @"name": @"PhonePe"},
        @{@"scheme": @"paytmmp://", @"name": @"Paytm"},
        @{@"scheme": @"bhim://", @"name": @"BHIM"}
    ];

    dispatch_async(dispatch_get_main_queue(), ^{
        NSMutableArray *installedApps = [NSMutableArray new];
        for (NSDictionary *app in upiApps) {
            NSURL *url = [NSURL URLWithString:app[@"scheme"]];
            if (url && [[UIApplication sharedApplication] canOpenURL:url]) {
                [installedApps addObject:app];
            }
        }
        resolve(installedApps);
    });
}

- (NSString *)generateUpiString:(NSString *)upiId
                  transactionId:(NSString *)transactionId
                       currency:(NSString *)currency
           merchantCategoryCode:(NSString *)merchantCategoryCode
                      payeeName:(NSString *)payeeName
                         amount:(NSString *)amount
                           note:(NSString *)note {
    NSCharacterSet *allowed = [NSCharacterSet URLQueryAllowedCharacterSet];
    NSString *encodedPayee = [payeeName stringByAddingPercentEncodingWithAllowedCharacters:allowed] ?: @"";
    NSString *encodedNote  = [note stringByAddingPercentEncodingWithAllowedCharacters:allowed] ?: @"";
    return [NSString stringWithFormat:@"upi://pay?pa=%@&pn=%@&tr=%@&am=%@&cu=%@&mc=%@&tn=%@",
            upiId, encodedPayee, transactionId, amount, currency, merchantCategoryCode, encodedNote];
}

- (void)initiateTransaction:(NSString *)upiId
              transactionId:(NSString *)transactionId
                   currency:(NSString *)currency
       merchantCategoryCode:(NSString *)merchantCategoryCode
                  payeeName:(NSString *)payeeName
                     amount:(NSString *)amount
                       note:(NSString *)note
                appSpecific:(NSString *)appSpecific
                    resolve:(RCTPromiseResolveBlock)resolve
                     reject:(RCTPromiseRejectBlock)reject {

    NSString *baseString = [self generateUpiString:upiId
                                     transactionId:transactionId
                                          currency:currency
                              merchantCategoryCode:merchantCategoryCode
                                         payeeName:payeeName
                                            amount:amount
                                              note:note];

    NSString *urlString = baseString;
    if (appSpecific.length > 0 && ![appSpecific isEqualToString:@"upi://"]) {
        urlString = [baseString stringByReplacingOccurrencesOfString:@"upi://" withString:appSpecific];
    }

    NSURL *url = [NSURL URLWithString:urlString];
    if (!url) {
        resolve(@{@"paymentStatus": @0, @"message": @"Invalid URL generated"});
        return;
    }

    dispatch_async(dispatch_get_main_queue(), ^{
        if ([[UIApplication sharedApplication] canOpenURL:url]) {
            [[UIApplication sharedApplication] openURL:url options:@{} completionHandler:^(BOOL success) {
                if (success) {
                    resolve(@{
                        @"paymentStatus": @1,
                        @"txnId": transactionId,
                        @"txnRef": @"IOS_VERIFY_ON_SERVER",
                        @"responseCode": @"00",
                        @"message": @"SUCCESS"
                    });
                } else {
                    resolve(@{@"paymentStatus": @0, @"message": @"Failed to open UPI app"});
                }
            }];
        } else {
            resolve(@{@"paymentStatus": @0, @"message": @"UPI App not installed"});
        }
    });
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeAllinoneUpiSpecJSI>(params);
}

@end
