import 'dart:convert';

import 'package:meta/meta.dart';

class UserDataResponse {
  UserDataResponse({
    @required this.purchaseService,
    @required this.data,
  });

  final String purchaseService;
  final UserDataInfo data;

  factory UserDataResponse.fromJson(String str) => UserDataResponse.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory UserDataResponse.fromMap(Map<String, dynamic> json) => UserDataResponse(
        purchaseService: json["purchaseService"] == null ? null : json["purchaseService"],
        data: json["data"] == null ? null : UserDataInfo.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "purchaseService": purchaseService == null ? null : purchaseService,
        "data": data == null ? null : data.toMap(),
      };
}

class UserDataInfo {
  UserDataInfo({
    @required this.requestId,
    @required this.requestStatus,
    @required this.userData,
  });

  final String requestId;
  final String requestStatus;
  final UserData userData;

  factory UserDataInfo.fromJson(String str) => UserDataInfo.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory UserDataInfo.fromMap(Map<String, dynamic> json) => UserDataInfo(
        requestId: json["REQUEST_ID"] == null ? null : json["REQUEST_ID"],
        requestStatus: json["REQUEST_STATUS"] == null ? null : json["REQUEST_STATUS"],
        userData: json["USER_DATA"] == null ? null : UserData.fromMap(json["USER_DATA"]),
      );

  Map<String, dynamic> toMap() => {
        "REQUEST_ID": requestId == null ? null : requestId,
        "REQUEST_STATUS": requestStatus == null ? null : requestStatus,
        "USER_DATA": userData == null ? null : userData.toMap(),
      };
}

class UserData {
  UserData({
    @required this.userId,
    @required this.marketplace,
  });

  final String userId;
  final String marketplace;

  factory UserData.fromJson(String str) => UserData.fromMap(json.decode(str));

  String toJson() => json.encode(toMap());

  factory UserData.fromMap(Map<String, dynamic> json) => UserData(
        userId: json["userId"] == null ? null : json["userId"],
        marketplace: json["marketplace"] == null ? null : json["marketplace"],
      );

  Map<String, dynamic> toMap() => {
        "userId": userId == null ? null : userId,
        "marketplace": marketplace == null ? null : marketplace,
      };
}
