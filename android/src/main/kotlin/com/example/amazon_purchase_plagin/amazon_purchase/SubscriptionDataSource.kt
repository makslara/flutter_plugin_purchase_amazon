package com.example.amazon_purchase_plagin.amazon_purchase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.util.*

/**
 * DAO class for sample purchase data
 *
 *
 */
class SubscriptionDataSource(context: Context?) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: SampleSQLiteHelper
    private val allColumns = arrayOf<String>(SampleSQLiteHelper.Companion.COLUMN_RECEIPT_ID, SampleSQLiteHelper.Companion.COLUMN_USER_ID,
            SampleSQLiteHelper.Companion.COLUMN_DATE_FROM, SampleSQLiteHelper.Companion.COLUMN_DATE_TO, SampleSQLiteHelper.Companion.COLUMN_SKU)

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    private fun cursorToSubscriptionRecord(cursor: Cursor): SubscriptionRecord {
        val subsRecord = SubscriptionRecord()
        subsRecord.amazonReceiptId = cursor.getString(cursor.getColumnIndex(SampleSQLiteHelper.Companion.COLUMN_RECEIPT_ID))
        subsRecord.amazonUserId = cursor.getString(cursor.getColumnIndex(SampleSQLiteHelper.Companion.COLUMN_USER_ID))
        subsRecord.from = cursor.getLong(cursor.getColumnIndex(SampleSQLiteHelper.Companion.COLUMN_DATE_FROM))
        subsRecord.to = cursor.getLong(cursor.getColumnIndex(SampleSQLiteHelper.Companion.COLUMN_DATE_TO))
        subsRecord.sku = cursor.getString(cursor.getColumnIndex(SampleSQLiteHelper.Companion.COLUMN_SKU))
        return subsRecord
    }

    /**
     * Return all subscription records for the user
     *
     * @param userId
     * user id used to verify the purchase record
     * @return
     */
    fun getSubscriptionRecords(userId: String): List<SubscriptionRecord> {
        Log.d(TAG, "getSubscriptionRecord: userId ($userId)")
        val where: String = SampleSQLiteHelper.Companion.COLUMN_USER_ID + " = ?"
        val cursor = database!!.query(SampleSQLiteHelper.Companion.TABLE_SUBSCRIPTIONS,
                allColumns,
                where, arrayOf(userId),
                null,
                null,
                null)
        cursor.moveToFirst()
        val results: MutableList<SubscriptionRecord> = ArrayList()
        while (!cursor.isAfterLast) {
            val subsRecord = cursorToSubscriptionRecord(cursor)
            results.add(subsRecord)
            cursor.moveToNext()
        }
        Log.d(TAG, "getSubscriptionRecord: found " + results.size + " records")
        cursor.close()
        return results
    }

    /**
     * Insert or update the subscription record by receiptId
     *
     * @param receiptId
     * The receipt id
     * @param userId
     * Amazon user id
     * @param dateFrom
     * Timestamp for subscription's valid from date
     * @param dateTo
     * Timestamp for subscription's valid to date. less than 1 means
     * cancel date not set, the subscription in active status.
     * @param sku
     * The sku
     */
    fun insertOrUpdateSubscriptionRecord(receiptId: String,
                                         userId: String,
                                         dateFrom: Long,
                                         dateTo: Long,
                                         sku: String?) {
        Log.d(TAG, "insertOrUpdateSubscriptionRecord: receiptId ($receiptId),userId ($userId)")
        val where: String = (SampleSQLiteHelper.Companion.COLUMN_RECEIPT_ID + " = ? and "
                + SampleSQLiteHelper.Companion.COLUMN_DATE_TO
                + " > 0")
        val cursor = database!!.query(SampleSQLiteHelper.Companion.TABLE_SUBSCRIPTIONS,
                allColumns,
                where, arrayOf(receiptId),
                null,
                null,
                null)
        val count = cursor.count
        cursor.close()
        if (count > 0) {
            // There are record with given receipt id and cancel_date>0 in the
            // table, this record should be final and cannot be overwritten
            // anymore.
            Log.w(TAG, "Record already in final state")
        } else {
            // Insert the record into database with CONFLICT_REPLACE flag.
            val values = ContentValues()
            values.put(SampleSQLiteHelper.Companion.COLUMN_RECEIPT_ID, receiptId)
            values.put(SampleSQLiteHelper.Companion.COLUMN_USER_ID, userId)
            values.put(SampleSQLiteHelper.Companion.COLUMN_DATE_FROM, dateFrom)
            values.put(SampleSQLiteHelper.Companion.COLUMN_DATE_TO, dateTo)
            values.put(SampleSQLiteHelper.Companion.COLUMN_SKU, sku)
            database!!.insertWithOnConflict(SampleSQLiteHelper.Companion.TABLE_SUBSCRIPTIONS,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE)
        }
    }

    /**
     * Cancel a subscription by set the cancel date for the subscription record
     *
     * @param receiptId
     * The receipt id
     * @param cancelDate
     * Timestamp for the cancel date
     * @return
     */
    fun cancelSubscription(receiptId: String, cancelDate: Long): Boolean {
        Log.d(TAG, "cancelSubscription: receiptId ($receiptId), cancelDate:($cancelDate)")
        val where: String = SampleSQLiteHelper.Companion.COLUMN_RECEIPT_ID + " = ?"
        val values = ContentValues()
        values.put(SampleSQLiteHelper.Companion.COLUMN_DATE_TO, cancelDate)
        val updated = database!!.update(SampleSQLiteHelper.Companion.TABLE_SUBSCRIPTIONS,
                values,
                where, arrayOf(receiptId))
        Log.d(TAG, "cancelSubscription: updated $updated")
        return updated > 0
    }

    companion object {
        private const val TAG = "SampleIAPManager"
    }

    init {
        dbHelper = SampleSQLiteHelper(context)
    }
}