
import 'dart:async';

import 'package:flutter/services.dart';

class AmazonPurchasePlagin {
  static const MethodChannel _channel =
      const MethodChannel('amazon_purchase_plagin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
