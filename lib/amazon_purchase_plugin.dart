import 'dart:async';

import 'package:amazon_purchase_plugin/service_response/product_response.dart';
import 'package:amazon_purchase_plugin/service_response/purchase_response.dart';
import 'package:amazon_purchase_plugin/service_response/restore_purchase_response.dart';
import 'package:amazon_purchase_plugin/service_response/user_data_response.dart';
import 'package:amazon_purchase_plugin/sku_object.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

import 'service_response/purchase_service.dart';

class AmazonPurchasePlugin {
  static const MethodChannel _channel = const MethodChannel('amazon_purchase_channel');
  static const EventChannel _eventChannel = const EventChannel('event_channel');
  static Stream<dynamic> _channelStream;

  StreamController<String> _purchaseController = StreamController();
  StreamController<String> _productsController = StreamController();

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
        } else if (purchaseService.purchaseService == PurchaseServiceENUM.RESTORE.value()) {
          RestorePurchaseResponse productResponse = RestorePurchaseResponse.fromJson(event);
          print(productResponse);
        } else if (purchaseService.purchaseService == PurchaseServiceENUM.USER_DATA.value()) {
          UserDataResponse userDataResponse = UserDataResponse.fromJson(event);
          print(userDataResponse);
        }
        //return purchaseResponse;
      }).handleError((error) {});
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

  ///returned requestId
  static Future<String> getProduct({@required String sku, String marketPlace}) async {
    var requestId = await _channel.invokeMethod(
        'getProduct', SkuObject(sku: sku, marketPlace: marketPlace).toJson().toString());
    return (requestId as String);
  }

  ///returned requestId
  static Future<String> restorePurchase({bool reset}) async {
    var requestId = await _channel.invokeMethod('restorePurchase', reset);
    return (requestId as String);
  }

  ///returned requestId
  static Future<String> getUserData() async {
    var requestId = await _channel.invokeMethod('userData');
    return (requestId as String);
  }

  void setupStream() {}

  dispose() {
    if (_purchaseController != null) _purchaseController.close();
    _purchaseController = null;
  }
}
