package com.mohitb117.stonks.ui.stocks

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.snackbar.Snackbar
import com.mohitb117.stonks.R
import com.mohitb117.stonks.activities.LaunchActivityViewModel
import com.mohitb117.stonks.common.ResultWrapper
import com.mohitb117.stonks.common.isNetworkAvailable
import com.mohitb117.stonks.databinding.FragmentStockViewBinding
import com.mohitb117.stonks.datamodels.Portfolio
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.repositories.PortfolioEndpoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

/**
 * UI to search the STONKS API.
 */
@AndroidEntryPoint
class StockViewFragment : Fragment(), Callbacks, SwipeRefreshLayout.OnRefreshListener {

    private var snackbar: Snackbar? = null
    private lateinit var viewBinding: FragmentStockViewBinding

    private val viewModel: StonksViewModel by viewModels()

    private val activityViewModel: LaunchActivityViewModel by activityViewModels()

    private val stockListAdapter = StockListAdapter(this)

    private val resultListener: (ResultWrapper<Portfolio>?) -> Unit = { updateForStockData(it) }

    private val valueSelectedListener = object : OnChartValueSelectedListener {
        override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
            val stock = entry?.data
            Log.v(
                TAG,
                "onValueSelected for entry $entry and data: $stock and highlight: $highlight"
            )

            viewBinding.chart.highlightValue(highlight)

            dismissSnackbar()

            snackbar = Snackbar.make(
                requireContext(),
                viewBinding.myCoordinatorLayout,
                "$stock",
                Snackbar.LENGTH_INDEFINITE
            ).apply { show() }
        }

