package com.example.tylerwells.githubrepofinder

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set constants
        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)

        val userEditText = findViewById<EditText>(R.id.userRepoEditText)
        val userButton = findViewById<Button>(R.id.userRepoButton)

        searchButton.setOnClickListener {


            val searchTermValue = searchEditText.text.toString()

            val intent = Intent(this,SearchResultActivity::class.java)
            intent.putExtra("searchTerm", searchTermValue)
            intent.putExtra("searchType", "repo")
            startActivity(intent)

        }

        userButton.setOnClickListener {

            val searchTermValue = userEditText.text.toString()

            val intent = Intent(this,SearchResultActivity::class.java)
            intent.putExtra("searchTerm", searchTermValue)
            intent.putExtra("searchType", "user")
            startActivity(intent)


        }
    }
}
