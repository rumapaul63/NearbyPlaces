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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.nearbyplaces.databinding.ActivityMeBinding
import com.example.nearbyplaces.model.UserModel
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class meActivity : AppCompatActivity() {
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg:Uri
    private lateinit var dialog: AlertDialog.Builder

    private lateinit var binding: ActivityMeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog=AlertDialog.Builder(this)
            .setMessage("updating profile picture")
            .setCancelable(false)

        database=FirebaseDatabase.getInstance()
        storage=FirebaseStorage.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()


        binding.imageProfilePhoto.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,1)
        }


       /* binding.floatingActionButton.setOnClickListener{
            //TODO:select image from phone to set as profile photo
            ImagePicker.with(this)
                //...
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .cropSquare()
                .maxResultSize(1080,1080)
                .createIntentFromDialog { launcher.launch(it) }

        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data !=null){
            if(data.data !=null){
                selectedImg=data.data!!
                binding.imageProfilePhoto.setImageURI(selectedImg)
                if(selectedImg==null){
                    Toast.makeText(this,"please select an image",Toast.LENGTH_SHORT).show()
                }else{
                    saveDataToFirebase(selectedImg)
                }

            }
        }
    }

    private fun saveDataToFirebase(selectedImg: Uri) {
        val currentUser=FirebaseAuth.getInstance()
        val reference=storage.reference.child("Profile").child(currentUser.uid.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user=UserModel(firebaseAuth.uid.toString(),binding.userName.text.toString(), imgUrl)
        database.reference.child("users")
            .child(firebaseAuth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"data added",Toast.LENGTH_SHORT).show()
            }
    }


    /* private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {


         if (it.resultCode == Activity.RESULT_OK) {
             val uri = it.data?.data!!
             // Use the uri to load the image
             binding.imageProfilePhoto.setImageURI(uri)
             SaveImageInFirebase()
         }
     }

     fun SaveImageInFirebase(){
         //TODO : saving data to firebase

     }
     */




}