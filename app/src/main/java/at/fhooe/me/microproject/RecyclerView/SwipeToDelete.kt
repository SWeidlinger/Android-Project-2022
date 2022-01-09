package at.fhooe.me.microproject.RecyclerView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R

class SwipeToDelete(var adapter: ChildRecyclerViewAdapter, context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var deleteBackground: ColorDrawable = ColorDrawable(Color.parseColor("#E41E1E"))
    private var finishedBackground: ColorDrawable = ColorDrawable(Color.parseColor("#4CAF50"))
    private var deleteIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_24)!!
    private var checkIcon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_24)!!

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        var position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.LEFT){
            adapter.deleteItem(position, viewHolder as ChildRecyclerViewViewHolder, true)
        } else{
            adapter.taskFinished(position, viewHolder as ChildRecyclerViewViewHolder, false)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        val iconMarginDelete = (itemView.height - deleteIcon.intrinsicHeight) / 2
        val iconMarginCheck = (itemView.height - checkIcon.intrinsicHeight) / 2

        //swipe right
        if (dX > 0) {
            finishedBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            checkIcon.setBounds(
                itemView.left + iconMarginCheck,
                itemView.top + iconMarginCheck,
                itemView.left + iconMarginCheck + checkIcon.intrinsicWidth,
                itemView.bottom - iconMarginCheck
            )
            finishedBackground.draw(c)
            checkIcon.draw(c)
        }
        //swipe left
        else {
            deleteBackground.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            deleteIcon.setBounds(
                itemView.right - iconMarginDelete - deleteIcon.intrinsicWidth,
                itemView.top + iconMarginDelete,
                itemView.right - iconMarginDelete,
                itemView.bottom - iconMarginDelete
            )
            deleteBackground.draw(c)
            deleteIcon.draw(c)
        }


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}