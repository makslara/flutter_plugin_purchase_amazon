import 'dart:async';

import 'package:amazon_purchase_plugin/sku_object.dart';
import 'package:flutter/cupertino.dart';
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

  ///returned requestId
  static Future<String> buySubscription({@required String sku, String marketPlace}) async {
    var requestId = await _channel.invokeMethod(
        'buy', SkuObject(sku: sku, marketPlace: marketPlace).toJson().toString());
    print(requestId);
    return (requestId as String);
  }
}
