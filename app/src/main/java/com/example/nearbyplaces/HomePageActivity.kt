package com.example.nearbyplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.nearbyplaces.databinding.ActivityHomePageBinding
import kotlinx.android.synthetic.main.activity_maps.*

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)





        //now showing dummy data to slider image,
        val imageList=ArrayList<SlideModel>()

        imageList.add(SlideModel("https://cdn.britannica.com/43/156343-050-CD194769/pilgrims-Muslim-Kabah-Great-Mosque-of-Mecca.jpg","Mecca, Saudi Arabia"))
        imageList.add(SlideModel("https://www.educationworld.in/wp-content/uploads/2018/11/amritsar-3083693_960_720-500x333.jpg","golden temple, Amritsar"))
        imageList.add(SlideModel("https://images.hindustantimes.com/rf/image_size_630x354/HT/p2/2020/01/13/Pictures/_70556bce-35d4-11ea-8a26-bda02fe1f8d7.jpg","kashi Vishwanath Temple, Varanasi"))
        imageList.add(SlideModel("https://www.tourmyindia.com/blog//wp-content/uploads/2021/04/Sun-Temple-in-Konark-Odisha.jpg","sun temple, Odisha"))

        binding.imageSlider.setImageList(imageList,ScaleTypes.FIT)




        //loading animation for the bottom buttons
        val animation=AnimationUtils.loadAnimation(this,R.anim.bounce)
        binding.nearMeBtn.setOnClickListener{
            binding.nearMeBtn.startAnimation(animation)
            Toast.makeText(this,"searching nearby places",Toast.LENGTH_SHORT).show()
            try {
                val intent= Intent(this,MapsActivity::class.java)
                startActivity(intent)
                finish()
            }catch (ex:Exception){}

        }

        binding.newsBtn.setOnClickListener{
            binding.newsBtn.startAnimation(animation)
            Toast.makeText(this,"loading news",Toast.LENGTH_SHORT).show()
            try {
                val intent= Intent(this,NewsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
            }catch (ex:Exception){}
        }

        binding.peopleBtn.setOnClickListener {
            binding.peopleBtn.startAnimation(animation)
            Toast.makeText(this,"loading posts, please wait...",Toast.LENGTH_LONG).show()
            try {
                //TODO: change back to PostActivity
                val intent= Intent(this,PostActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
            }catch (ex:Exception){}
        }

        binding.meBtn.setOnClickListener {
            binding.meBtn.startAnimation(animation)
            try {
                val intent= Intent(this,meActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
            }catch (ex:Exception){}
        }

    }
}