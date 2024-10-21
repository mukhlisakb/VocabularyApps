package com.example.vocabularyapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyapplication.adapter.CategoryAdapter
import com.example.vocabularyapplication.adapter.VocabAdapter
import com.example.vocabularyapplication.databinding.ActivityDashboardBinding
import com.example.vocabularyapplication.db.SqlDbHandler
import com.example.vocabularyapplication.model.ListWordState
import com.example.vocabularyapplication.model.WordCategory

class DashboardActivity : AppCompatActivity() {
    private lateinit var bindingDashboard: ActivityDashboardBinding
    private lateinit var adapterCategory: CategoryAdapter
    private lateinit var adapterVocab: VocabAdapter
    private var selectedListState: ListWordState = ListWordState.NORMAL
    private var selectedCategory: WordCategory = WordCategory.ALL_CATEGORY
    private lateinit var dbHandler: SqlDbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDashboard = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(bindingDashboard.root)

        dbHandler = SqlDbHandler(this)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", null)
        if (userName != null) {
            bindingDashboard.tvGreetings.text = getString(R.string.txt_greetings, userName)
        }
        setCategoryList()
        setVocabList()

        // Handle add button click
        bindingDashboard.ivPlus.setOnClickListener {
            navigateToAddVocab()
        }
    }

    private fun setCategoryList() {
        val categoryList = WordCategory.values().toList()
        adapterCategory = CategoryAdapter(categoryList, selectedCategory) { wordCategory ->
            selectedCategory = wordCategory
            refreshListCategoryAndVocab(wordCategory)
        }
        bindingDashboard.rvCategory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = adapterCategory
        }
    }

    private fun refreshListCategoryAndVocab(wordCategory: WordCategory) {
        val listWord = if (wordCategory == WordCategory.ALL_CATEGORY) {
            dbHandler.getVocab()
        } else {
            dbHandler.getVocab().filter {
                it.category == wordCategory
            }
        }
        // Update vocab list and selected category in the adapter
        adapterVocab.refreshList(listWord)
        adapterCategory.updateSelectedCategory(selectedCategory)
    }

    // Navigate to add new Vocab
    private fun navigateToAddVocab() {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, 123)
    }

    // Set up vocabulary list adapter
    private fun setVocabList() {
        adapterVocab = VocabAdapter(dbHandler.getVocab(), selectedListState) { positionToBeRemoved ->
            removeAndRefresh(positionToBeRemoved)
        }
        bindingDashboard.rvVocab.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterVocab
        }
    }

    private fun removeAndRefresh(position: Int) {
        dbHandler.deleteVocab(position)
        adapterVocab.refreshList(dbHandler.getVocab())

        // Logic to toggle button visibility based on vocabulary list state
        if (dbHandler.getVocab().isNotEmpty()) {
            showButtonsForCancel()
        } else {
            showButtonsForEmptyList()
        }
    }

    private fun showButtonsForCancel() {
        bindingDashboard.btnCencel.isVisible = true
        bindingDashboard.ivPlus.isVisible = true
        bindingDashboard.ivDelete.isVisible = true
    }

    private fun showButtonsForEmptyList() {
        bindingDashboard.btnCencel.isVisible = true
        bindingDashboard.ivPlus.isVisible = true
        bindingDashboard.ivDelete.isVisible = dbHandler.getVocab().isNotEmpty()
    }
}
