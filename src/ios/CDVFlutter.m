#import "CDVFlutter.h"
#import "GeneratedPluginRegistrant.h"
#import <Flutter/Flutter.h>


@implementation CDVFlutter

-(void) pluginInitialize {
    NSLog(@"pluginInitialize");
}

-(void)init:(CDVInvokedUrlCommand *)command {
    
    CDVPluginResult* result= [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
    
    __weak CDVFlutter* weakSelf = self;
    if(nil == weakSelf.flutterEngine){
                  weakSelf.flutterEngine =[[FlutterEngine alloc] initWithName:@"my flutter engine"];
                 [weakSelf.flutterEngine runWithEntrypoint:nil];
                 [GeneratedPluginRegistrant registerWithRegistry:weakSelf.flutterEngine];
    }
    
    if(nil == self.methodDict){
        self.methodDict = [NSMutableDictionary dictionary];
    }
    
    [self.commandDelegate runInBackground:^{
        
        [weakSelf.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    }];
    
}

-(void)open:(CDVInvokedUrlCommand*)command {
    
    NSInteger dictCount = [command.arguments count];
    FlutterViewController *flutterViewController;
    
    //如果js端 传入路由参数
    if(dictCount>0){
        NSString *routerName = [command.arguments objectAtIndex:0];
            flutterViewController = [[FlutterViewController alloc] init];
        [flutterViewController setInitialRoute:routerName];
        [GeneratedPluginRegistrant registerWithRegistry:flutterViewController];
    }else{
        flutterViewController = [[FlutterViewController alloc] initWithEngine:self.flutterEngine nibName:nil bundle:nil];
    }
        
    FlutterMethodChannel *flutterChannel = [FlutterMethodChannel
                                           methodChannelWithName:@"app.channel.shared.cordova.data"
                                           binaryMessenger:flutterViewController
                                          ];
        
    [flutterChannel setMethodCallHandler:^(FlutterMethodCall* call, FlutterResult result) {
        CFUUIDRef uuid = CFUUIDCreate(NULL);
        CFStringRef uuidStr = CFUUIDCreateString(NULL, uuid);
        NSString *uuidMethod = [[NSString alloc] initWithFormat:@"%@#%@#",call.method,uuidStr];

        NSString *script=nil;
        if(nil != call.arguments){
            NSDictionary *paramsDict = call.arguments;
            NSString *JSONString = [self DataTOjsonString:paramsDict];
            script = [[NSString alloc] initWithFormat:@"window.bridgeFlutterInvoke('%@','%@',%@)",uuidStr,call.method,JSONString];
        }else{
            script = [[NSString alloc] initWithFormat:@"window.bridgeFlutterInvoke('%@','%@',%@)",uuidStr,call.method,nil];
        }
        
        [self.methodDict setObject:result forKey:uuidMethod];
        [self.webViewEngine evaluateJavaScript:script completionHandler:nil];
    }];
 
    [self.viewController presentViewController:flutterViewController animated:YES completion:nil];
    
    __weak CDVFlutter* weakSelf = self;
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* result= [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
        [weakSelf.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    }];
    
}

-(void)invokeCallback:(CDVInvokedUrlCommand *)command {
     NSDictionary *jsonObjDict = [command.arguments objectAtIndex:0];
     NSString *uuidMethod = [jsonObjDict objectForKey:@"uuid"];
     NSDictionary *valueDict = [jsonObjDict objectForKey:@"result"];

     __weak CDVFlutter* weakSelf = self;
     [self.commandDelegate runInBackground:^{
             
           FlutterResult flutterResult = [self.methodDict objectForKey:uuidMethod];
              
           NSString *JSONString = [self DataTOjsonString:valueDict];
           flutterResult(JSONString);
           [self.methodDict removeObjectForKey:uuidMethod];
         
           CDVPluginResult* result= [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
           [weakSelf.commandDelegate sendPluginResult:result callbackId:command.callbackId];
     }];
    
     CDVPluginResult* result= [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:0];
     [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

 
-(NSString*)DataTOjsonString:(id)object
{
    NSString *jsonString = nil;
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:object
                                                       options:0 //NSJSONWritingPrettyPrinted
                        //Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];
    if (! jsonData) {
        NSLog(@"Got an error: %@", error);
    } else {
        jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    }
    return jsonString;
}

 
@end
