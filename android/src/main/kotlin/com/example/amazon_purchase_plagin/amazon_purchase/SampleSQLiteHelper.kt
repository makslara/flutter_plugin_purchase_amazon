package com.example.amazon_purchase_plagin.amazon_purchase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Sample SQLiteHelper for the purchase record table
 *
 */
class SampleSQLiteHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log
                .w(SampleSQLiteHelper::class.java.name, "Upgrading database from version " + oldVersion
                        + " to "
                        + newVersion)
        // do nothing in the sample
    }

    companion object {
        //table name
        const val TABLE_SUBSCRIPTIONS = "subscriptions"

        //receipt id
        const val COLUMN_RECEIPT_ID = "receipt_id"

        //amazon user id
        const val COLUMN_USER_ID = "user_id"

        //subscription valid from date
        const val COLUMN_DATE_FROM = "date_from"

        //subscription valid to date
        const val COLUMN_DATE_TO = "date_to"

        //subscription sku
        const val COLUMN_SKU = "sku"
        private const val DATABASE_NAME = "subscriptions.db"
        private const val DATABASE_VERSION = 1

        // Database creation sql statement
        private const val DATABASE_CREATE = ("create table " + TABLE_SUBSCRIPTIONS
                + "("
                + COLUMN_RECEIPT_ID
                + " text primary key not null, "
                + COLUMN_USER_ID
                + " text not null, "
                + COLUMN_DATE_FROM
                + " integer not null, "
                + COLUMN_DATE_TO
                + " integer, "
                + COLUMN_SKU
                + " text not null"
                + ");")
    }
}