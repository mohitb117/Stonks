package com.mohitb117.stonks.datamodels

import android.graphics.Color
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import kotlin.random.Random

/**
 * View specific data object to display portfolio.
 */
@Parcelize
data class Portfolio(
    val stocks: List<Stock>
) : Parcelable {
    @IgnoredOnParcel
    private val portfolioSum = stocks.sumOf { it.currentPortfolioValue }

    init {
        stocks.forEach {
            it.ratio = it.currentPortfolioValue * 100F / portfolioSum
        }
    }

    fun portfolioDetails(formatter: NumberFormat): CharSequence {
        val portfolioSumFormatted = formatter.format(portfolioSum)
        return "Portfolio Details: ${stocks.size} Stocks and $portfolioSumFormatted in stocks"
    }
}

@Entity(tableName = TABLE_NAME)
@Parcelize
data class Stock(
    val currency: String,
    val current_price_cents: Int?,
    val current_price_timestamp: Int,
    val name: String,
    val quantity: Int?,
    @PrimaryKey val ticker: String,
    var ratio: Float = 0.0F
) : Parcelable, Comparable<Stock> {

    @IgnoredOnParcel
    val currentPriceDollars: Int
        get() {
            return current_price_cents?.let { current_price_cents / 100 } ?: 0
        }

    @IgnoredOnParcel
    val currentPortfolioValue: Int
        get() {
            val quantity = this.quantity ?: 0
            return quantity * currentPriceDollars
        }

    override fun compareTo(other: Stock): Int {
        return if (ticker == other.ticker) 0
        else if (ticker.first() < other.ticker.first()) +1
        else -1
    }

    /**
     * Expensive Color generation based on a stock.
     */
    fun generateColor(): Int {
        val rnd = Random(name.hashCode())
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    override fun toString(): String {
        return """
             $ticker($name) @ $currentPortfolioValue $currency 
        """.trimIndent()
    }
}

const val TABLE_NAME = "stock_table_name"

@Parcelize
data class StockDetails(
    val afterHours: Double,
    val close: Double,
    val from: String,
    val high: Double,
    val low: Double,
    val open: Double,
    val preMarket: Double,
    val status: String,
    val symbol: String,
    val volume: Int
) : Parcelable
