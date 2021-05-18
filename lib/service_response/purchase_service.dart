import 'dart:convert';

import 'package:flutter/cupertino.dart';

enum PurchaseServiceENUM { PURCHASE, PRODUCTS, RESTORE, USER_DATA }

extension PurchaseServiceExtention on PurchaseServiceENUM {
  String value() {
    switch (this) {
      case PurchaseServiceENUM.PURCHASE:
        return 'PURCHASE';
        break;
      case PurchaseServiceENUM.PRODUCTS:
        return 'PRODUCT';
        break;
      case PurchaseServiceENUM.RESTORE:
        return 'RESTORE';
        break;
      case PurchaseServiceENUM.USER_DATA:
        // TODO: Handle this case.
        break;
    }
  }
}

class PurchaseService {
  PurchaseService({
    @required this.purchaseService,
  });

  final String purchaseService;

  factory PurchaseService.fromJson(String str) => PurchaseService.fromMap(json.decode(str));

  factory PurchaseService.fromMap(Map<String, dynamic> json) => PurchaseService(
        purchaseService: json["purchaseService"] == null ? null : json["purchaseService"],
      );

  Map<String, dynamic> toMap() => {
        "purchaseService": purchaseService == null ? null : purchaseService,
      };
}
