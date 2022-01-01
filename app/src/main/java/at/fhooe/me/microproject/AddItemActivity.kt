package at.fhooe.me.microproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import at.fhooe.me.microproject.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore
    private var priority = "priorityA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.activityAddItemToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.activityAddItemCardViewPriorityA.isChecked = true

        binding.activityAddItemCardViewPriorityA.setOnClickListener{
            unselectAllCardViews()
            binding.activityAddItemCardViewPriorityA.isChecked = true
            priority = "priorityA"
        }
        binding.activityAddItemCardViewPriorityB.setOnClickListener{
            unselectAllCardViews()
            binding.activityAddItemCardViewPriorityB.isChecked = true
            priority = "priorityB"
        }
        binding.activityAddItemCardViewPriorityC.setOnClickListener{
            unselectAllCardViews()
            binding.activityAddItemCardViewPriorityC.isChecked = true
            priority = "priorityC"
        }
        binding.activityAddItemCardViewPriorityD.setOnClickListener{
            unselectAllCardViews()
            binding.activityAddItemCardViewPriorityD.isChecked = true
            priority = "priorityD"
        }

        binding.activityAddItemButtonAddTask.setOnClickListener {
            if (binding.activityAddItemTextfieldTask.text.isEmpty()) {
                binding.activityAddItemTextfieldTask.error = "Please type in your Task!";
            } else {
                //adding task to the database
                mDb.collection("users").document(mAuth.uid.toString()).update(
                    priority,
                    FieldValue.arrayUnion(binding.activityAddItemTextfieldTask.text.toString())
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //flag Activity Clear Top, so the intent of the main activity from before is cleared from the backstack
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        Toast.makeText(this, "Added task!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Adding task failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    fun unselectAllCardViews(){
        binding.activityAddItemCardViewPriorityA.isChecked = false
        binding.activityAddItemCardViewPriorityB.isChecked = false
        binding.activityAddItemCardViewPriorityC.isChecked = false
        binding.activityAddItemCardViewPriorityD.isChecked = false
    }
}