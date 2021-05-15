package com.example.amazon_purchase_plagin

import android.content.Context
import android.util.JsonReader
import android.util.Log
import androidx.annotation.NonNull
import com.amazon.device.iap.PurchasingService
import com.example.amazon_purchase_plagin.amazon_purchase.MySku
import com.example.amazon_purchase_plagin.amazon_purchase.SampleIapManager
import com.example.amazon_purchase_plagin.amazon_purchase.SamplePurchasingListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONObject

/** AmazonPurchasePlaginPlugin */
class AmazonPurchasePlaginPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var context: Context
    private var eventSink: EventChannel.EventSink? = null
    private var sampleIapManager: SampleIapManager? = null

    // ///////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////// Application specific code below
    // ////////////////////////////
    // ///////////////////////////////////////////////////////////////////////////////////////
    private val TAG = "AMAZON-PURCHASE"

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "event_channel")
        eventChannel.setStreamHandler(this)
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "amazon_purchase_channel")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "setup" -> {
                sampleIapManager = SampleIapManager(context,eventSink);
                val purchasingListener = SamplePurchasingListener(sampleIapManager!!)
                Log.d(TAG, "onCreate: registering PurchasingListener")
                PurchasingService.registerListener(context, purchasingListener)
                result.success(true)
            }
            "buy" -> {
                val json: JSONObject = JSONObject(call.arguments.toString())
                val sku: String = json.getString("sku")
                val requestId = PurchasingService.purchase(sku)
                result.success(requestId.toString())
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events;
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }
}
