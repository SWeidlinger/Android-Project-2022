package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import at.fhooe.me.microproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var mAuth = FirebaseAuth.getInstance()
    private var mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activitySignUpButtonSignUp.setOnClickListener {
            createUser()
        }
    }

    //to create a new user
    private fun createUser() {
        var email = binding.activitySignUpTexfieldEmail.text
        var password = binding.activitySignUpTexfieldPassword.text
        var confirmPassword = binding.activitySignUpTexfieldPasswordConfirm.text
        var firstName = binding.activitySignUpTexfieldFirstName.text
        var lastName = binding.activitySignUpTexfieldLastName.text

        //TODO Ugly code
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            if (email.isEmpty()) {
                binding.activitySignUpTexfieldEmail.error = "Email can't be empty"
            }
            if (password.isEmpty()) {
                binding.activitySignUpTexfieldPassword.error = "Password can't be empty"
            }
            if (confirmPassword.isEmpty()) {
                binding.activitySignUpTexfieldPasswordConfirm.error =
                    "Need to confirm your password"
            }
            if (firstName.isEmpty()) {
                binding.activitySignUpTexfieldFirstName.error = "First name can't be empty"
            }
            if (lastName.isEmpty()) {
                binding.activitySignUpTexfieldLastName.error = "Last name can't be empty"
            }
        } else {
            if (password.toString() == confirmPassword.toString()) {
                mAuth.createUserWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //safe user in hashMap to then insert into the database
                            val user = hashMapOf(
                                "firstName" to firstName.toString(),
                                "lastName" to lastName.toString(),
                                "uid" to mAuth.uid
                            )
                            //get the user ID of the user and name the document in the database like that
                            mDb.collection("users").document(mAuth.uid.toString()).set(user)
                            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            Toast.makeText(
                                this,
                                "User registration failed Error:" + it.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                binding.activitySignUpTexfieldPasswordConfirm.error = "Password doesn't match"
            }
        }
    }
}