import 'dart:convert';

import 'package:meta/meta.dart';

class ProductResponse {
  ProductResponse({
    @required this.purchaseService,
    @required this.data,
  });

  final String purchaseService;
  final List<Product> data;

  factory ProductResponse.fromJson(String str) => ProductResponse.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory ProductResponse.fromMap(Map<String, dynamic> json) => ProductResponse(
        purchaseService: json["purchaseService"] == null ? null : json["purchaseService"],
        data: json["data"] == null
            ? null
            : List<Product>.from(json["data"].map((x) => Product.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "purchaseService": purchaseService == null ? null : purchaseService,
        "data": data == null ? null : List<dynamic>.from(data.map((x) => x.toMap())),
      };
}

class Product {
  Product({
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

  factory Product.fromJson(String str) => Product.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory Product.fromMap(Map<String, dynamic> json) => Product(
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
