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
    streamSubscription = AmazonPurchasePlugin.channelStream.listen((event) {
      setState(() {
        _platformVersion = event.toString();
      });
      print(event);
    }, onError: ((handleError) {
      setState(() {
        _platformVersion = handleError.toString();
      });
      print(handleError);
    }));
    AmazonPurchasePlugin.setup();
    AmazonPurchasePlugin.buySubscription(
            sku: 'com.locals.testsubscription', marketPlace: 'BklklkhY')
        .then((requestId) {
      print(requestId);
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
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
