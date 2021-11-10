package at.fhooe.me.microproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import at.fhooe.me.microproject.databinding.ActivityLoginBinding
import at.fhooe.me.microproject.databinding.ActivitySignUpBinding

const val LOGIN = "LoginActivity"
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            activityLoginButtonLogin.setOnClickListener(this@LoginActivity)
            activityLoginButtonSignup.setOnClickListener(this@LoginActivity)
            activityLoginButtonForgotPassword.setOnClickListener(this@LoginActivity)
        }
    }

    override fun onClick(_view: View?) {
        var i: Intent? = null
        when(_view?.id){
            R.id.activity_login_button_login -> {i = Intent(_view.context, MainActivity::class.java)}
            R.id.activity_login_button_signup -> {i = Intent(_view.context, SignUpActivity::class.java)}
            R.id.activity_login_button_forgotPassword -> {i = Intent(_view.context, SignUpActivity::class.java)}
            else ->{
                Log.e(LOGIN, "unexpected id encountered")
            }
        }
        startActivity(i)
    }
}