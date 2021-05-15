import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:amazon_purchase_plagin/amazon_purchase_plagin.dart';

void main() {
  const MethodChannel channel = MethodChannel('amazon_purchase_plagin');

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
    expect(await AmazonPurchasePlagin.platformVersion, '42');
  });
}
