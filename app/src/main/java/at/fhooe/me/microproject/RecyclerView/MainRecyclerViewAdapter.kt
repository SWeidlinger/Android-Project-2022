package at.fhooe.me.microproject.RecyclerView

import android.R.attr
import android.view.LayoutInflater
import android.view.ViewGroup
import at.fhooe.me.microproject.R
import android.R.attr.numColumns
import android.graphics.Color
import androidx.recyclerview.widget.*

class MainRecyclerViewAdapter(private val sectionList: ArrayList<TaskData>, private val color: Int) :
    RecyclerView.Adapter<MainRecyclerViewViewHolder>() {

    private lateinit var root: LayoutInflater
    public var mSectionColor: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.activity_main_recyclerview_section, null)
            return MainRecyclerViewViewHolder(root);
        }
    }

    override fun onBindViewHolder(holder: MainRecyclerViewViewHolder, position: Int) {
        holder.mSectionTitle.text = sectionList[position].sectionTitle
        mSectionColor = color
        holder.mCardView.setCardBackgroundColor(mSectionColor)
        val items: ArrayList<String> = sectionList[position].sectionElements

        holder.mChildRecyclerView.adapter = ChildRecyclerViewAdapter(items, sectionList[position].sectionTitle)
        holder.mChildRecyclerView.layoutManager = GridLayoutManager(holder.mChildRecyclerView.context,1)

        val swipeGesture = object: SwipeGestures(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.LEFT ->{
                        val adapter = holder.mChildRecyclerView.adapter as ChildRecyclerViewAdapter
                        adapter.deleteItem(viewHolder.adapterPosition, sectionList[position].sectionTitle)
                    }

                    ItemTouchHelper.RIGHT->{
                        //TODO
                    }
                }
                super.onSwiped(viewHolder, direction)
            }
        }

        //add horizontal separation lines
//        holder.mChildRecyclerView.addItemDecoration(DividerItemDecoration(holder.mChildRecyclerView.context, DividerItemDecoration.VERTICAL))
    }

    override fun getItemCount(): Int = sectionList.size

}