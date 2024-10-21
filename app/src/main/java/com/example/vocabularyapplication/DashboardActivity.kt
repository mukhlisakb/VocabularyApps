package com.example.vocabularyapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
    private var selectListState: ListWordState = ListWordState.NORMAL
    private var selectedCategory: WordCategory = WordCategory.ALL_CATEGORY
    private val dbHandler: SqlDbHandler = SqlDbHandler(this)
    private var progress = 0
    private var maxVocab = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingDashboard = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(bindingDashboard.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", null)
        if (userName != null) {
            bindingDashboard.tvGreeting.text = getString(R.string.txt_greeting, userName)
        }
        buttonDeleteAdd()
        setCategoryList()
        setVocabList()
        setProgressandRefreshed()

        // add button
        bindingDashboard.ivAdd.setOnClickListener {
            selectListState = ListWordState.NORMAL
            adapterVocab.setListState(selectListState)
            navigateToNewVocab()
        }

        bindingDashboard.ivDelete.setOnClickListener {
            buttonCancel()
            selectListState = ListWordState.REMOVED
            adapterVocab.setListState(selectListState)
        }

        bindingDashboard.btnCancel.setOnClickListener {
            buttonDeleteAdd()
            selectListState = ListWordState.NORMAL
            adapterVocab.setListState(selectListState)
        }


    }

    fun setCategoryList() {
        val categoryList = WordCategory.values().toList()
        adapterCategory = CategoryAdapter(categoryList, selectedCategory) { wordCategory ->
            selectedCategory = wordCategory
            refreshListCategoryandVocab(wordCategory)
        }
        bindingDashboard.rvCategory.apply {
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = adapterCategory
        }
    }

    private fun refreshListCategoryandVocab(wordCategory: WordCategory) {
        val listWord = if (wordCategory == WordCategory.ALL_CATEGORY) {
            dbHandler.getVocab()
        } else {
            dbHandler.getVocab().filter { it.category == wordCategory }
        }
        // update list
        adapterVocab.refreshList(listWord)
        adapterCategory.updateSelectedCategory(selectedCategory)
    }

    // navigate to add new vocab
    private fun navigateToNewVocab() {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, 123)
    }

    // set value adapter vocab
    private fun setVocabList() {
        adapterVocab = VocabAdapter(dbHandler.getVocab(), selectListState) { positionTobeRemoved ->
            //removeandrefreshed
            removedAndRefreshed(positionTobeRemoved)
        }
        bindingDashboard.rvVocab.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = adapterVocab
        }
    }

    // removed and refreshed
    private fun removedAndRefreshed(position: Int) {
        dbHandler.deleteVocab(position)
        adapterVocab.refreshList(dbHandler.getVocab())
        setProgressandRefreshed()

        // logic untuk mengatur button yang ditampilkan
        if (dbHandler.getVocab().isNotEmpty()) {
            buttonCancel()
        } else {
            buttonDeleteAdd()
        }
    }

    private fun buttonCancel() {
        bindingDashboard.btnCancel.visibility = View.VISIBLE
        bindingDashboard.ivDelete.visibility = View.GONE
        bindingDashboard.ivAdd.visibility = View.GONE // Show add if progress is less than 100
        selectListState = ListWordState.NORMAL
        setProgressandRefreshed()
    }

    private fun buttonDeleteAdd() {
        bindingDashboard.btnCancel.visibility = View.GONE
        bindingDashboard.ivAdd.visibility = View.VISIBLE
        bindingDashboard.ivDelete.visibility = View.VISIBLE
        setProgressandRefreshed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            adapterVocab.refreshList(dbHandler.getVocab())
            buttonDeleteAdd()
        }
    }

    private fun setProgressandRefreshed() {
        progress = (dbHandler.getVocab().size * 100) / maxVocab
        bindingDashboard.tvTitleVocabAvailableValue.text =
            getString(R.string.txt_available_value, progress)
        bindingDashboard.tvAchieved.text = "$progress %"
        bindingDashboard.pbAchieved.progress = progress
        bindingDashboard.ivAdd.isVisible = progress < 100 && selectListState == ListWordState.NORMAL
    }
}