package org.elvor.translator.ui.main.history.history_query

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.elvor.translator.R
import org.elvor.translator.databinding.FragmentHistoryQueryBinding
import org.elvor.translator.ui.main.MainActivity
import org.elvor.translator.ui.main.QueryResultItem
import org.elvor.translator.ui.main.ResultListAdapter
import org.elvor.translator.ui.main.showErrorNotification
import javax.inject.Inject
import javax.inject.Provider

class HistoryQueryFragment : MvpAppCompatFragment(), HistoryQueryView {
    @Inject
    lateinit var presenterProvider: Provider<HistoryQueryPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    private lateinit var binding: FragmentHistoryQueryBinding
    private lateinit var adapter: ResultListAdapter

    companion object {
        private const val ARG_QUERY = "QUERY"
        private const val ARG_SOURCE_LANGUAGE_ID = "SRC_LANG_ID"
        private const val ARG_TARGET_LANGUAGE_ID = "TRG_LANG_ID"

        fun newInstance(
            query: String,
            sourceLanguageId: Int,
            targetLanguageId: Int
        ): HistoryQueryFragment {
            return HistoryQueryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                    putInt(ARG_SOURCE_LANGUAGE_ID, sourceLanguageId)
                    putInt(ARG_TARGET_LANGUAGE_ID, targetLanguageId)
                }
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryQueryBinding.inflate(inflater, container, false)
        adapter = ResultListAdapter()
        with(binding.resultList) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HistoryQueryFragment.adapter
        }
        return binding.root
    }

    override fun fillQueryInfo(queryString: String, sourceLanguageId: Int, targetLanguageId: Int) {
        val languages = requireContext().resources.getStringArray(R.array.languages)
        with(binding) {
            query.text = queryString
            sourceLanguage.text = languages[sourceLanguageId - 1]
            targetLanguage.text = languages[targetLanguageId - 1]
        }
    }

    override fun onStart() {
        super.onStart()
        val args = arguments ?: throw IllegalArgumentException("no arguments specified")
        val sourceLanguageId = args.getInt(ARG_SOURCE_LANGUAGE_ID)
        if (sourceLanguageId == 0) {
            throw IllegalArgumentException("no source language argument specified")
        }
        val targetLanguageId = args.getInt(ARG_TARGET_LANGUAGE_ID)
        if (targetLanguageId == 0) {
            throw IllegalArgumentException("no target language argument specified")
        }
        presenter.fillQueryInfo(
            args.getString(ARG_QUERY)
                ?: throw IllegalArgumentException("no query argument specified"),
            sourceLanguageId,
            targetLanguageId
        )
    }

    override fun showResult(items: List<QueryResultItem>) {
        adapter.items = items
    }

    override fun showLoading() {
        binding.loadingScreen.root.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingScreen.root.visibility = View.GONE
    }

    override fun showError(message: String?) {
        showErrorNotification(requireContext(), message)
    }
}