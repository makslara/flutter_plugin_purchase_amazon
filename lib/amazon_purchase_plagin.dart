import 'dart:async';

import 'package:flutter/services.dart';

class AmazonPurchasePlugin {
  static const MethodChannel _channel = const MethodChannel('amazon_purchase_channel');
  static const EventChannel _eventChannel = const EventChannel('event_channel');

  static Stream<dynamic> get channelStream => _eventChannel.receiveBroadcastStream();

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void setup() async {
    var result = await _channel.invokeMethod('setup');
    print(result);
  }

  static void buySubscription() async {
    var result = await _channel.invokeMethod('buy');
    print(result);
  }
}
