import 'dart:convert';

import 'package:meta/meta.dart';

class RestorePurchaseResponse {
  RestorePurchaseResponse({
    @required this.purchaseService,
    @required this.data,
  });

  final String purchaseService;
  final List<RestorePurchaseData> data;

  factory RestorePurchaseResponse.fromJson(String str) =>
      RestorePurchaseResponse.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory RestorePurchaseResponse.fromMap(Map<String, dynamic> json) => RestorePurchaseResponse(
        purchaseService: json["purchaseService"] == null ? null : json["purchaseService"],
        data: json["data"] == null
            ? null
            : List<RestorePurchaseData>.from(
                json["data"].map((x) => RestorePurchaseData.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "purchaseService": purchaseService == null ? null : purchaseService,
        "data": data == null ? null : List<dynamic>.from(data.map((x) => x.toMap())),
      };
}

class RestorePurchaseData {
  RestorePurchaseData({
    @required this.receiptId,
    @required this.sku,
    @required this.itemType,
    @required this.purchaseDate,
    @required this.endDate,
  });

  final String receiptId;
  final String sku;
  final String itemType;
  final String purchaseDate;
  final String endDate;

  factory RestorePurchaseData.fromJson(String str) => RestorePurchaseData.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory RestorePurchaseData.fromMap(Map<String, dynamic> json) => RestorePurchaseData(
        receiptId: json["receiptId"] == null ? null : json["receiptId"],
        sku: json["sku"] == null ? null : json["sku"],
        itemType: json["itemType"] == null ? null : json["itemType"],
        purchaseDate: json["purchaseDate"] == null ? null : json["purchaseDate"],
        endDate: json["endDate"] == null ? null : json["endDate"],
      );

  Map<String, dynamic> toMap() => {
        "receiptId": receiptId == null ? null : receiptId,
        "sku": sku == null ? null : sku,
        "itemType": itemType == null ? null : itemType,
        "purchaseDate": purchaseDate == null ? null : purchaseDate,
        "endDate": endDate == null ? null : endDate,
      };
}
