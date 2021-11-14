package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import at.fhooe.me.microproject.databinding.ActivityLoginBinding
import at.fhooe.me.microproject.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

const val LOGIN = "LoginActivity"

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            activityLoginButtonLogin.setOnClickListener(this@LoginActivity)
            activityLoginButtonSignup.setOnClickListener(this@LoginActivity)
            activityLoginButtonForgotPassword.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(_view: View?) {
        var i: Intent? = null
        when (_view?.id) {
            R.id.activity_login_button_login -> {
                if (loginUser()) {
                    i = Intent(_view.context, MainActivity::class.java)
                    startActivity(i)
                }
            }
            R.id.activity_login_button_signup -> {
                i = Intent(_view.context, SignUpActivity::class.java)
                startActivity(i)
            }
            R.id.activity_login_button_forgotPassword -> {
                i = Intent(_view.context, SignUpActivity::class.java)
                startActivity(i)
            }
            else -> {
                Log.e(LOGIN, "unexpected id encountered")
            }
        }
    }

    private fun loginUser(): Boolean {
        var email = binding.activityLoginTextfieldEmail.text
        var password = binding.activityLoginTextfieldPassword.text
        var flag: Boolean = false

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                binding.activityLoginTextfieldEmail.error = "Email can't be empty"
            }
            if (password.isEmpty()) {
                binding.activityLoginTextfieldPassword.error = "Password can't be empty"
            }
        } else {
            mAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                        flag = true
                    } else {
                        Toast.makeText(this, "Login Failed:" + it.exception, Toast.LENGTH_SHORT)
                            .show()
                        flag = false
                    }
                }
        }
        return flag
    }
}