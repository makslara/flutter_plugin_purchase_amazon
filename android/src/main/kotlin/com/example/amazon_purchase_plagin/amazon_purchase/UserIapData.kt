package com.example.amazon_purchase_plagin.amazon_purchase

/**
 * This is a simple example used in Amazon InAppPurchase Sample App, to show how
 * developer's application holding the customer's InAppPurchase data.
 *
 *
 */
class UserIapData(val amazonUserId: String, val amazonMarketplace: String) {
    var subscriptionRecords: List<SubscriptionRecord>? = null
    var isSubsActiveCurrently = false
        private set
    var currentSubsFrom: Long = 0
        private set

    /**
     * Reload current subscription status from SubscriptionRecords
     */
    fun reloadSubscriptionStatus() {
        isSubsActiveCurrently = false
        currentSubsFrom = 0
        for (record in subscriptionRecords!!) {
            if (record.isActiveNow) {
                isSubsActiveCurrently = true
                currentSubsFrom = record.from
                return
            }
        }
    }
}