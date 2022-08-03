package com.example.nearbyplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.nearbyplaces.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //loading animation for the bottom buttons
        val animation=AnimationUtils.loadAnimation(this,R.anim.bounce)

        binding.nearMeBtn.setOnClickListener{
            binding.nearMeBtn.startAnimation(animation)
            Toast.makeText(this,"searching nearby places",Toast.LENGTH_SHORT).show()
            try {
                val intent= Intent(this,MapsActivity::class.java)
                startActivity(intent)
            }catch (ex:Exception){}

        }

        binding.newsBtn.setOnClickListener{
            binding.newsBtn.startAnimation(animation)
            Toast.makeText(this,"loading news",Toast.LENGTH_SHORT).show()
            try {
                val intent= Intent(this,NewsActivity::class.java)
                startActivity(intent)
            }catch (ex:Exception){}
        }

        binding.peopleBtn.setOnClickListener {
            binding.peopleBtn.startAnimation(animation)
            Toast.makeText(this,"loading  posts",Toast.LENGTH_SHORT).show()
            try {
                val intent= Intent(this,PostActivity::class.java)
                startActivity(intent)
            }catch (ex:Exception){}
        }

        binding.meBtn.setOnClickListener {
            binding.meBtn.startAnimation(animation)
            try {
                val intent= Intent(this,meActivity::class.java)
                startActivity(intent)
            }catch (ex:Exception){}
        }

    }
}