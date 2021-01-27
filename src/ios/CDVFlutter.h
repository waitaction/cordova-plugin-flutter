#import <Cordova/CDVPlugin.h>
@import Flutter;

@interface CDVFlutter : CDVPlugin

-(void)init:(CDVInvokedUrlCommand*)command;
-(void)open:(CDVInvokedUrlCommand*)command;
-(NSString*)DataTOjsonString:(id)object;

@property (nonatomic,strong) FlutterEngine *flutterEngine;

@end
