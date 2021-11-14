package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import at.fhooe.me.microproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

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
                binding.activitySignUpTexfieldPasswordConfirm.error = "Need to confirm your password"
            }
            if (firstName.isEmpty()) {
                binding.activitySignUpTexfieldFirstName.error = "First name can't be empty"
            }
            if (lastName.isEmpty()) {
                binding.activitySignUpTexfieldLastName.error = "Last name can't be empty"
            }
        } else {
            //TODO add first name and so on to database
            if (password.toString() == confirmPassword.toString()){
                mAuth.createUserWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    else{
                        Toast.makeText(this, "User registration failed Error:" + it.exception, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                binding.activitySignUpTexfieldPasswordConfirm.error = "Password doesn't match"
            }
        }
    }
}