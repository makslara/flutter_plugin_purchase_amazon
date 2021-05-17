package com.example.amazon_purchase_plugin.amazon_purchase

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