package org.elvor.translator.ui.main.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.elvor.translator.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val languages: Array<String>,
    private val onItemClicked: (HistoryItem) -> Unit
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var items: List<HistoryItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            with(binding) {
                query.text = item.query
                sourceLanguage.text = this@HistoryAdapter.languages[item.sourceLanguageId - 1]
                targetLanguage.text = this@HistoryAdapter.languages[item.targetLanguageId - 1]
                root.setOnClickListener { this@HistoryAdapter.onItemClicked(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}