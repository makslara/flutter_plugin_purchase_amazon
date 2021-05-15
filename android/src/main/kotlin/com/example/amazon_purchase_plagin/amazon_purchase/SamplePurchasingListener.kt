package com.example.amazon_purchase_plagin.amazon_purchase

import android.util.Log
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import java.util.*

/**
 * Implementation of [PurchasingListener] that listens to Amazon
 * InAppPurchase SDK's events, and call [SampleIAPManager] to handle the
 * purchase business logic.
 */
class SamplePurchasingListener(private val iapManager: SampleIapManager) : PurchasingListener {
    /**
     * This is the callback for [PurchasingService.getUserData]. For
     * successful case, get the current user from [UserDataResponse] and
     * call [SampleIAPManager.setAmazonUserId] method to load the Amazon
     * user and related purchase information
     *
     * @param response
     */
    override fun onUserDataResponse(response: UserDataResponse) {
        Log.d(TAG, "onGetUserDataResponse: requestId (" + response.requestId
                + ") userIdRequestStatus: "
                + response.requestStatus
                + ")")
        val status = response.requestStatus
        when (status) {
            UserDataResponse.RequestStatus.SUCCESSFUL -> {
                Log.d(TAG, "onUserDataResponse: get user id (" + response.userData.userId
                        + ", marketplace ("
                        + response.userData.marketplace
                        + ") ")
                iapManager.setAmazonUserId(response.userData.userId, response.userData.marketplace)
            }
            UserDataResponse.RequestStatus.FAILED, UserDataResponse.RequestStatus.NOT_SUPPORTED -> {
                Log.d(TAG, "onUserDataResponse failed, status code is $status")
                iapManager.setAmazonUserId(null, null)
            }
        }
    }

    /**
     * This is the callback for [PurchasingService.getProductData]. After
     * SDK sends the product details and availability to this method, it will
     * call [SampleIAPManager.enablePurchaseForSkus]
     * [SampleIAPManager.disablePurchaseForSkus] or
     * [SampleIAPManager.disableAllPurchases] method to set the purchase
     * status accordingly.
     */
    override fun onProductDataResponse(response: ProductDataResponse) {
        val status = response.requestStatus
        Log.d(TAG, "onProductDataResponse: RequestStatus ($status)")
        when (status) {
            ProductDataResponse.RequestStatus.SUCCESSFUL -> {
                Log.d(TAG, "onProductDataResponse: successful.  The item data map in this response includes the valid SKUs")
                val unavailableSkus = response.unavailableSkus
                Log.d(TAG, "onProductDataResponse: " + unavailableSkus.size + " unavailable skus")
                iapManager.enablePurchaseForSkus(response.productData)
                iapManager.disablePurchaseForSkus(response.unavailableSkus)
                iapManager.refreshMagazineSubsAvailability()
            }
            ProductDataResponse.RequestStatus.FAILED, ProductDataResponse.RequestStatus.NOT_SUPPORTED -> {
                Log.d(TAG, "onProductDataResponse: failed, should retry request")
                iapManager.disableAllPurchases()
            }
        }
    }

    /**
     * This is the callback for [PurchasingService.getPurchaseUpdates].
     *
     * You will receive receipts for all possible Subscription history from this
     * callback
     *
     */
    override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
        Log.d(TAG, "onPurchaseUpdatesResponse: requestId (" + response.requestId
                + ") purchaseUpdatesResponseStatus ("
                + response.requestStatus
                + ") userId ("
                + response.userData.userId
                + ")")
        val status = response.requestStatus
        when (status) {
            PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL -> {
                iapManager.setAmazonUserId(response.userData.userId, response.userData.marketplace)
                for (receipt in response.receipts) {
                    iapManager.handleReceipt(response.requestId.toString(), receipt!!, response.userData)
                }
                if (response.hasMore()) {
                    PurchasingService.getPurchaseUpdates(false)
                }
                iapManager.reloadSubscriptionStatus()
            }
            PurchaseUpdatesResponse.RequestStatus.FAILED, PurchaseUpdatesResponse.RequestStatus.NOT_SUPPORTED -> {
                Log.d(TAG, "onProductDataResponse: failed, should retry request")
                iapManager.disableAllPurchases()
            }
        }
    }

    /**
     * This is the callback for [PurchasingService.purchase]. For each
     * time the application sends a purchase request
     * [PurchasingService.purchase], Amazon Appstore will call this
     * callback when the purchase request is completed. If the RequestStatus is
     * Successful or AlreadyPurchased then application needs to call
     * [SampleIAPManager.handleReceipt] to handle the purchase
     * fulfillment. If the RequestStatus is INVALID_SKU, NOT_SUPPORTED, or
     * FAILED, notify corresponding method of [SampleIAPManager] .
     */
    override fun onPurchaseResponse(response: PurchaseResponse) {
        val requestId = response.requestId.toString()
        val userId = response.userData.userId
        val status = response.requestStatus
        Log.d(TAG, "onPurchaseResponse: requestId (" + requestId
                + ") userId ("
                + userId
                + ") purchaseRequestStatus ("
                + status
                + ")")
        when (status) {
            PurchaseResponse.RequestStatus.SUCCESSFUL -> {
                val receipt = response.receipt
                Log.d(TAG, "onPurchaseResponse: receipt json:" + receipt.toJSON())
                iapManager.handleReceipt(response.requestId.toString(), receipt, response.userData)
                iapManager.reloadSubscriptionStatus()
            }
            PurchaseResponse.RequestStatus.ALREADY_PURCHASED -> {
                iapManager.errorHandler("onPurchaseResponse: already purchased, you should verify the subscription purchase on your side and make sure the purchase was granted to customer")
                Log.i(TAG,
                        "onPurchaseResponse: already purchased, you should verify the subscription purchase on your side and make sure the purchase was granted to customer")
            }
            PurchaseResponse.RequestStatus.INVALID_SKU -> {
                Log.d(TAG,
                        "onPurchaseResponse: invalid SKU!  onProductDataResponse should have disabled buy button already.")
                val unavailableSkus: MutableSet<String?> = HashSet()
                unavailableSkus.add(response.receipt.sku)
                iapManager.disablePurchaseForSkus(unavailableSkus)

            }
            PurchaseResponse.RequestStatus.FAILED, PurchaseResponse.RequestStatus.NOT_SUPPORTED -> {
                Log.d(TAG, "onPurchaseResponse: failed so remove purchase request from local storage")
                iapManager.purchaseFailed("FAILED")
            }
        }
    }

    companion object {
        private const val TAG = "SampleIAPSubscriptionApp"
    }
}