#import <Cordova/CDVPlugin.h>

@interface CDVFlutter : CDVPlugin

-(void)init:(CDVInvokedUrlCommand*)command;
-(void)open:(CDVInvokedUrlCommand*)command;
-(void)invokeCallback:(CDVInvokedUrlCommand *)command;
@end