#import "CDVFlutter.h"
@import <FlutterPluginRegistrant/GeneratedPluginRegistrant.h>

@implementation CDVFlutter

-(void) pluginInitialize {
    //NSString* appKey = [[self.commandDelegate settings] objectForKey:@"appkey"];
    //[[BaiduMobStat defaultStat] startWithAppId:appKey]; 
}

-(void)init:(CDVInvokedUrlCommand *)command {
    self.flutterEngine =[[FlutterEngine alloc] initWithName:@"my flutter engine"];
    [self.flutterEngine run];
    [GeneratedPluginRegistrant registerWithRegistry:self.flutterEngine];
    // CDVPluginResult* result= nil;
    // NSArray* args=command.arguments;
    
    // if (args.count != 2) {
    //     result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"please pass event and label"];
    // }
    // else {
    //     [[BaiduMobStat defaultStat] logEvent:[command argumentAtIndex:0] eventLabel:[command argumentAtIndex:1]];
    //     result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"success!"];
    // }
    
    // [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)open:(CDVInvokedUrlCommand*)command {
    // CDVPluginResult* result= nil;
    // NSArray* args=command.arguments;
    
    // if (args.count != 3) {
    //     result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"please pass event and label with attributes"];
    // }
    // else {
    //     [[BaiduMobStat defaultStat] logEvent:[command argumentAtIndex:0] eventLabel:[command argumentAtIndex:1] attributes:[command argumentAtIndex:2]];
    //     result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"success!"];
    // }
    
    // [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

-(void)invokeCallback:(CDVInvokedUrlCommand *)command {
    // CDVPluginResult* result= nil;
    // NSArray* args=command.arguments;
    
    // if (args.count != 3) {
    //     result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"please pass event, label and duration"];
    // }
    // else {
    //     [[BaiduMobStat defaultStat] logEventWithDurationTime:[command argumentAtIndex:0] eventLabel:[command argumentAtIndex:1] durationTime:(unsigned long)[command argumentAtIndex:2 withDefault:0]];
    //     result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"success!"];
    // }
    
    // [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
    
}

 

 
@end