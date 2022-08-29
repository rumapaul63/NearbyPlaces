package com.example.nearbyplaces

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.nearbyplaces.databinding.ActivitySignUpBinding
import com.example.nearbyplaces.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    var selectedImg: Uri?=null
    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog=AlertDialog.Builder(this)
            .setMessage("updating profile picture")
            .setCancelable(false)

        database=FirebaseDatabase.getInstance()
        storage=FirebaseStorage.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()



        binding.ivProfilePic.setOnClickListener {
            val intent=Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,1)
        }


        binding.textView12.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
           finish()
            overridePendingTransition(0,0)
        }



        binding.button.setOnClickListener {
            val email=binding.editTextTextEmailAddress.text.toString()
            val password=binding.editTextTextPassword.text.toString()
            val confirmPassword=binding.editTextTextPassword2.text.toString()
            val name=binding.etName.text.toString()
            val phone=binding.etPhoneNumber.text.toString()
            binding.progressSignUp.visibility=View.VISIBLE

            if(phone.isNotEmpty() && name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() ){

                if(password.equals(confirmPassword)){
                       firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                           if(it.isSuccessful){
                               binding.progressSignUp.visibility=View.GONE

                               saveDataToFirebase()
                               Toast.makeText(this, "SignUp successful", Toast.LENGTH_SHORT).show()
                               val intent = Intent(this, LoginActivity::class.java)

                               //saving data to shared preference
                               val sharedPreference =  this.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
                               var editor = sharedPreference.edit()
                               editor.putString("username", name)
                               // editor.putLong("l",100L)
                               editor.commit()

                               startActivity(intent)

                           }else{
                               binding.progressSignUp.visibility=View.GONE
                               Toast.makeText(this,"SignUp Failed !",Toast.LENGTH_SHORT).show()
                           }
                       }

                }else{
                    binding.progressSignUp.visibility=View.GONE
                    Toast.makeText(this,"password mismatch !!",Toast.LENGTH_SHORT).show()
                }

            }else{
                binding.progressSignUp.visibility=View.GONE
                Toast.makeText(this,"Above fields and profile photo can't be empty !!",Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data !=null){
            if(data.data !=null){
                selectedImg=data.data!!
                binding.ivProfilePic.setImageURI(selectedImg)
            }
        }
    }

    private fun saveDataToFirebase() {
        val reference=storage.reference.child("Profile").child(firebaseAuth.uid.toString())
        reference.putFile(selectedImg!!).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user= UserModel(firebaseAuth.uid.toString(),binding.etName.text.toString(), imgUrl,binding.etPhoneNumber.text.toString())
        database.reference.child("users")
            .child(firebaseAuth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"profile set successfully",Toast.LENGTH_SHORT).show()
            }
    }








}