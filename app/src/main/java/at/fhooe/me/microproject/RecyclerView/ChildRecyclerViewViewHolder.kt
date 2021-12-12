package at.fhooe.me.microproject.RecyclerView

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R

class ChildRecyclerViewViewHolder(root: View): RecyclerView.ViewHolder(root) {
    val mItemText: TextView

    init {
        mItemText = root.findViewById(R.id.activity_main_recyclerView_sectio_element_title)

        root.setOnClickListener{
            Toast.makeText(root.context, "selected ${mItemText.text}", Toast.LENGTH_SHORT).show()
        }
    }
}