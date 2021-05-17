import 'dart:async';

import 'package:amazon_purchase_plugin/service_response/product_response.dart';
import 'package:amazon_purchase_plugin/service_response/purchase_response.dart';
import 'package:amazon_purchase_plugin/sku_object.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

import 'service_response/purchase_service.dart';

class AmazonPurchasePlugin {
  static const MethodChannel _channel = const MethodChannel('amazon_purchase_channel');
  static const EventChannel _eventChannel = const EventChannel('event_channel');
  StreamController<String> streamController = StreamController();
  static Stream<dynamic> _channelStream;

  static Stream<dynamic> get getChannelStream {
    if (_channelStream == null)
      _channelStream = _eventChannel.receiveBroadcastStream().map((event) {
        PurchaseService purchaseService = PurchaseService.fromJson(event);
        if (purchaseService.purchaseService == PurchaseServiceENUM.PURCHASE.value()) {
          PurchaseResponse purchaseResponse = PurchaseResponse.fromJson(event);
          print(purchaseResponse);
        } else if (purchaseService.purchaseService == PurchaseServiceENUM.PRODUCTS.value()) {
          ProductResponse productResponse = ProductResponse.fromJson(event);
          print(productResponse);
        }
        //return purchaseResponse;
      });
    return _channelStream;
  }

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

  static Future<String> getProduct({@required String sku, String marketPlace}) async {
    var requestId = await _channel.invokeMethod(
        'getProduct', SkuObject(sku: sku, marketPlace: marketPlace).toJson().toString());
    return (requestId as String);
  }

  void setupStream() {}

  dispose() {
    if (streamController != null) streamController.close();
    streamController = null;
  }
}
