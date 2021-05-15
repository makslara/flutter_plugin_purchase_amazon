package com.example.amazon_purchase_plagin.amazon_purchase

import android.content.Context
import android.util.Log
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.*
import io.flutter.plugin.common.EventChannel

/**
 * This is a sample of how an application may handle InAppPurchasing. The major
 * functions includes
 *
 *  * Simple user and subscription history management
 *  * Grant subscription purchases
 *  * Enable/disable subscribe from GUI
 *  * Save persistent subscriptions data into SQLite database
 *
 *
 *
 */
class SampleIapManager(context: Context?, private val eventSink: EventChannel.EventSink?) {
    private val dataSource: SubscriptionDataSource
    var isMagazineSubsAvailable = false
    var userIapData: UserIapData? = null
        private set

    /**
     * Method to set the app's amazon user id and marketplace from IAP SDK
     * responses.
     *
     * @param newAmazonUserId
     * @param newAmazonMarketplace
     */
    fun setAmazonUserId(newAmazonUserId: String?, newAmazonMarketplace: String?) {
        // Reload everything if the Amazon user has changed.
        if (newAmazonUserId == null) {
            // A null user id typically means there is no registered Amazon
            // account.
            if (userIapData != null) {
                userIapData = null
                refreshMagazineSubsAvailability()
            }
        } else if (userIapData == null || newAmazonUserId != userIapData!!.amazonUserId) {
            // If there was no existing Amazon user then either no customer was
            // previously registered or the application has just started.

            // If the user id does not match then another Amazon user has
            // registered.
            userIapData = newAmazonMarketplace?.let { UserIapData(newAmazonUserId, it) }
            refreshMagazineSubsAvailability()
        }
    }

    /**
     * Enable the magazine subscription.
     *
     * @param productData
     */
    fun enablePurchaseForSkus(productData: Map<String?, Product?>) {
        if (productData.containsKey(MySku.MY_MAGAZINE_SUBS.sku)) {
            isMagazineSubsAvailable = true
        }
    }

    /**
     * Disable the magazine subscription.
     *
     * @param unavailableSkus
     */
    fun errorHandler(errorStatus: String) {
        eventSink?.error(errorStatus, errorStatus, "error purchase")
    }

    fun disablePurchaseForSkus(unavailableSkus: Set<String?>) {

        if (unavailableSkus.contains(MySku.MY_MAGAZINE_SUBS.toString())) {
            isMagazineSubsAvailable = false
            // reasons for product not available can be:
            // * Item not available for this country
            // * Item pulled off from Appstore by developer
            // * Item pulled off from Appstore by Amazon
        }
    }

    /**
     * This method contains the business logic to fulfill the customer's
     * purchase based on the receipt received from InAppPurchase SDK's
     * [PurchasingListener.onPurchaseResponse] or
     * [PurchasingListener.onPurchaseUpdates] method.
     *
     *
     * @param requestId
     * @param receiptId
     */
    fun handleSubscriptionPurchase(receipt: Receipt, userData: UserData) {
        eventSink?.success(userData.toJSON().toString() + receipt.toJSON().toString())
        try {
            if (receipt.isCanceled) {
                // Check whether this receipt is for an expired or canceled
                // subscription
                revokeSubscription(receipt, userData.userId)
            } else {
                // We strongly recommend that you verify the receipt on
                // server-side.
                if (!verifyReceiptFromYourService(receipt.receiptId, userData)) {
                    // if the purchase cannot be verified,
                    // show relevant error message to the customer.
                    return
                }
                grantSubscriptionPurchase(receipt, userData)
            }
            return
        } catch (e: Throwable) {
        }
    }

