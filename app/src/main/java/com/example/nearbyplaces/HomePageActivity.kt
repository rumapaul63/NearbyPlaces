package com.example.nearbyplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nearbyplaces.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nearMeBtn.setOnClickListener{
            val intent= Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }

    }
}