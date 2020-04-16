import 'dart:async';

import 'package:flutter/services.dart';

class NetworkType {
  static const MethodChannel _channel =
      const MethodChannel('network_type');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> get networkType async {
    final String version = await _channel.invokeMethod('networkType');
    return version;
  }
}