    private fun grantSubscriptionPurchase(receipt: Receipt, userData: UserData) {
        eventSink?.success(userData.toJSON().toString() + receipt.toJSON().toString())
        val mySku = MySku.fromSku(receipt.sku, userIapData!!.amazonMarketplace)
        // Verify that the SKU is still applicable.
        if (mySku != MySku.MY_MAGAZINE_SUBS) {
            Log.w(TAG, "The SKU [" + receipt.sku + "] in the receipt is not valid anymore ")
            // if the sku is not applicable anymore, call
            // PurchasingService.notifyFulfillment with status "UNAVAILABLE"
            PurchasingService.notifyFulfillment(receipt.receiptId, FulfillmentResult.UNAVAILABLE)
            return
        }
        try {
            // Set the purchase status to fulfilled for your application
            saveSubscriptionRecord(receipt, userData.userId)
            PurchasingService.notifyFulfillment(receipt.receiptId, FulfillmentResult.FULFILLED)
        } catch (e: Throwable) {
            // If for any reason the app is not able to fulfill the purchase,
            // add your own error handling code here.
            Log.e(TAG, "Failed to grant entitlement purchase, with error " + e.message)
        }
    }

    /**
     * Method to handle receipt
     *
     * @param requestId
     * @param receipt
     * @param userData
     */
    fun handleReceipt(requestId: String?, receipt: Receipt, userData: UserData) {
        eventSink?.success(userData.toJSON().toString() + receipt.toJSON().toString())
        when (receipt.productType) {
            ProductType.CONSUMABLE -> {
            }
            ProductType.ENTITLED -> {
            }
            ProductType.SUBSCRIPTION -> handleSubscriptionPurchase(receipt, userData)
        }
    }

    /**
     * Show purchase failed message
     * @param sku
     */
    fun purchaseFailed(sku: String?) {
        eventSink?.success("'Failed'")
    }

    /**
     * Disable all magezine subscriptions on UI
     */
    fun disableAllPurchases() {
        isMagazineSubsAvailable = false
        refreshMagazineSubsAvailability()
    }

    /**
     * Reload the magazine subscription availability
     */
    fun refreshMagazineSubsAvailability() {
        val available = isMagazineSubsAvailable && userIapData != null
        /* mainActivity.setMagazineSubsAvail(available,
                                          userIapData != null && !userIapData.isSubsActiveCurrently());*/
    }

    /**
     * Gracefully close the database when the main activity's onStop and
     * onDestroy
     *
     */
    fun deactivate() {
        dataSource.close()
    }

    /**
     * Connect to the database when main activity's onStart and onResume
     */
    fun activate() {
        dataSource.open()
    }

    /**
     * Reload the subscription history from database
     */
    fun reloadSubscriptionStatus() {
        val subsRecords = dataSource.getSubscriptionRecords(userIapData!!.amazonUserId)
        userIapData!!.subscriptionRecords = subsRecords
        userIapData!!.reloadSubscriptionStatus()
        refreshMagazineSubsAvailability()
    }

    /**
     *
     * This sample app includes a simple SQLite implementation for save
     * subscription purchase detail locally.
     *
     * We strongly recommend that you save the purchase information on a server.
     *
     *
     *
     * @param receipt
     * @param userId
     */
    private fun saveSubscriptionRecord(receipt: Receipt, userId: String) {
        // TODO replace with your own implementation
        dataSource
                .insertOrUpdateSubscriptionRecord(receipt.receiptId,
                        userId,
                        receipt.purchaseDate.time,
                        if (receipt.cancelDate == null) SubscriptionRecord.TO_DATE_NOT_SET.toLong() else receipt.cancelDate.time,
                        receipt.sku)
    }

    /**
     * We strongly recommend verifying the receipt on your own server side
     * first. The server side verification ideally should include checking with
     * Amazon RVS (Receipt Verification Service) to verify the receipt details.
     *
     * @see [Appstore's Receipt Verification Service](https://developer.amazon.com/appsandservices/apis/earn/in-app-purchasing/docs/rvs)
     *
     *
     * @param receiptId
     * @return
     */
    private fun verifyReceiptFromYourService(receiptId: String, userData: UserData): Boolean {
        // TODO Add your own server side accessing and verification code
        return true
    }

    /**
     * Private method to revoke a subscription purchase from the customer
     *
     * Please implement your application-specific logic to handle the revocation
     * of a subscription purchase.
     *
     *
     * @param receipt
     * @param userId
     */
    private fun revokeSubscription(receipt: Receipt, userId: String) {
        val receiptId = receipt.receiptId
        dataSource.cancelSubscription(receiptId, receipt.cancelDate.time)
    }

    companion object {
        private const val TAG = "SampleIAPManager"
    }

    init {
        dataSource = SubscriptionDataSource(context)
    }
}