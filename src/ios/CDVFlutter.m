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
    
    [self.commandDelegate runInBackground:^{
        [weakSelf.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    }];
    
}

-(void)open:(CDVInvokedUrlCommand*)command {
    
    NSInteger dictCount = [command.arguments count];
    FlutterViewController *flutterViewController;
    if(dictCount>0){
        NSString *routerName = [command.arguments objectAtIndex:0];
            flutterViewController = [[FlutterViewController alloc] init];
        [flutterViewController setInitialRoute:routerName];
        [GeneratedPluginRegistrant registerWithRegistry:flutterViewController];
    }else{
        flutterViewController = [[FlutterViewController alloc] initWithEngine:self.flutterEngine nibName:nil bundle:nil];
    }
    __weak CDVFlutter* weakSelf = self;
    FlutterMethodChannel *flutterChannel = [FlutterMethodChannel
                                           methodChannelWithName:@"app.channel.shared.cordova.data" binaryMessenger:flutterViewController ];
        
    [flutterChannel setMethodCallHandler:^(FlutterMethodCall* call, FlutterResult result) {
        NSDictionary *paramsDict = call.arguments;
        NSString *JSONString = [self DataTOjsonString:paramsDict];
        if ([[call.arguments method] isEqual:@"finish"]){
            [self.commandDelegate runInBackground:^{
                CDVPluginResult* res= [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:JSONString];
                [weakSelf.commandDelegate sendPluginResult:res callbackId:command.callbackId ];
            }];
          
        }
    }];
 
    [self.viewController presentViewController:flutterViewController animated:YES completion:nil];
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
