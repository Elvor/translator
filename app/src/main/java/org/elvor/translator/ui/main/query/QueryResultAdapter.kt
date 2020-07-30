package org.elvor.translator.ui.main.query

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.elvor.translator.databinding.ItemResultBinding
import org.elvor.translator.databinding.ItemResultHeaderBinding

class QueryResultAdapter : RecyclerView.Adapter<QueryResultAdapter.ViewHolder>() {

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }

    var items: List<QueryResultItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: Binding): RecyclerView.ViewHolder(binding.root)

    class Binding(val root: View, val value: TextView, val count: TextView?, val info: TextView?)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = when (viewType) {
            HEADER -> {
                val viewBinding = ItemResultHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                Binding(viewBinding.root, viewBinding.value, null, null)
            }
            else -> {
                val viewBinding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                Binding(viewBinding.root, viewBinding.value, viewBinding.count, viewBinding.info)
            }
        }
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].info == null) HEADER else ITEM
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            HEADER -> with(holder.binding) {
                value.text = Html.fromHtml(item.value)
            }
            ITEM -> with(holder.binding) {
                value.text = Html.fromHtml(item.value)
                count?.text = item.count.toString()
                info?.text = Html.fromHtml(item.info)
            }
        }
    }
}