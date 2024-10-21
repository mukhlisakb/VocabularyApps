package com.example.vocabularyapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.vocabularyapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedUserName = sharedPreferences.getString("USER_NAME", null)
        if (savedUserName != null) {
            navigateToDashboard(savedUserName)
        } else {
            showOnBoard()
        }
    }

    private fun showOnBoard() {
        bindingMain.etNameOnboarding.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                bindingMain.ivTitle.text = getString(R.string.txt_title_default)
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                bindingMain.ivTitle.text = getString(R.string.txt_title_replace)
            }

            override fun afterTextChanged(s: Editable?) {
                bindingMain.ivTitle.text = getString(R.string.txt_title_replace)
                bindingMain.btnStart.isVisible = !s.isNullOrEmpty()

            }
        })
        bindingMain.btnStart.setOnClickListener {
            val userName = bindingMain.etNameOnboarding.text.toString()
            saveName(userName)
            navigateToDashboard(userName)
        }
    }

    private fun saveName(userName: String) {
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("USER_NAME", userName)
        editor.apply()
    }
    private fun navigateToDashboard(userName: String){
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        startActivity(intent)
    }
}