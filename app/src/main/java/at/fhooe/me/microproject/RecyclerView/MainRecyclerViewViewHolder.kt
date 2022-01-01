package at.fhooe.me.microproject.RecyclerView

import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R

class MainRecyclerViewViewHolder(root: View) : RecyclerView.ViewHolder(root){
    val mSectionTitle: TextView
    val mChildRecyclerView: RecyclerView
    val mCardView: CardView

    init{
        mSectionTitle = root.findViewById(R.id.activity_main_recyclerView_section_title)
        mChildRecyclerView = root.findViewById(R.id.activity_main_recyclerView_childRecyclerView)
        mCardView = root.findViewById(R.id.activity_main_recyclerview_section_cardView)

        //TODO this could be used for the cheer me up button
//        root.setOnClickListener{
//            Toast.makeText(root.context, "selected ${mSectionTitle.text}", Toast.LENGTH_SHORT).show()
//        }
    }
}