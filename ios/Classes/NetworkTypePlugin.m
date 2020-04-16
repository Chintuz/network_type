#import "NetworkTypePlugin.h"
#if __has_include(<network_type/network_type-Swift.h>)
#import <network_type/network_type-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "network_type-Swift.h"
#endif

@implementation NetworkTypePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftNetworkTypePlugin registerWithRegistrar:registrar];
}
@end
