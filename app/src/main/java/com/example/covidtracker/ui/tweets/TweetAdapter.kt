package com.example.covidtracker.ui.tweets

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covidtracker.common.Utils
import com.example.covidtracker.common.addOnClickListener
import com.example.covidtracker.common.toUtc
import com.example.covidtracker.data.remote.Tweet
import com.example.covidtracker.databinding.ItemTweetBinding

class TweetAdapter(
    private val itemClickCallback: ItemClickCallback
) : RecyclerView.Adapter<TweetAdapter.TweetViewHolder>() {


    private var itemList = mutableListOf<Tweet>()

    interface ItemClickCallback {
        fun onItemClicked(item: Tweet)
        fun onProfileClicked(item: Tweet)
        fun onShareItemClicked(item: Tweet)
    }


    inner class TweetViewHolder(var binding: ItemTweetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder =
        TweetViewHolder(
            ItemTweetBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val tweet = itemList[position]
        with(holder.binding) {
            tweetText.text = tweet.tweetData.text
            profileName.text = tweet.userData.name
            profileUserName.text = "@${tweet.userData.userName}"
            Glide.with(profilePhoto.context).load(tweet.userData.profileImageUrl).into(profilePhoto)
            tweetTime.text = "• ${Utils.getAgoTimeString(tweet.tweetData.createdAt?.toUtc()!!)}"

            group.addOnClickListener { v ->
                itemClickCallback.onProfileClicked(tweet)
            }

            shareBtn.setOnClickListener {
                itemClickCallback.onShareItemClicked(tweet)
            }

        }
    }

    fun setData(itemList: MutableList<Tweet>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun updateData(itemList: MutableList<Tweet>) {
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    private fun splitWords(text: String, position: Int): String {
        val words = text.toLowerCase().split("\\s+".toRegex())
        Log.v("TweetLogs", "words : ${words}")

        val tweetText = StringBuilder()
        for (word in words) {
            val wordToSpan = SpannableString(word)
            if (android.util.Patterns.PHONE.matcher(word).matches()) {
                wordToSpan.setSpan(
                    ForegroundColorSpan(Color.BLUE),
                    0,
                    word.length - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )


            } else {
                wordToSpan.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    0,
                    word.length - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            tweetText.append("$wordToSpan ")
        }

        return tweetText.toString()
    }


}