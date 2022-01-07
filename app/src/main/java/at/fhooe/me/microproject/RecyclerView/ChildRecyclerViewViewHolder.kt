package at.fhooe.me.microproject.RecyclerView

import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView

class ChildRecyclerViewViewHolder(root: View): RecyclerView.ViewHolder(root) {
    lateinit var mItemText: TextView
    lateinit var mCardView: MaterialCardView

    init {
        mItemText = root.findViewById(R.id.activity_main_recyclerView_section_element_title)
        mCardView = root.findViewById(R.id.activity_main_recyclerView_section_element_cardView)

        mCardView.setOnClickListener{
            mCardView.isChecked = !mCardView.isChecked
            if (mCardView.isChecked){
                Toast.makeText(root.context, "Great Job you finished: ${mItemText.text}", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(root.context, "unselected ${mItemText.text}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}