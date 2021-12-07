package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import at.fhooe.me.microproject.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        //getting the data from the database and populating the textView for the name of the user
        val userData =
            mDb.collection("users").document(mAuth.uid.toString()).get().addOnCompleteListener() {
                if (it.isSuccessful) {
                    binding.activityMainTextViewUsername.text =
                        it.getResult()!!["firstName"].toString() + "'s S****"
                } else {
                    binding.activityMainTextViewUsername.text = "User's S****"
                }
            }
        supportActionBar?.title = "Sebastian's S****"
    }

    //check if user is already logged in
    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = mAuth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}