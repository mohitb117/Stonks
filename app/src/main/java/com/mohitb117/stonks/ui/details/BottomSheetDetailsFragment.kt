package com.mohitb117.stonks.ui.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohitb117.stonks.R
import com.mohitb117.stonks.STOCK_KEY
import com.mohitb117.stonks.common.ResultWrapper
import com.mohitb117.stonks.databinding.FragmentDetailsBinding
import com.mohitb117.stonks.datamodels.Stock
import com.mohitb117.stonks.datamodels.StockDetails
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var viewBinding: FragmentDetailsBinding
    private val detailsViewModel: BottomSheetDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDetailsBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return viewBinding.root
    }

    private fun stockKey() = arguments?.getParcelable<Stock>(STOCK_KEY)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stockKey()?.let { detailsViewModel.loadDetailsResult(it) }

        detailsViewModel.portfolioDetails
            .observe(viewLifecycleOwner) { handleResult(it) }
    }

    @SuppressLint("SetTextI18n")
    private fun handleResult(stockDetailsResultWrapper: ResultWrapper<StockDetails>?) {
        if (stockDetailsResultWrapper == null) return

        if (stockDetailsResultWrapper is ResultWrapper.Loading) {
            viewBinding.apply {
                this.loading.isVisible = true
                this.stockDataContainer.isVisible = false
                this.emptyView.isVisible = false
            }
        }

        if (stockDetailsResultWrapper is ResultWrapper.Success) {
            val data = stockDetailsResultWrapper.data

            viewBinding.apply {
                this.emptyView.isVisible = false
                this.loading.isVisible = false
                this.stockDataContainer.isVisible = true

                this.name.text = data.symbol
                this.from.text = data.from
                this.volume.text = "${data.volume} Units"

                this.stockSpecSummary.text = """
                    preMarket=${data.preMarket},
                    afterhours=${data.afterHours},
                    low=${data.low},
                    high=${data.high},
                    close=${data.close}
                """.trimIndent()
            }
        }

        else if (stockDetailsResultWrapper is ResultWrapper.Error) {
            viewBinding.apply {
                this.emptyView.isVisible = true
                this.emptyView.text = resources.getString(R.string.empty_portfolio_result_for_ticker_reason, stockKey()?.ticker, stockDetailsResultWrapper.error.toString())
                this.loading.isVisible = false
                this.stockDataContainer.isVisible = false
            }
        }
    }
}