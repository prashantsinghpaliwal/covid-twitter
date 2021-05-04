package com.example.covidtracker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(
        private val onCategoryChecked: (Set<String>) -> Unit,
) : RecyclerView.Adapter<LocationViewHolder>() {

    internal var items = emptyList<String>()

    private var checked: String? = null
    private val checkedIds = mutableSetOf<Int>()
    private val checkedNames = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.inflate(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        if (items.isEmpty()) return
        val item = items[position]
        holder.bind(item)
        if (checkedIds.contains(position)) {
            holder.setActive(true)
        } else {
            holder.setActive(false)
        }
        holder.setOnClickListener(object : LocationViewHolder.OnClickListener {
            override fun onClicked(viewHolder: LocationViewHolder) {
                onItemClickListener(viewHolder)
            }
        })
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun set(newItems: List<String>, diffUtilsCallbackProducer: (List<String>, List<String>) -> DiffUtil.Callback) {
        val diffResult = DiffUtil.calculateDiff(diffUtilsCallbackProducer.invoke(this.items, newItems))
        this.items = newItems.toMutableList()
        diffResult.dispatchUpdatesTo(this)
    }

    fun set(newItems: List<String>) {
        this.items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    private fun onItemClickListener(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        val previousPosition = if (checked != null) items.indexOf(checked) else RecyclerView.NO_POSITION
        if (previousPosition > RecyclerView.NO_POSITION) {
            notifyItemChanged(previousPosition)
        }

        checked = items[position].trim()

        val newPosition = if (checked != null) items.indexOf(checked) else RecyclerView.NO_POSITION
        if (newPosition > RecyclerView.NO_POSITION) {
            notifyItemChanged(newPosition)
        }

        if (checkedIds.contains(position)) {
            checkedIds.remove(position)
            checkedNames.remove(checked)
        } else {
            checkedIds.add(position)
            checked?.let(checkedNames::add)
        }

        onCategoryChecked.invoke(checkedNames)
    }

    fun getCheckedNames() = checkedNames

    fun addChecked(category: String) {
        val position = items.indexOf(category)
        checked = items[position].trim()

        if (position > 0) {
            if (checkedIds.contains(position)) {
                checkedIds.remove(position)
                checkedNames.remove(checked)
            } else {
                checkedIds.add(position)
                checked?.let(checkedNames::add)
            }
            notifyItemChanged(position)
        }
        onCategoryChecked.invoke(checkedNames)
    }

    fun setActive(categories: List<String>) {
        categories.forEach {
            val index = items.indexOf(it)
            checkedIds.add(index)
            checkedNames.add(it)
            notifyItemChanged(index)
        }
    }

}