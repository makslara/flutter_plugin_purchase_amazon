import 'dart:async';

class PurchaseStream<T> {
  StreamController<T> _streamController = StreamController();
  Stream get purchaseStream => _streamController.stream;
  add(T) {
    _streamController.add(T);
  }

  close() {
    _streamController.close();
  }
}
