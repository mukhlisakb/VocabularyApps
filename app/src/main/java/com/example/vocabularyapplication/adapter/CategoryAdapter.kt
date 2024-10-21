package com.example.vocabularyapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapplication.R
import com.example.vocabularyapplication.databinding.ItemCategoryBinding
import com.example.vocabularyapplication.model.WordCategory

class CategoryAdapter(
    private val mList: List<WordCategory>,
    selectedCategory: WordCategory,
    private var onSelectedCategory: (WordCategory) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var currentCategory: WordCategory = selectedCategory

    class CategoryViewHolder(private val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(
            item: WordCategory,
            selectedCategory: WordCategory,
            onSelectCategory: (WordCategory) -> Unit
        ) {
            itemCategoryBinding.tvCategory.text = item.title
            if (item.ordinal == selectedCategory.ordinal) {
                itemCategoryBinding.border.setCardBackgroundColor(
                    itemCategoryBinding.root.context.getColor(
                        R.color.colorBackgrounStartButton
                    )
                )
            } else {
                itemCategoryBinding.border.setCardBackgroundColor(
                    itemCategoryBinding.root.context.getColor(
                        R.color.colorCardBackground
                    )
                )
            }
            itemCategoryBinding.root.setOnClickListener {
                onSelectCategory(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = mList.size

    internal fun updateSelectedCategory(selectedCategory: WordCategory) {
        notifyItemChanged(currentCategory.ordinal)
        currentCategory = selectedCategory
        notifyItemChanged(selectedCategory.ordinal)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(mList[position], currentCategory, onSelectedCategory)
    }
}