        override fun onNothingSelected() = dismissSnackbar()
    }

    private fun dismissSnackbar() {
        this.snackbar?.dismiss()
        this.snackbar = null
    }

    override fun onStockSelected(stock: Stock) {
        val chart = viewBinding.chart
        val dataSets = chart.data.dataSets
        val focussedItem = dataSets?.firstOrNull { it.label == stock.ticker }?.getEntryForIndex(0)

        focussedItem?.let {
            viewBinding.chart.centerViewToAnimated(
                focussedItem.x,
                focussedItem.y,
                YAxis.AxisDependency.RIGHT,
                TimeUnit.MILLISECONDS.toMillis(700)
            )
        }

        activityViewModel.onStockSelected(stock)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentStockViewBinding.inflate(inflater, container, false)

        initAdapter(viewBinding.list)
        initLineChart(viewBinding.chart)

        viewBinding.swipeRefreshLayout.setOnRefreshListener(this)

        val greeting = viewBinding.greeting

        greeting.setContent {
            MdcTheme { // or AppCompatTheme
                Greeting(stringResource(R.string.greeting))
            }
        }

        return viewBinding.root
    }

    @Composable
    private fun Greeting(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.margin_small))
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }

    @Preview
    @Composable
    fun DummyPreview() {
        MdcTheme {
            Greeting(text = "Hello World!!!")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve and show cached portfolio.
        viewModel
            .portfolio
            .observe(viewLifecycleOwner) { resultListener(it) }

        // On new Setting selected
        activityViewModel
            .portfolioEndpoint
            .observe(viewLifecycleOwner) { onRefreshInternal(it) }
    }

   private fun initAdapter(list: RecyclerView) {
        list.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
            adapter = stockListAdapter
        }
    }

    private fun initLineChart(lineChart: LineChart) {
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        //remove legend
        lineChart.legend.isEnabled = true

        //remove description label
        lineChart.description.isEnabled = true

        //add animation
        lineChart.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter(emptyList())
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
    }

    inner class MyAxisFormatter(private val stocks: List<Stock>) : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (stocks.isNotEmpty()) stocks[index].ticker else ""
        }
    }

    private fun setDataToLineChart(stocks: List<Stock>) {
        //now draw bar chart with dynamic data
        val lineDataAsList = mutableListOf<ILineDataSet>()

        //you can replace this data object with your custom object
        for (i in stocks.indices) {
            val stock = stocks[i]
            val entries = mutableListOf<Entry>()
            val portfolioValue = stock.currentPortfolioValue.toFloat()

            if (portfolioValue == 0F) continue

            val entry = Entry(i.toFloat(), portfolioValue)
            entry.data = stock
            entries.add(entry)

            val lineDataSet = LineDataSet(entries, stock.ticker)
            val color = stock.generateColor()
            lineDataSet.color = color
            lineDataSet.setCircleColor(color)
            lineDataAsList.add(lineDataSet)
        }

        val data = LineData(lineDataAsList.toList())

        viewBinding.chart.apply {
            xAxis.valueFormatter = MyAxisFormatter(emptyList())

            setOnChartValueSelectedListener(valueSelectedListener)

            setData(data)

            invalidate()
        }
    }

    @Suppress("DEPRECATION")
    private fun updateForStockData(portfolioResult: ResultWrapper<Portfolio>?) {
        if (portfolioResult == null) return
        setupUiElementsForState(portfolioResult)

        when (portfolioResult) {
            is ResultWrapper.Success -> {
                val data = portfolioResult.data
                val stocks = data.stocks

                Log.v(TAG, "StockCount = ${stocks.size} and \n $stocks")

                if (stocks.isEmpty()) {
                    viewBinding.emptyView.isVisible = true
                    viewBinding.emptyView.text = resources.getString(R.string.empty_portfolio_result_for_reason, "The backend had no portfolio data for you ????????????")
                    return
                }

                setDataToLineChart(stocks)
                stockListAdapter.submitList(stocks)

                val current = resources.configuration.locale
                val formatter = NumberFormat.getCurrencyInstance(current)
                viewBinding.portfolioDetails.text = data.portfolioDetails(formatter, portfolioResult.isCachedData)
            }
            else -> Unit
        }
    }

    private fun setupUiElementsForState(portfolioResult: ResultWrapper<Portfolio>?) {
        when(portfolioResult) {
            is ResultWrapper.Loading -> {
                viewBinding.apply {
                    loading.isVisible = true
                    swipeRefreshLayout.isRefreshing = true
                    emptyView.isVisible = false
                }
            }

            is ResultWrapper.Error -> {
                viewBinding.apply {
                    portfolioData.isVisible = false
                    this.emptyView.isVisible = true
                    this.emptyView.text = resources.getString(R.string.empty_portfolio_result_for_reason, portfolioResult.error.toString())
                    loading.isVisible = false
                    swipeRefreshLayout.isRefreshing = false
                }
            }

            is ResultWrapper.Success -> {
                val data = portfolioResult.data
                val stocks = data.stocks
                val isAvailable = stocks.isNotEmpty()

                viewBinding.apply {
                    portfolioData.isVisible = isAvailable
                    emptyView.isVisible = !isAvailable
                    loading.isVisible = false
                    swipeRefreshLayout.isRefreshing = false
                }
            }
            else -> Unit
        }
    }

    override fun onDestroyView() {
        dismissSnackbar()
        super.onDestroyView()
    }

    override fun onRefresh() {
        dismissSnackbar()
        val default = activityViewModel.portfolioEndpoint.value ?: PortfolioEndpoint.Good
        onRefreshInternal(default)
    }

    @SuppressLint("SetTextI18n")
    private fun onRefreshInternal(endpoint: PortfolioEndpoint) {
        val isNetworkAvailable = { requireContext().isNetworkAvailable() }

        val offlineObserver =
            Observer<ResultWrapper<Portfolio>> { if (!isNetworkAvailable()) resultListener(it) }

        if (isNetworkAvailable()) {
            lifecycleScope.launch { viewModel.loadStocks(endpoint) }
            viewModel.getCachedPortfolio().removeObserver(offlineObserver)
        } else {
            viewBinding.portfolioDetails.apply {
                isVisible = true
                text = "Sorry!!! No internet available... fetching cached data ????"
            }

            lifecycleScope.launch {
                delay(1000)
                viewModel
                    .getCachedPortfolio()
                    .observe(viewLifecycleOwner, offlineObserver)
            }
        }
    }

    private companion object {
        private val TAG = StockViewFragment::class.java.simpleName
    }
}