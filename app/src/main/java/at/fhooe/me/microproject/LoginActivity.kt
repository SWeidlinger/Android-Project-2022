package at.fhooe.me.microproject

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import at.fhooe.me.microproject.databinding.ActivityLoginBinding
import at.fhooe.me.microproject.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

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
                showForgotPasswordDialog()
            }
            else -> {
                Log.e(LOGIN, "unexpected id encountered")
            }
        }
    }

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
            binding.activityLoginProgressBar.isVisible = true
            mAuth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                        flag = true
                        //transition to main screen when logged in

                        binding.activityLoginProgressBar.isVisible = false
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        binding.activityLoginProgressBar.isVisible = false
                        Toast.makeText(this, "Login Failed: " + it.exception?.message, Toast.LENGTH_LONG)
                            .show()
                        flag = false
                    }
                }
        }
        return flag
    }

    private fun showForgotPasswordDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_forgot_password)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnBack = dialog.findViewById(R.id.dialog_forgotPassword_button_back) as Button
        val btnReset =
            dialog.findViewById(R.id.dialog_forgotPassword_button_resetPassword) as Button
        val email =
            dialog.findViewById(R.id.dialog_forgotPassword_textInputLayout_email) as TextInputLayout

        btnReset.setOnClickListener {
            if (email.editText?.text.isNullOrEmpty()) {
                email.error = "Email can't be empty!"
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.editText!!.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email sent!", Toast.LENGTH_LONG).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Sending Email failed!", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        btnBack.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}