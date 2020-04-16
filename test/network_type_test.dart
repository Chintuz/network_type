import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:network_type/network_type.dart';

void main() {
  const MethodChannel channel = MethodChannel('network_type');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await NetworkType.platformVersion, '42');
  });
}
