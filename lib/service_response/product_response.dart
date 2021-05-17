import 'dart:convert';

import 'package:meta/meta.dart';

class ProductResponse {
  ProductResponse({
    @required this.purchaseService,
    @required this.data,
  });

  final String purchaseService;
  final Data data;

  factory ProductResponse.fromJson(String str) => ProductResponse.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory ProductResponse.fromMap(Map<String, dynamic> json) => ProductResponse(
        purchaseService: json["purchaseService"] == null ? null : json["purchaseService"],
        data: json["data"] == null ? null : Data.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "purchaseService": purchaseService == null ? null : purchaseService,
        "data": data == null ? null : data.toMap(),
      };
}

class Data {
  Data({
    @required this.requestId,
    @required this.unavailableSkus,
    @required this.requestStatus,
    @required this.productData,
  });

  final String requestId;
  final String unavailableSkus;
  final String requestStatus;
  final ProductData productData;

  factory Data.fromJson(String str) => Data.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory Data.fromMap(Map<String, dynamic> json) => Data(
        requestId: json["requestId"] == null ? null : json["requestId"],
        unavailableSkus: json["UNAVAILABLE_SKUS"] == null ? null : json["UNAVAILABLE_SKUS"],
        requestStatus: json["requestStatus"] == null ? null : json["requestStatus"],
        productData: json["productData"] == null ? null : ProductData.fromMap(json["productData"]),
      );

  Map<String, dynamic> toMap() => {
        "requestId": requestId == null ? null : requestId,
        "UNAVAILABLE_SKUS": unavailableSkus == null ? null : unavailableSkus,
        "requestStatus": requestStatus == null ? null : requestStatus,
        "productData": productData == null ? null : productData.toMap(),
      };
}

class ProductData {
  ProductData({
    @required this.comLocalsTestsubscription,
  });

  final ComLocalsTestsubscription comLocalsTestsubscription;

  factory ProductData.fromJson(String str) => ProductData.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory ProductData.fromMap(Map<String, dynamic> json) => ProductData(
        comLocalsTestsubscription: json["com.locals.testsubscription"] == null
            ? null
            : ComLocalsTestsubscription.fromMap(json["com.locals.testsubscription"]),
      );

  Map<String, dynamic> toMap() => {
        "com.locals.testsubscription":
            comLocalsTestsubscription == null ? null : comLocalsTestsubscription.toMap(),
      };
}

class ComLocalsTestsubscription {
  ComLocalsTestsubscription({
    @required this.sku,
    @required this.productType,
    @required this.description,
    @required this.price,
    @required this.smallIconUrl,
    @required this.title,
    @required this.coinsRewardAmount,
  });

  final String sku;
  final String productType;
  final String description;
  final String price;
  final String smallIconUrl;
  final String title;
  final int coinsRewardAmount;

  factory ComLocalsTestsubscription.fromJson(String str) =>
      ComLocalsTestsubscription.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory ComLocalsTestsubscription.fromMap(Map<String, dynamic> json) => ComLocalsTestsubscription(
        sku: json["sku"] == null ? null : json["sku"],
        productType: json["productType"] == null ? null : json["productType"],
        description: json["description"] == null ? null : json["description"],
        price: json["price"] == null ? null : json["price"],
        smallIconUrl: json["smallIconUrl"] == null ? null : json["smallIconUrl"],
        title: json["title"] == null ? null : json["title"],
        coinsRewardAmount: json["coinsRewardAmount"] == null ? null : json["coinsRewardAmount"],
      );

  Map<String, dynamic> toMap() => {
        "sku": sku == null ? null : sku,
        "productType": productType == null ? null : productType,
        "description": description == null ? null : description,
        "price": price == null ? null : price,
        "smallIconUrl": smallIconUrl == null ? null : smallIconUrl,
        "title": title == null ? null : title,
        "coinsRewardAmount": coinsRewardAmount == null ? null : coinsRewardAmount,
      };
}
