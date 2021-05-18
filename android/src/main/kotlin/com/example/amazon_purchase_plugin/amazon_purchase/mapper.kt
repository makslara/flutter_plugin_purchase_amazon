package com.example.amazon_purchase_plugin.amazon_purchase

import com.amazon.device.iap.model.Product
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.Receipt
import com.amazon.device.iap.model.UserData
import org.json.JSONObject

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
    if (productDataResponse.productData != null &&productDataResponse.productData.isNotEmpty()) {
        val iterator: Iterator<String> = productDataResponse.productData.keys.iterator()
        while (iterator.hasNext()) {
            val iteratorNext = iterator.next() as String
            json.put(iteratorNext, (productDataResponse.productData[iteratorNext] as Product).toJSON())
        }
    }
    val map = mapOf("purchaseService" to "PRODUCT", "data" to json)
    return  JSONObject(map).toString()
}

