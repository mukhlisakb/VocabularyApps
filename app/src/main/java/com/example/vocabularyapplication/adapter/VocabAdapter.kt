package com.example.vocabularyapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapplication.databinding.ItemVocabularyBinding
import com.example.vocabularyapplication.model.ListWordState
import com.example.vocabularyapplication.model.WordData

class VocabAdapter(
    private var currentList: List<WordData>,
    private var currentListState: ListWordState,
    private val onRemovedItem: (Int) -> Unit
) : RecyclerView.Adapter<VocabAdapter.VocabViewHolder>() {

    class VocabViewHolder(private val itemWordViewBinding: ItemVocabularyBinding) :
        RecyclerView.ViewHolder(itemWordViewBinding.root) {

        fun bind(item: WordData, currentListState: ListWordState, onRemovedItem: (Int) -> Unit) {
            itemWordViewBinding.tvNameVocab.text = item.name
            itemWordViewBinding.tvMeaning.text = item.meaning
            itemWordViewBinding.tvCategory.apply {
                text = item.category.title
                requestLayout()
            }
            itemWordViewBinding.layoutCategory.setCardBackgroundColor(
                itemWordViewBinding.root.context.getColor(item.category.color)
            )
            itemWordViewBinding.btnRemove.isVisible = currentListState == ListWordState.REMOVED
            itemWordViewBinding.btnRemove.setOnClickListener {
                onRemovedItem(item.id) // Pastikan item.id adalah Int
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabViewHolder {
        val view = ItemVocabularyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VocabViewHolder(view)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: VocabViewHolder, position: Int) {
        holder.bind(currentList[position], currentListState, onRemovedItem)
    }

    internal fun setListState(selectedListState: ListWordState) {
        currentListState = selectedListState
        notifyDataSetChanged()
    }

    internal fun refreshList(newList: List<WordData>) {
        currentList = newList
        notifyDataSetChanged()
    }
}
