// To parse this JSON data, do
//
//     final purchaseResponse = purchaseResponseFromMap(jsonString);

import 'dart:convert';

import 'package:meta/meta.dart';

class PurchaseResponse {
  PurchaseResponse({
    @required this.purchaseService,
    @required this.data,
  });

  final String purchaseService;
  final PurchaseData data;

  factory PurchaseResponse.fromJson(String str) => PurchaseResponse.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory PurchaseResponse.fromMap(Map<String, dynamic> json) => PurchaseResponse(
        purchaseService: json["purchaseService"] == null ? null : json["purchaseService"],
        data: json["data"] == null ? null : PurchaseData.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "purchaseService": purchaseService == null ? null : purchaseService,
        "data": data == null ? null : data.toMap(),
      };
}

class PurchaseData {
  PurchaseData({
    @required this.userId,
    @required this.marketPlace,
    @required this.receiptId,
    @required this.productType,
    @required this.sku,
    @required this.purchaseDate,
  });

  final String userId;
  final String marketPlace;
  final String receiptId;
  final dynamic productType;
  final String sku;
  final String purchaseDate;

  factory PurchaseData.fromJson(String str) => PurchaseData.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory PurchaseData.fromMap(Map<String, dynamic> json) => PurchaseData(
        userId: json["userId"] == null ? null : json["userId"],
        marketPlace: json["marketPlace"] == null ? null : json["marketPlace"],
        receiptId: json["receiptId"] == null ? null : json["receiptId"],
        productType: json["productType"],
        sku: json["sku"] == null ? null : json["sku"],
        purchaseDate: json["purchaseDate"] == null ? null : json["purchaseDate"],
      );

  Map<String, dynamic> toMap() => {
        "userId": userId == null ? null : userId,
        "marketPlace": marketPlace == null ? null : marketPlace,
        "receiptId": receiptId == null ? null : receiptId,
        "productType": productType,
        "sku": sku == null ? null : sku,
        "purchaseDate": purchaseDate == null ? null : purchaseDate,
      };
}
