package com.som.structureorigamimatching.view

import android.content.Context
import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.som.structureorigamimatching.presenter.DeckInterface.View
import com.som.structureorigamimatching.presenter.DeckPresenter
import com.som.structureorigamimatching.R
import com.som.structureorigamimatching.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),View {

    private lateinit var presenter : DeckPresenter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        presenter = DeckPresenter(this)
        presenter.beginGame()
    }

    override fun getContext(): Context {
        return this
    }

    override fun startGame() {
        binding.stepCounterText.text = "0"
        binding.deckRecycler.visibility     = VISIBLE
        binding.stepCounterText.visibility  = VISIBLE
        binding.stepsText.visibility        = VISIBLE
        binding.endingLayout.visibility     = GONE
        binding.deckRecycler.layoutManager = GridLayoutManager(this, 4)
        binding.deckRecycler.adapter = presenter.getAdapter()
    }

    override fun showEnding() {
        binding.deckRecycler.visibility         = GONE
        binding.stepCounterText.visibility      = GONE
        binding.stepsText.visibility            = GONE
        binding.endingLayout.visibility         = VISIBLE
    }

    override fun refreshData(position: Int) {
        binding.deckRecycler.adapter?.notifyItemChanged(position)
    }

    override fun showToast(message: String) {
        Toast.makeText(this,"Правильно",Toast.LENGTH_SHORT).show()
    }

    override fun updateSteps(value: Int) {
        binding.stepCounterText.text = value.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun bind() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        binding.playagainButton.setOnClickListener {
            presenter.beginGame()
        }
    }


}