#import <Cordova/CDVPlugin.h>
@import Flutter;

@interface CDVFlutter : CDVPlugin

-(void)init:(CDVInvokedUrlCommand*)command;
-(void)open:(CDVInvokedUrlCommand*)command;
-(void)invokeCallback:(CDVInvokedUrlCommand *)command;

@property (nonatomic,strong) FlutterEngine *flutterEngine;

@end