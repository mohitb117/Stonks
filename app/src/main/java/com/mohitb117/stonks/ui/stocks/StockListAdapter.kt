package com.mohitb117.stonks.ui.stocks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mohitb117.stonks.adapters.DiffCallback
import com.mohitb117.stonks.databinding.ItemStonkBinding
import com.mohitb117.stonks.datamodels.Stock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StockListAdapter(
    private var callbacks: Callbacks? = null
) : ListAdapter<Stock, StockListAdapter.StockViewHolder>(
    DiffCallback<Stock>()
) {

    private val listener: (stock: Stock) -> Unit = { callbacks?.onStockSelected(it) }

    override fun onViewRecycled(holder: StockViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStonkBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return StockViewHolder(binding)
    }

    override fun submitList(list: List<Stock>?) {
        super.submitList(interleaveStocks(list))
    }

    /**
     * Interleaves even index for [GridLayoutManager] to show all non-null stocks at the top.
     */
    private fun interleaveStocks(list: List<Stock>?): List<Stock> {
        if (list.isNullOrEmpty()) return emptyList()

        val sortedDescending = list.sortedByDescending { it.ratio }
        val emptyStockPortfolio = list.filter { it.ratio == 0F }
        val nonEmptyStockPortfolio = sortedDescending.minus(emptyStockPortfolio.toSet())

        val leftoverStocks = list.toMutableSet()
        val result = Array<Stock?>(leftoverStocks.size) { null }

        val handleIndexAndStock : (Int, Stock) -> Unit = { computedIndex, stock ->
            if (computedIndex <= result.lastIndex) {
                result[computedIndex] = stock
                leftoverStocks.remove(stock)
            }
        }

        nonEmptyStockPortfolio.forEachIndexed { index, stock ->
            val computedIndex = 2 * index
            handleIndexAndStock(computedIndex, stock)
        }

        emptyStockPortfolio.forEachIndexed { index, stock ->
            val computedIndex = 2 * index + 1
            handleIndexAndStock(computedIndex, stock)
        }

        leftoverStocks.forEach { stock ->
            val firstNullIndex = result.indexOfFirst { it == null }
            result[firstNullIndex] = stock
        }

        return result.filterNotNull()
    }

    inner class StockViewHolder(
        private val binding: ItemStonkBinding
    ) : ViewHolder(binding.root) {

        private var job: Job? = null

        @SuppressLint("SetTextI18n")
        fun bind(stock: Stock) {
            val data = stock.ticker

            binding.name.apply {
                binding.root.setOnClickListener { listener(stock) }

                text = data

                job = CoroutineScope(Dispatchers.Main).launch {
                    compoundDrawables[0]?.setTint(stock.generateColor())
                }
            }

            binding.fraction.apply {
                binding.root.setOnClickListener { listener(stock) }
                text = "${stock.ratio.format(1)} %"
            }

            binding.root.setOnClickListener { listener(stock) }
        }

        fun onViewRecycled() {
            job?.cancel()
        }
    }

    private fun Number.format(digits: Int): String {
        return "%.${digits}f".format(this)
    }
}

interface Callbacks {
    /**
     * Callback for a selected [Stock].
     */
    fun onStockSelected(stock: Stock)
}
