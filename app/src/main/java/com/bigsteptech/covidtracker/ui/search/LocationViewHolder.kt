package com.bigsteptech.covidtracker.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bigsteptech.covidtracker.R
import com.bigsteptech.covidtracker.databinding.ItemLocationBinding


class LocationViewHolder(
        private val viewBinding: ItemLocationBinding
) : RecyclerView.ViewHolder(viewBinding.button) {

    companion object {
        fun inflate(layoutInflater: LayoutInflater, parent: ViewGroup): LocationViewHolder {
            val binding = ItemLocationBinding.inflate(layoutInflater, parent, false)
            return LocationViewHolder(binding)
        }
    }

    interface OnClickListener {
        fun onClicked(viewHolder: LocationViewHolder)
    }

    init {
        itemView.setOnClickListener { onClick() }
    }

    private val backgroundResSelector: (Boolean) -> Int = { isActive ->
        if (isActive) {
            R.drawable.category_active_bg
        } else {
            R.drawable.category_inactive_bg
        }
    }

    private val textColorSelector: (Context, Boolean) -> Int = { context, isActive ->
        if (isActive) {
            ContextCompat.getColor(context, R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.category_inactive)
        }
    }

    private var clickListener: OnClickListener? = null

    fun bind(title: String) {
        viewBinding.button.text = title
    }

    fun setActive(active: Boolean) {
        viewBinding.button.setBackgroundResource(backgroundResSelector.invoke(active))
        viewBinding.button.setTextColor(textColorSelector.invoke(itemView.context, active))
    }

    fun setOnClickListener(callback: OnClickListener) {
        clickListener = callback
    }

    private fun onClick() {
        clickListener?.onClicked(this)
    }

}