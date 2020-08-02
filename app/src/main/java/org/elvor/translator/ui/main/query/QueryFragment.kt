package org.elvor.translator.ui.main.query

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.elvor.translator.R
import org.elvor.translator.databinding.FragmentQueryBinding
import org.elvor.translator.ui.main.MainActivity
import org.elvor.translator.ui.main.QueryResultItem
import org.elvor.translator.ui.main.ResultListAdapter
import org.elvor.translator.ui.main.showErrorNotification
import javax.inject.Inject
import javax.inject.Provider

class QueryFragment : MvpAppCompatFragment(), QueryView {
    @Inject
    lateinit var presenterProvider: Provider<QueryPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }
    private lateinit var binding: FragmentQueryBinding
    private val adapter = ResultListAdapter()
    private var disabledTargetLanguage = 1
    private lateinit var targetLanguageAdapter: ArrayAdapter<String>
    private lateinit var languages: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
        languages = resources.getStringArray(R.array.languages)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQueryBinding.inflate(inflater, container, false)
        with(binding) {
            translateBtn.setOnClickListener { onTranslate() }
            with(results) {
                layoutManager = LinearLayoutManager(context)
                adapter = this@QueryFragment.adapter
            }
            initSourceLanguagesDropdown(binding.sourceLanguage)
            initTargetLanguageDropdown(binding.targetLanguage)
            history.setOnClickListener { presenter.openHistory() }
            forceDisableTargetLanguage(1)
            return root
        }
    }

    private fun initSourceLanguagesDropdown(dropdown: Spinner) {
        initDropdown(dropdown)
        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                presenter.onSourceLanguageSelected(position + 1)
            }
        }
    }

    private fun initTargetLanguageDropdown(dropdown: Spinner) {
        targetLanguageAdapter = initDropdown(dropdown)
    }

    override fun disableTargetLanguage(languageId: Int) {
        if (disabledTargetLanguage == languageId) {
            return
        }
        targetLanguageAdapter.insert(
            languages[disabledTargetLanguage - 1],
            disabledTargetLanguage - 1
        )
        targetLanguageAdapter.remove(languages[languageId - 1])
        targetLanguageAdapter.notifyDataSetChanged()
        disabledTargetLanguage = languageId
    }

    override fun setCurrentLanguage(languageId: Int) {
        binding.currentLanguage.text = resources.getString(R.string.current_language, languages[languageId - 1])
    }

    override fun showError(message: String?) {
        showErrorNotification(requireContext(), message)
    }

    private fun forceDisableTargetLanguage(languageId: Int) {
        val languages = resources.getStringArray(R.array.languages)
        targetLanguageAdapter.remove(languages[languageId - 1])
        targetLanguageAdapter.notifyDataSetChanged()
        disabledTargetLanguage = languageId
    }

    private fun initDropdown(dropdown: Spinner): ArrayAdapter<String> {
        val viewRes = android.R.layout.simple_spinner_dropdown_item
        val adapter = ArrayAdapter<String>(
            requireContext(),
            viewRes,
            resources.getStringArray(R.array.languages).toMutableList()
        )
        adapter.setDropDownViewResource(viewRes)
        dropdown.adapter = adapter
        return adapter
    }

    private fun onTranslate() {
        val word = binding.query.text.trim()
        if (word.isBlank()) {
            return
        }
        binding.query.clearFocus()
        presenter.translate(
            word.toString(),
            binding.sourceLanguage.selectedItemPosition + 1,
            binding.targetLanguage.selectedItemPosition + if (disabledTargetLanguage <= binding.targetLanguage.selectedItemPosition + 1) 2 else 1
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

}