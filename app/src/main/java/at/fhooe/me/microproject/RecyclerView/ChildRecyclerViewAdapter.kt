package at.fhooe.me.microproject.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R

class ChildRecyclerViewAdapter(private val items: ArrayList<String>) :RecyclerView.Adapter<ChildRecyclerViewViewHolder>() {

    private lateinit var root: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildRecyclerViewViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.activity_main_recyclerview_section_element, null)
            return ChildRecyclerViewViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ChildRecyclerViewViewHolder, position: Int) {
        holder.mItemText.text = items[position]
    }

    override fun getItemCount(): Int = items.size
}