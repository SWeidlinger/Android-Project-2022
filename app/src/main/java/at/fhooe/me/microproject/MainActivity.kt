package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
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

        //setting action Bar
        binding.activityMainActionBar.showOverflowMenu()
        setSupportActionBar(binding.activityMainActionBar)

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
    }

    //populating ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //assigning Function to ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var txt: String? = null
        when (item.itemId) {
            R.id.activity_main_menu_add -> {
                startActivity(Intent(this, AddItemActivity::class.java))
            }
            R.id.activity_main_menu_changePassword -> {
                startActivity(Intent(this, ChangePasswordActivity::class.java))
            }
            R.id.activity_main_menu_changeSectionColor -> {
                startActivity(Intent(this, ChangeSectionColorActivity::class.java))
            }
            R.id.activity_main_menu_help -> {
                txt = "HELP!!!"
            }
            R.id.activity_main_menu_signOut -> {
                txt = "User signed out"
                mAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            android.R.id.home -> {
                txt = "HOME touched"
            }

            else -> {
                Log.e(
                    "",
                    "MainActivity::onOptionsItemsSelected ... unhandled ID ${item.itemId} encountered"
                )
            }
        }
        txt?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    //check if user is already logged in
    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = mAuth.currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}