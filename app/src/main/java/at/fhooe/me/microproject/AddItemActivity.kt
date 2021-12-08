package at.fhooe.me.microproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.fhooe.me.microproject.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}