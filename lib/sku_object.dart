import 'dart:convert';

import 'package:meta/meta.dart';

class SkuObject {
  const SkuObject({
    @required this.sku,
    @required this.marketPlace,
  })  : assert(sku != null),
        assert(marketPlace != null);

  final String sku;
  final String marketPlace;

  String toJson() {
    print(json.encode(toMap()));
    return json.encode(toMap());
  }

  Map<String, dynamic> toMap() => {
        "sku": sku,
        "marketPlace": marketPlace,
      };
}
