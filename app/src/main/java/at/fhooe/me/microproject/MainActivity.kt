package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import at.fhooe.me.microproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            mAuth = FirebaseAuth.getInstance()
        binding.button.setOnClickListener{
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
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