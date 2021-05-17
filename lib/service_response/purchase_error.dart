class PurchaseError implements Exception {
  final String code;
  final String msg;
  final String details;
  PurchaseError(this.code, this.msg, this.details);
}
