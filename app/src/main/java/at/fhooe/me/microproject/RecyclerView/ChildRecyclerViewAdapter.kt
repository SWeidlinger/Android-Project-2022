package at.fhooe.me.microproject.RecyclerView

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.FieldPosition

class ChildRecyclerViewAdapter(
    private val items: ArrayList<String>,
    private val priority: String,
    private val cardView: MaterialCardView,
    private val toolBar: Menu,
    private val parentView: ConstraintLayout
) : RecyclerView.Adapter<ChildRecyclerViewViewHolder>() {

    private lateinit var root: LayoutInflater
    private var mDb = Firebase.firestore
    private var mAuth = FirebaseAuth.getInstance()
    private var removedPosition = arrayListOf<Int>()
    private var removedItem = arrayListOf<String>()
    private var finishedPosition = arrayListOf<Int>()
    private var finishedItem = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildRecyclerViewViewHolder {
        LayoutInflater.from(parent.context).apply {
            val root = inflate(R.layout.activity_main_recyclerview_section_element, null)
            return ChildRecyclerViewViewHolder(root)
        }
    }

    override fun onBindViewHolder(holder: ChildRecyclerViewViewHolder, position: Int) {
        holder.mItemText.text = items[position]

        if (priority.equals("Priority A") && items.size > 2) {
            toolBar.findItem(R.id.activity_main_menu_cheerMeUp).isVisible = true
        } else if (priority.equals("Priority A")) {
            toolBar.findItem(R.id.activity_main_menu_cheerMeUp).isVisible = false
        }

        holder.mCardView.setOnLongClickListener {
            Toast.makeText(
                holder.mCardView.context,
                "Swipe left or right to interact with your tasks!",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }

    fun taskFinished(position: Int, holder: ChildRecyclerViewViewHolder, showToast: Boolean) {
        finishedPosition.add(position)
        finishedItem.add(items[position])
        items.removeAt(position)
        notifyDataSetChanged()
        cardView.isInvisible = items.isEmpty()

        var snackBar =
            Snackbar.make(parentView, "Great job you finished your Task!", Snackbar.LENGTH_SHORT)
                .setAction("UNDO") {
                    items.add(finishedPosition.first(), finishedItem.first())
                    finishedPosition.removeAt(0)
                    finishedItem.removeAt(0)
                    cardView.isInvisible = items.isEmpty()
                    (parentView.findViewById(R.id.activity_main_add_button) as FloatingActionButton).isVisible = true
                    notifyDataSetChanged()
                }
        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                    removeFromFirebase(holder, finishedItem, finishedPosition, false)
                    (parentView.findViewById(R.id.activity_main_add_button) as FloatingActionButton).isVisible = true
                    super.onDismissed(transientBottomBar, event)
                }
            }
        })
        (parentView.findViewById(R.id.activity_main_add_button) as FloatingActionButton).isVisible = false
        snackBar.show()
    }

    fun deleteItem(position: Int, holder: ChildRecyclerViewViewHolder, showToast: Boolean) {
        removedPosition.add(position)
        removedItem.add(items[position])
        items.removeAt(position)
        notifyDataSetChanged()
        cardView.isInvisible = items.isEmpty()

        var snackBar =
            Snackbar.make(parentView, "Task deleted!", Snackbar.LENGTH_SHORT)
                .setAction("UNDO") {
                    items.add(removedPosition.first(), removedItem.first())
                    removedPosition.removeAt(0)
                    removedItem.removeAt(0)
                    cardView.isInvisible = items.isEmpty()
                    (parentView.findViewById(R.id.activity_main_add_button) as FloatingActionButton).isVisible = true
                    notifyDataSetChanged()
                }
        snackBar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == DISMISS_EVENT_TIMEOUT || event == DISMISS_EVENT_CONSECUTIVE) {
                    removeFromFirebase(holder, removedItem, removedPosition, false)
                    (parentView.findViewById(R.id.activity_main_add_button) as FloatingActionButton).isVisible = true
                    super.onDismissed(transientBottomBar, event)
                }
            }
        })
        (parentView.findViewById(R.id.activity_main_add_button) as FloatingActionButton).isVisible = false
        snackBar.show()
    }

    private fun removeFromFirebase(
        holder: ChildRecyclerViewViewHolder,
        item: ArrayList<String>,
        itemPosition: ArrayList<Int>,
        showToast: Boolean
    ) {
        val fireBasePriority = convertPriorityNames(priority)
        mDb.collection("users").document(mAuth.uid.toString())
            .update(fireBasePriority, FieldValue.arrayRemove(item.first()))
            .addOnSuccessListener { success ->
                if (showToast) {
                    Toast.makeText(
                        holder.mCardView.context,
                        "deleted ${item.first()} of $priority",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                item.removeAt(0)
                itemPosition.removeAt(0)
                cardView.isInvisible = items.isEmpty()
                true
            }.addOnFailureListener { e ->
                if (showToast) {
                    Toast.makeText(
                        holder.mCardView.context,
                        "deleting task failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false
            }
    }

    private fun convertPriorityNames(priority: String): String {
        when (priority) {
            "Priority A" -> (return "priorityA")
            "Priority B" -> (return "priorityB")
            "Priority C" -> (return "priorityC")
            "Priority D" -> (return "priorityD")
        }
        return ""
    }

    override fun getItemCount(): Int = items.size
}