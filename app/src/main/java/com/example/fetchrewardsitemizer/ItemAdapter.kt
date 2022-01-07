package com.example.fetchrewardsitemizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

/*
 * The ItemAdapter class adapts a list of items to the recycler view.
 */
class ItemAdapter(
    private val items: MutableList<Item>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //
    // Methods
    //
    fun addItem(item : Item) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val curItem = items[position]

        holder.itemView.apply {
            idTextView.text = curItem.id.toString()
            listIdTextView.text = curItem.listId.toString()
            nameTextView.text = if (curItem.name != null && curItem.name.isNotEmpty()) curItem.name else "N/A"
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}