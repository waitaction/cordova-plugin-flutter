#import <Cordova/CDVPlugin.h>
@import Flutter;

@interface CDVFlutter : CDVPlugin

-(void)init:(CDVInvokedUrlCommand*)command;
-(void)open:(CDVInvokedUrlCommand*)command;
-(void)invokeCallback:(CDVInvokedUrlCommand *)command;
-(NSString*)DataTOjsonString:(id)object;

@property (nonatomic,strong) FlutterEngine *flutterEngine;
@property (nonatomic,strong) NSMutableDictionary *methodDict;

@end
