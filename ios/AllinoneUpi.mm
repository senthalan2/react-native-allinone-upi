#import "AllinoneUpi.h"

#import "AllinoneUpi-Swift.h"

@implementation AllinoneUpi

RCT_EXPORT_MODULE()

// 1. Get Installed Apps
- (void)getInstalledUpiApps:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    [[AllinoneUpiSwift new] getInstalledUpiAppsWithResolve:resolve reject:reject];
}

// 2. Generate UPI String
- (NSString *)generateUpiString:(NSString *)upiId 
                  transactionId:(NSString *)transactionId 
                       currency:(NSString *)currency 
           merchantCategoryCode:(NSString *)merchantCategoryCode 
                      payeeName:(NSString *)payeeName 
                         amount:(NSString *)amount 
                           note:(NSString *)note {
    return [[AllinoneUpiSwift new] generateUpiStringWithUpiId:upiId 
                                                transactionId:transactionId 
                                                     currency:currency 
                                         merchantCategoryCode:merchantCategoryCode 
                                                    payeeName:payeeName 
                                                       amount:amount 
                                                         note:note];
}

// 3. Initiate Transaction
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
    
    [[AllinoneUpiSwift new] initiateTransactionWithUpiId:upiId 
                                           transactionId:transactionId 
                                                currency:currency 
                                    merchantCategoryCode:merchantCategoryCode 
                                               payeeName:payeeName 
                                                  amount:amount 
                                                    note:note 
                                             appSpecific:appSpecific 
                                                 resolve:resolve 
                                                  reject:reject];
}

// Turbo Module Spec Registration
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeAllinoneUpiSpecJSI>(params);
}

@end
