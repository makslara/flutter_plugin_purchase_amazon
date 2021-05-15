package com.example.amazon_purchase_plagin.amazon_purchase

/**
 * This is a sample SubscriptionRecord object that holds the InAppPurchase
 * Subscription details.
 *
 *
 */
class SubscriptionRecord {
    var amazonReceiptId: String? = null
    var from: Long = 0
    var to = TO_DATE_NOT_SET.toLong()
    var amazonUserId: String? = null
    var sku: String? = null
    val isActiveNow: Boolean
        get() = TO_DATE_NOT_SET.toLong() == to

    fun isActiveForDate(date: Long): Boolean {
        return date >= from && (isActiveNow || date <= to)
    }

    companion object {
        var TO_DATE_NOT_SET = -1
    }
}