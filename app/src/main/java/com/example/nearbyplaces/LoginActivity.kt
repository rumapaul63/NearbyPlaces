package com.example.nearbyplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.example.nearbyplaces.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.registerTxt.setOnClickListener{
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email=binding.etmail.text.toString()
            val password=binding.etpassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"Login successful", Toast.LENGTH_SHORT).show()
                            val intent=Intent(this,HomePageActivity::class.java)
                            startActivity(intent)
                            finish()

                        }else{
                            Toast.makeText(this,"invalid email or password !", Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(this,"Above fields can't be empty !!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser!=null){
            val intent=Intent(this,HomePageActivity::class.java)
            startActivity(intent)
        }
    }
    fun SplitString(email:String):String{
        val split=email.split("@")
        return split[0]
    }


}