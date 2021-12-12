package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityAddItemButtonAddTask.setOnClickListener {
            if (binding.activityAddItemTextfieldTask.text.isEmpty()) {
                binding.activityAddItemTextfieldTask.error = "Please type in your Task!";
            } else {
                //adding task to the database
                //TODO adding which priority its inserted into
                mDb.collection("users").document(mAuth.uid.toString()).update(
                    "priorityA",
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
}