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
    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        mAuth = FirebaseAuth.getInstance()
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
                loginUser()
            }
            R.id.activity_login_button_signup -> {
                i = Intent(_view.context, SignUpActivity::class.java)
                startActivity(i)
            }
            R.id.activity_login_button_forgotPassword -> {
                i = Intent(_view.context, ForgotPasswordActivity::class.java)
                startActivity(i)
            }
            else -> {
                Log.e(LOGIN, "unexpected id encountered")
            }
        }
    }

    //TODO handle when user logs in with not the right email
    //checking if all fields are populated and creating a toast message
    private fun loginUser(): Boolean {
        var email = binding.activityLoginTextInputLayoutEmail.editText?.text
        var password = binding.activityLoginTextInputLayoutPassword.editText?.text
        var flag: Boolean = false
        if (email?.isEmpty() == true || password?.isEmpty() == true) {
            if (email?.isEmpty() == true) {
                binding.activityLoginTextInputLayoutEmail.error = "Email can't be empty"
            }
            if (password?.isEmpty() == true) {
                binding.activityLoginTextInputLayoutPassword.error = "Password can't be empty"
            }
        } else {
            mAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                        flag = true
                        //transition to main screen when logged in
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
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