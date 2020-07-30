package org.elvor.translator.ui.main.query

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.elvor.translator.databinding.FragmentHomeBinding
import org.elvor.translator.ui.main.MainActivity
import javax.inject.Inject
import javax.inject.Provider

class QueryFragment : MvpAppCompatFragment(), QueryView {

    @Inject
    lateinit var presenterProvider: Provider<QueryPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }
    private lateinit var binding: FragmentHomeBinding
    private val adapter = QueryResultAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        with(binding) {
            translateBtn.setOnClickListener {onTranslate()}
            with(results) {
                layoutManager = LinearLayoutManager(context)
                adapter = this@QueryFragment.adapter
            }
            return root
        }
    }

    private fun onTranslate() {
        val word = binding.query.text.trim()
        if (word.isBlank()) {
            return
        }
        presenter.translate(word.toString())
    }

    override fun showResult(items: List<QueryResultItem>) {
        adapter.items = items
    }

    override fun showLoading() {
        binding.loadingScreen.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loadingScreen.visibility = View.GONE
    }

}