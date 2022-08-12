package com.som.structureorigamimatching.presenter

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import com.som.structureorigamimatching.model.Card
import com.som.structureorigamimatching.model.CardImage
import com.som.structureorigamimatching.presenter.DeckInterface.*
import com.som.structureorigamimatching.R
import com.som.structureorigamimatching.adapter.DeckAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class DeckPresenter (view: View) : Presenter {

    private var mView: View = view
    private var itemsCount = 12
    private var stepCount: Int = 0
    private var items: ArrayList<Card> = arrayListOf()
    private var images: ArrayList<CardImage> = arrayListOf()
    private var visibles = arrayListOf<Int>()

    // RecyclerView elements clickable
    private var isCLickable = true

    init {
        mView.bind()
        fillList()
    }

    override fun fillInitial() {
        var x = 0

        while (x < itemsCount) {
            val image = images[Random.nextInt(images.size)]

            if (countOf(Card(image)) < 2) {
                items.add(Card(image))
                items.add(Card(image))
                x+=2
            }
        }
        //  mix cards randomly
        items.shuffle()
    }

    override fun beginGame() {
        fillInitial()
        mView.startGame()
        stepCount = 0
    }

    override fun fillList() {
        images.add(CardImage(R.drawable.ic_origami1,Color.parseColor("#FBE2B4")))
        images.add(CardImage(R.drawable.ic_origami2,Color.parseColor("#6C8672")))
        images.add(CardImage(R.drawable.ic_origami3,Color.parseColor("#FFD900")))
        images.add(CardImage(R.drawable.ic_origami4,Color.parseColor("#638CA6")))
        images.add(CardImage(R.drawable.ic_origami5,Color.parseColor("#260126")))
        images.add(CardImage(R.drawable.ic_origami6,Color.parseColor("#ADC4CC")))
        images.add(CardImage(R.drawable.ic_origami7,Color.parseColor("#354458")))
        images.add(CardImage(R.drawable.ic_origami8,Color.parseColor("#1FDA9A")))
        images.add(CardImage(R.drawable.ic_origami9,Color.parseColor("#85C4B9")))
        images.add(CardImage(R.drawable.ic_origami10,Color.parseColor("#3B3A35")))
        images.add(CardImage(R.drawable.ic_origami11,Color.parseColor("#C6D5CD")))
        images.add(CardImage(R.drawable.ic_origami12,Color.parseColor("#69D2E7")))
    }

    override fun getAdapter(): DeckAdapter {
        val deckAdapter = DeckAdapter(items, mView.getContext()) {
            if (isCLickable) {
                if (!items[it].isVisible) {

                        items[it].isVisible = true
                        visibles.add(it)
                        mView.refreshData(it)

                    if (visibles.size == 2) {
                        isCLickable = false

                        Handler(Looper.getMainLooper()).postDelayed({
                            if (items[visibles[0]].getImage() == items[visibles[1]].getImage()) {
                                items[visibles[0]].isMatched = true
                                items[visibles[1]].isMatched = true
                                mView.showToast("Yay!")
                            } else {
                                items[visibles[0]].isVisible = false
                                items[visibles[1]].isVisible = false
                            }
                            mView.refreshData(visibles[0])
                            mView.refreshData(visibles[1])
                            visibles.clear()
                            isCLickable = true
                            checkGameEnd()
                        }, 1000L)
                        stepCount++
                    }
                    mView.updateSteps(stepCount)
                }
            }
        }
        return deckAdapter
    }

    private fun checkGameEnd() {
        var i = 0
        for(k in 0 until items.size) {
            if(items[k].isMatched) i++
        }
        if(i == items.size) {
            mView.showEnding()
            items.clear()
        }
    }

    private fun countOf(item: Card): Int {
        return Collections.frequency(items, item)
    }
}