package org.elvor.translator.ui.main.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.elvor.translator.R
import org.elvor.translator.databinding.FragmentHistoryBinding
import org.elvor.translator.ui.main.MainActivity
import javax.inject.Inject
import javax.inject.Provider


class HistoryFragment : MvpAppCompatFragment(), HistoryView {

    @Inject
    lateinit var presenterProvider: Provider<HistoryPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
        adapter = HistoryAdapter(context.resources.getStringArray(R.array.languages)) { item ->
            presenter.openHistoryItem(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        with(binding) {
            with(queryHistory) {
                val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                layoutManager = linearLayoutManager
                adapter = this@HistoryFragment.adapter

                val dividerItemDecoration = DividerItemDecoration(
                    requireContext(),
                    linearLayoutManager.orientation
                )
                addItemDecoration(dividerItemDecoration)
            }
            return root
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.fetchHistory()
    }

    override fun showHistory(history: List<HistoryItem>) {
        adapter.items = history
    }

    override fun showLoading() {
        binding.loadingScreen.root.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingScreen.root.visibility = View.GONE
    }
}