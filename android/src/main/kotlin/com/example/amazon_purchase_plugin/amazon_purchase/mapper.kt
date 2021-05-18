package com.example.amazon_purchase_plugin.amazon_purchase

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
    val json = JSONObject()
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
fun restorePurchaseMapper(productDataResponse: PurchaseUpdatesResponse): String {
    val json = productDataResponse.toJSON().toString()
    val map = mapOf("purchaseService" to "RESTORE", "data" to json)
    return  JSONObject(map).toString()
}

