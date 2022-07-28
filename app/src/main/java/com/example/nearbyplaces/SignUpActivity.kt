package com.example.nearbyplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.nearbyplaces.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.textView12.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.button.setOnClickListener {
            val email=binding.editTextTextEmailAddress.text.toString()
            val password=binding.editTextTextPassword.text.toString()
            val confirmPassword=binding.editTextTextPassword2.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if(password.equals(confirmPassword)){
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"SignUp successful", Toast.LENGTH_SHORT).show()
                            val intent=Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                        }else{
                            Toast.makeText(this,"SignUp Failed !",Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    Toast.makeText(this,"password mismatch !!",Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Above fields can't be empty !!",Toast.LENGTH_SHORT).show()
            }
        }

    }




}