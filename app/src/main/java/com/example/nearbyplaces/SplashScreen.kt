package com.example.nearbyplaces

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.nearbyplaces.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var preference: SharedPreferences
    val pref_show_intro="Intro"
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference=getSharedPreferences("intro_screen",Context.MODE_PRIVATE)
        if(!preference.getBoolean(pref_show_intro,true)){
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //setting animation
        //val animation= AnimationUtils.loadAnimation(this,R.anim.bounce)

        binding.button11.setOnClickListener {
            //binding.button11.startAnimation(animation)
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
            val editor=preference.edit()
            editor.putBoolean(pref_show_intro,false)
            editor.apply()
            overridePendingTransition(0,0)
        }


    }
}