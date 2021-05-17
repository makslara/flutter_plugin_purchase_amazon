import 'dart:async';

import 'package:amazon_purchase_plugin/amazon_purchase_plugin.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  StreamSubscription streamSubscription;

  @override
  void initState() {
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      AmazonPurchasePlugin.setup();
      AmazonPurchasePlugin.buySubscription(sku: 'com.locals.testsubscription', marketPlace: 'US')
          .then((requestId) {
        print(requestId);
      });
      AmazonPurchasePlugin.getProduct(sku: 'com.locals.testsubscription', marketPlace: 'US')
          .then((value) => print(value));
    });

    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await AmazonPurchasePlugin.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
  }

  @override
  void dispose() {
    streamSubscription?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: StreamBuilder<dynamic>(
              stream: AmazonPurchasePlugin.getChannelStream,
              builder: (context, snapshot) {
                if (snapshot.hasData)
                  return Text('Data: ${snapshot.data}');
                else if (snapshot.hasError) {
                  return Text('Data: ${(snapshot.error as PlatformException).code},\n'
                      '${(snapshot.error as PlatformException).message},\n'
                      '${(snapshot.error as PlatformException).details}');
                } else
                  return Container(
                    height: 30,
                    width: 30,
                    child: CircularProgressIndicator(),
                  );
              }),
        ),
      ),
    );
  }
}
