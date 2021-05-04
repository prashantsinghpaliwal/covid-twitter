package com.example.covidtracker.ui.tweets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.covidtracker.R
import com.example.covidtracker.common.*
import com.example.covidtracker.data.remote.Tweet
import com.example.covidtracker.databinding.ActivityTweetBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class TweetActivity : AppCompatActivity(), TweetAdapter.ItemClickCallback,
    SwipeRefreshLayout.OnRefreshListener {

    private val binding by viewBinding(ActivityTweetBinding::inflate)
    private val viewModel by viewModels<CovidTrackerViewModel>()

    private val compositeDisposable = CompositeDisposable()
    private var query: String? = null

    private var tweetAdapter: TweetAdapter? = null
    private var listSize = 0
    private var lastTweedId = ""
    private var isLoadMoreRequest = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpUI()
        setUpObservers()
    }

    private fun setUpObservers() {
        intent?.let {
            query = it.getStringExtra(Constants.SEARCH_QUERY)
        }
        getLatestTweets()
    }

    private fun setUpUI() {
        tweetAdapter = TweetAdapter(this)
        with(binding) {

            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            tweetList.apply {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = tweetAdapter
            }

            swipeRefreshLayout.setOnRefreshListener(this@TweetActivity)
            swipeRefreshLayout.setColorSchemeResources(R.color.green_700)

            tweetList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (!isLoadMoreRequest) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() ==
                            listSize - 1
                        ) {
                            //bottom of list!
                            getMoreTweets()
                            isLoadMoreRequest = true
                        }
                    }

                }
            })
        }
    }

    override fun onItemClicked(item: Tweet) {

    }

    override fun onProfileClicked(item: Tweet) {
        if (Utils.doesPackageExist(this, "com.twitter.android")) {
            val twitterIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("twitter://user?user_id=${item.userData.id}")
            )

            val shareIntent = Intent.createChooser(twitterIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun onShareItemClicked(item: Tweet) {
        val shareText = "${item.tweetData.text} \n \n Shared via Covid Twitter App"
        val intent = Intent()
        with(intent) {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooserIntent: Intent = Intent.createChooser(intent, "Share Data via...")

        startActivity(chooserIntent)
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.post {
            listSize = 0
            lastTweedId = ""
            binding.swipeRefreshLayout.isRefreshing = true
            getLatestTweets()
        }
    }

    private fun getLatestTweets() {

        with(binding) {
            viewModel.getTweets(query ?: "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.render(
                        onLoading = {
                            progress.visible()
                        },
                        onSuccess = { tweets ->
                            tweets?.let {
//                                    val updatedList = removeUnwantedTweets(tweets)
                                tweetAdapter?.setData(tweets)
                                listSize += tweets.size
                                lastTweedId = tweets[tweets.size - 1].meta.oldestId!!
                            }
                            progress.gone()
                            isLoadMoreRequest = false
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                        },
                        onError = {
                            progress.gone()
                            isLoadMoreRequest = false
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                        })

                }, {
                    Log.v("TweetLogs", "words : ${it}")
                }).addTo(compositeDisposable)
        }

    }

    private fun getMoreTweets() {
        with(binding) {
            viewModel.getMoreTweets(query ?: "", lastTweedId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.render(
                        onLoading = {
                            progress.visible()
                        },
                        onSuccess = { tweets ->
                            tweets?.let {
                                if (isLoadMoreRequest) {
//                                    val updatedList = removeUnwantedTweets(tweets)
                                    tweetAdapter?.updateData(tweets)
                                    listSize += tweets.size
                                    lastTweedId = tweets[tweets.size - 1].meta.oldestId!!
                                }
                            }
                            progress.gone()
                            isLoadMoreRequest = false
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                        },
                        onError = {
                            progress.gone()
                            isLoadMoreRequest = false
                            if (swipeRefreshLayout.isRefreshing) {
                                swipeRefreshLayout.isRefreshing = false
                            }
                        })

                }, {
                    Log.v("TweetLogs", "words : ${it}")
                }).addTo(compositeDisposable)
        }
    }

    private fun removeUnwantedTweets(itemList: MutableList<Tweet>): MutableList<Tweet> {
        val removeIndices = mutableListOf<Int>()
        itemList.mapIndexed { index, tweet ->

            val text = tweet.tweetData.text!!

            val words = text.toLowerCase().split("\\s+".toRegex())
            var isContainPhoneNumber = false
            for (word in words) {
                if (android.util.Patterns.PHONE.matcher(word).matches()) {
                    isContainPhoneNumber = true
                }
            }

            if (!isContainPhoneNumber) {
                removeIndices.add(index)
            }

        }

        for (index in removeIndices)
            itemList.removeAt(index)

        return itemList

    }

}