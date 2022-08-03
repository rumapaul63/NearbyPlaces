package com.example.nearbyplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.nearbyplaces.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting animation
        val animation= AnimationUtils.loadAnimation(this,R.anim.bounce)

        binding.button11.setOnClickListener {
            binding.button11.startAnimation(animation)
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}