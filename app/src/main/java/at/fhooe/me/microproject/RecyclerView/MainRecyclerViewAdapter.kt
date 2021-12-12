package at.fhooe.me.microproject.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R


class MainRecyclerViewAdapter(private val sectionList: ArrayList<TaskData>) :
    RecyclerView.Adapter<MainRecyclerViewViewHolder>() {

    private lateinit var root: LayoutInflater;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.activity_main_recyclerview_section, null)
            return MainRecyclerViewViewHolder(root);
        }
    }

    override fun onBindViewHolder(holder: MainRecyclerViewViewHolder, position: Int) {
        holder.mSectionTitle.text = sectionList[position].sectionTitle
        val items: ArrayList<String> = sectionList[position].sectionElements

        holder.mChildRecyclerView.adapter = ChildRecyclerViewAdapter(items)
        holder.mChildRecyclerView.layoutManager = GridLayoutManager(holder.mChildRecyclerView.context, 1)
        holder.mChildRecyclerView.addItemDecoration(DividerItemDecoration(holder.mChildRecyclerView.context, DividerItemDecoration.VERTICAL))
    }

    override fun getItemCount(): Int = sectionList.size

}