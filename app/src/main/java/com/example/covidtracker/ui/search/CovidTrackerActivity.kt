package com.example.covidtracker.ui.search

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.common.Constants
import com.example.covidtracker.common.TwitterSearchQueryMaker
import com.example.covidtracker.common.dp
import com.example.covidtracker.common.viewBinding
import com.example.covidtracker.databinding.ActivityCovidTrackerBinding
import com.example.covidtracker.ui.tweets.TweetActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.xiaofeng.flowlayoutmanager.Alignment
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CovidTrackerActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityCovidTrackerBinding::inflate)
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpUI()
        setUpObservers()
    }

    private fun setUpUI() {

        with(binding) {

            val adapter = LocationAdapter {
                var location = ""
                if (it.isNotEmpty()) {
                    search.alpha = 1f
                    search.isEnabled = true

                    it.forEach {
                        if (location.isEmpty())
                            location = it
                        else
                            location = "$location, $it"
                    }

                    val list = it.toString()
                    list.replace("[", "")
                    list.replace("]", "")
                }
                cityNameInput.editText?.setText(location)
            }

            val locationList =
                mutableListOf("Delhi", "Varanasi", "Mumbai", "Ahemdabad", "Kolkata", "Lucknow")

            adapter.set(locationList)

            with(quickLocationsList) {
                this.adapter = adapter
                layoutManager = FlowLayoutManager().apply {
                    isAutoMeasureEnabled = true
                    setAlignment(Alignment.LEFT)
                }

                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        outRect.set(4.dp(), 10.dp(), 4.dp(), 10.dp())
                    }
                })
            }

            search.alpha = 0.7f
            search.isEnabled = false
            search.setOnClickListener {
                query = TwitterSearchQueryMaker.getQuery(
                    bedsCheckbox.isChecked,
                    icuCheckbox.isChecked,
                    oxygenCheckbox.isChecked,
                    ventilatorCheckbox.isChecked,
                    favifluCheckbox.isChecked,
                    remdesivirCheckbox.isChecked,
                    plasmaCheckbox.isChecked,
                    tocilizumabCheckbox.isChecked,
                    otherInput.editText?.text.toString(),
                    cityNameInput.editText?.text.toString()
                )

                Log.v("TweetLogs", "query: $query")

                startActivity(Intent(this@CovidTrackerActivity, TweetActivity::class.java)
                    .apply {
                        putExtra(Constants.SEARCH_QUERY, query)
                    })
            }

            setCheckListeners(
                listOf(
                    bedsCheckbox,
                    icuCheckbox,
                    oxygenCheckbox,
                    ventilatorCheckbox,
                    favifluCheckbox,
                    remdesivirCheckbox,
                    plasmaCheckbox,
                    tocilizumabCheckbox
                ), search
            )

        }

    }

    private fun setCheckListeners(checkBoxes: List<MaterialCheckBox>, search: MaterialButton) {
        for (checkbox in checkBoxes) {
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                search.alpha = 1f
                search.isEnabled = true
            }
        }

    }

    private fun setUpObservers() {

    }
}