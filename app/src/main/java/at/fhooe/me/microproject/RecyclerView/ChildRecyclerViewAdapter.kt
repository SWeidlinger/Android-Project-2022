package at.fhooe.me.microproject.RecyclerView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import at.fhooe.me.microproject.R
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChildRecyclerViewAdapter(
    private val items: ArrayList<String>,
    private val priority: String,
    private val cardView: MaterialCardView,
    private val toolBar: Menu
) : RecyclerView.Adapter<ChildRecyclerViewViewHolder>() {

    private lateinit var root: LayoutInflater
    private var mDb = Firebase.firestore
    private var mAuth = FirebaseAuth.getInstance()

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
        }else{
            toolBar.findItem(R.id.activity_main_menu_cheerMeUp).isVisible = false
        }

//        val fireBasePriority = convertPriorityNames(priority)
//        holder.mCardView.setOnLongClickListener{
//            mDb.collection("users").document(mAuth.uid.toString()).update(fireBasePriority,FieldValue.arrayRemove(items[position])).addOnSuccessListener { success ->
//                Toast.makeText(it.context, "deleted ${items.removeAt(position)} of ${priority}", Toast.LENGTH_SHORT).show()
//                cardView.isInvisible = items.isEmpty()
//                notifyDataSetChanged()
//                true
//            }.addOnFailureListener{ e ->
//                Toast.makeText(it.context, "deleting task ${items.removeAt(position)} of ${priority} failed", Toast.LENGTH_SHORT).show()
//                false
//            }
//            false
//        }
    }

    fun deleteItem(position: Int, holder: ChildRecyclerViewViewHolder) {
        val fireBasePriority = convertPriorityNames(priority)
        mDb.collection("users").document(mAuth.uid.toString())
            .update(fireBasePriority, FieldValue.arrayRemove(items[position]))
            .addOnSuccessListener { success ->
                Toast.makeText(
                    holder.mCardView.context,
                    "deleted ${items.removeAt(position)} of ${priority}",
                    Toast.LENGTH_SHORT
                ).show()
                cardView.isInvisible = items.isEmpty()
                notifyDataSetChanged()
                true
            }.addOnFailureListener { e ->
                Toast.makeText(
                    holder.mCardView.context,
                    "deleting task ${items[position]} of ${priority} failed",
                    Toast.LENGTH_SHORT
                ).show()
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