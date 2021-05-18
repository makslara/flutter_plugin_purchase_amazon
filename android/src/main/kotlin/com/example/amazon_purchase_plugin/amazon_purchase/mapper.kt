package com.example.amazon_purchase_plugin.amazon_purchase

import android.util.JsonWriter
import com.amazon.device.iap.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

fun purchaseResponseMapper(userData: UserData, receipt: Receipt): String {
    val map = mapOf("userId" to userData.userId,
            "marketPlace" to userData.marketplace,
            "receiptId" to receipt.receiptId,
            "productType" to receipt.productType,
            "sku" to receipt.sku,
            "purchaseDate" to receipt.purchaseDate)
    val purchaseResponse = mapOf("purchaseService" to "PURCHASE", "data" to map)
    return JSONObject(purchaseResponse).toString()
}

fun productDataMapper(productDataResponse: ProductDataResponse): String {
    val productData:ArrayList<JSONObject> = ArrayList()
    if (productDataResponse.productData != null &&productDataResponse.productData.isNotEmpty()) {
        val iterator: Iterator<String> = productDataResponse.productData.keys.iterator()
        while (iterator.hasNext()) {
            val iteratorNext = iterator.next() as String
            productData.add((productDataResponse.productData[iteratorNext] as Product).toJSON())
        }
    }
    val map = mapOf("purchaseService" to "PRODUCT", "data" to JSONArray(productData))
    return  JSONObject(map).toString()
}
fun restorePurchaseMapper(restorePurchase: PurchaseUpdatesResponse): String {
    val productData:ArrayList<JSONObject> = ArrayList()
    if(!restorePurchase.receipts.isNullOrEmpty()){
        val iterator: Iterator<Receipt> = restorePurchase.receipts.iterator()
        while (iterator.hasNext()) {
            val iteratorNext = iterator.next() as Receipt
            productData.add(iteratorNext.toJSON());
        }
    }
    val map = mapOf("purchaseService" to "RESTORE", "data" to JSONArray(productData))
    return  JSONObject(map).toString()
}
fun getUserData(userData: UserDataResponse): String {
    val map = mapOf("purchaseService" to "USER_DATA", "data" to userData.toJSON())
    return  JSONObject(map).toString()
}

