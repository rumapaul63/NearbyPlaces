package com.example.nearbyplaces

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.nearbyplaces.databinding.ActivityMeBinding
import com.example.nearbyplaces.model.UserModel
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_me.*
import kotlinx.android.synthetic.main.posts_card.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class meActivity : AppCompatActivity() {
    private lateinit var firebaseAuth:FirebaseAuth
    private var database:FirebaseDatabase=FirebaseDatabase.getInstance()
    private var myRef=database.reference
    private lateinit var selectedImg:Uri
    private lateinit var dialog: AlertDialog.Builder

    private lateinit var binding: ActivityMeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        database=FirebaseDatabase.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()

        LoadProfileInfo()

    }


fun LoadProfileInfo(){
    myRef.child("users")
        .child(firebaseAuth.uid.toString())
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                try {

                    var td=snapshot.value as HashMap<String,Any>
                    for(key in td.keys){
                        var userInfo=td[key] as String
                        if(key.equals("imageUrl")){
                            Picasso.get().load(userInfo).into(imageProfilePhoto);

                        }else if(key.equals("name")){
                            userName.setText(userInfo)
                        }
                        progressBarMe.visibility=View.GONE
                    }



                }catch (ex:Exception){}
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
}





}