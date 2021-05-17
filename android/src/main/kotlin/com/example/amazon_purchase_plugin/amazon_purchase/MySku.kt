package com.example.amazon_purchase_plugin.amazon_purchase

/**
 *
 * MySku enum contains all In App Purchase products definition that the sample
 * app will use. The product definition includes two properties: "SKU" and
 * "Available Marketplace".
 *
 */
enum class MySku(
        /**
         * Returns the Sku string of the MySku object
         * @return
         */
        val sku: String,
        /**
         * Returns the Available Marketplace of the MySku object
         * @return
         */
        val availableMarketplace: String) {
    //The only subscription product used in this sample app
    MY_MAGAZINE_SUBS("com.locals.testsubscription", "US");

    companion object {
        /**
         * Returns the MySku object from the specified Sku and marketplace value.
         * @param sku
         * @param marketplace
         * @return
         */
        fun fromSku(sku: String): MySku? {
            return if (MY_MAGAZINE_SUBS.sku == sku) {
                MY_MAGAZINE_SUBS
            } else null
        }
    }
}