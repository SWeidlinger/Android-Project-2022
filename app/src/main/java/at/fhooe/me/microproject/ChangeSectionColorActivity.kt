package at.fhooe.me.microproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import at.fhooe.me.microproject.databinding.ActivityChangeSectionColorBinding

class ChangeSectionColorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeSectionColorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeSectionColorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}