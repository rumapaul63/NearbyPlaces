package com.example.nearbyplaces

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.nearbyplaces.databinding.ActivityPostBinding
import com.example.nearbyplaces.model.PostTicket
import com.example.nearbyplaces.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.view.CropImageView
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.posts_card.*
import kotlinx.android.synthetic.main.posts_card.view.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PostActivity : AppCompatActivity() {
    lateinit var myPref:SharedPreferences
    var name:String?=null

    private lateinit var binding:ActivityPostBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var database:FirebaseDatabase=FirebaseDatabase.getInstance()
    private var myRef=database.reference
    private lateinit var storage: FirebaseStorage

    private var selectedImg: Uri?=null
    private lateinit var dialog: AlertDialog.Builder


    var adapter:MyPostAdapter?=null

    var ListOfPosts=ArrayList<PostTicket>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LoadLikes()
        LoadDislikes()
        myPref=this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        //retrieving data from shared preference
        name= myPref.getString("username","no name").toString()
       // sharedPreference.getLong("l",1L)



        database=FirebaseDatabase.getInstance()
        storage=FirebaseStorage.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()


        dialog=AlertDialog.Builder(this)
            .setMessage("updating profile picture")
            .setCancelable(false)




        binding.attachPhoto.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,11)
        }


        binding.addPostBtn.setOnClickListener {
            cardViewPost.visibility=View.VISIBLE
            addPostBtn.visibility=View.GONE
            hidePostBtn.visibility=View.VISIBLE
        }
        binding.hidePostBtn.setOnClickListener {
            cardViewPost.visibility=View.GONE
            hidePostBtn.visibility=View.GONE
            addPostBtn.visibility=View.VISIBLE
        }
///////////////////////////////////////////////////////////////////////////////////////////////



        binding.postBtn.setOnClickListener {
            cardViewPost.visibility=View.GONE
            hidePostBtn.visibility=View.GONE
            addPostBtn.visibility=View.VISIBLE
            binding.attachText.clearFocus()
            closeKeyboard(binding.attachText)

            if ( selectedImg!=null && binding.attachText.text.isNotEmpty()) {
                binding.photoStatusText.visibility=View.GONE
                Toast.makeText(this, "please wait, post is being uploaded", Toast.LENGTH_SHORT).show()
                saveDataToFirebase()
                attachPhoto.setImageURI(null)    //TODO: This needs to checked !

            } else {
                Toast.makeText(this, "you might have missed attaching photo or description", Toast.LENGTH_LONG).show()
                cardViewPost.visibility=View.VISIBLE
            }
        }




        adapter=MyPostAdapter(this, ListOfPosts)
        binding.postListView.adapter=adapter

        LoadPost()
    }

    private fun closeKeyboard(view: View) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    ////////////////////////////////////////////////////////////////////////////////

    inner class MyPostAdapter: BaseAdapter {
        var adapterListOfPosts=ArrayList<PostTicket>()
        var context:Context?=null
        constructor(context: Context,adapterListOfPosts:ArrayList<PostTicket>):super(){
            this.adapterListOfPosts=adapterListOfPosts
            this.context=context
        }

        override fun getCount(): Int {
            return adapterListOfPosts.size
        }

        override fun getItem(position: Int): Any {
            return adapterListOfPosts[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var mypost=adapterListOfPosts[position]


            var myView=layoutInflater.inflate(R.layout.posts_card,null)

            if(myView!=null){
                progressBar.visibility=View.GONE
            }

            myView.username.setText(mypost.username)
            Picasso.get().load(mypost.postImageURL).into(myView.postPic);
            myView.description.setText(mypost.postDes)

           //TODO: here i am adding the features of like and dislike options

                myView.likeIcon.setOnClickListener {
                    Toast.makeText(context,"you liked this review",Toast.LENGTH_SHORT).show()
                    myRef
                        .child("likes_node")
                        .push()
                        .setValue(firebaseAuth.currentUser!!.uid)
                        .addOnSuccessListener {
                            //TODO: integrate animation in like button

                        }
                    myView.likeIcon.isEnabled=false
                }


                myView.dislikeIcon.setOnClickListener {
                    Toast.makeText(context,"you disliked this review",Toast.LENGTH_SHORT).show()
                    myRef
                        .child("dislikes_node")
                        .push()
                        .setValue(firebaseAuth.currentUser!!.uid)
                        .addOnSuccessListener {
                            //TODO: integrate animation in dislike button
                        }

                    myView.dislikeIcon.isEnabled=false


                }










            //TODO: changing the name and image field to poster's data.

//            myRef.child("users")
//                .child(firebaseAuth.uid.toString())
//                .addValueEventListener(object :ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//
//                        try {
//                            var td=snapshot.value as HashMap<String,Any>
//                            for(key in td.keys){
//                                var userInfo=td[key] as String
//                                if(key.equals("imageUrl")){
//                                    Picasso.get().load(userInfo).into(myView.imageProfilePhoto)
//                                }else if(key.equals("name")){
//                                  //  myView.username.setText(userInfo)
//                                }
//
//                            }
//
//
//                        }catch (ex:Exception){}
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//
//                    }
//                })


            return myView
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode== RESULT_OK) {
            if (data.data != null) {
                selectedImg = data.data!!

                Toast.makeText(this, "loading your photo", Toast.LENGTH_SHORT).show()
                binding.photoStatusText.setVisibility(View.VISIBLE)

            } else {
                Toast.makeText(this, "image was not selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    uploadInfo(task.toString())

                }
            }
        }
     */


    private fun saveDataToFirebase() {
        val reference=storage.reference.child("Posts").child(Calendar.getInstance().timeInMillis.toString())

        //reducing the image size before uploading to firebase,
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImg)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

        //adding image to firebase
        reference.putBytes(reducedImage).addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener {
                uploadInfo(it.toString())
                //TODO: hiding progress bar
                uploadStatusProgressBar.visibility=View.GONE

            }

        }.addOnProgressListener {
            //TODO: showing progress bar
            uploadStatusProgressBar.visibility=View.VISIBLE
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to upload!", Toast.LENGTH_SHORT).show()
        }
    }




    private fun uploadInfo(myimageUrl:String) {
        val attachment= PostTicket(name!!,firebaseAuth.uid.toString(),binding.attachText.text.toString(),myimageUrl)
        myRef.child("posts")
            .push()
            .setValue(attachment)
            .addOnSuccessListener {
                Toast.makeText(this,"posted successfully", Toast.LENGTH_SHORT).show()
            }
        binding.attachText.text.clear()

    }



    //this function will retrieve the information from our firebase database and load it to our views.
    fun LoadPost(){
        myRef.child("posts")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        ListOfPosts.clear() //we cleared the list
                        //ListOfPosts.add(PostTicket("0","feeling great","url"))

                        val td=snapshot.value as HashMap<String, Any>
                        for(key in td.keys){
                            val post=td[key] as HashMap<String, Any>
                            ListOfPosts.add(PostTicket( post["username"] as String, key as String, post["postDes"] as String,  post["postImageURL"] as String ))
                        }
                        adapter?.notifyDataSetChanged()

                    }catch (ex:Exception){}
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }


    fun LoadLikes(){
        myRef.child("likes_node").push()
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        val td=snapshot.value as HashMap<String, Any>
                        for(key in td.keys){

                            //TODO: error in loading likes and dislikes.

                            // val like=td[key] as HashMap<String, Any>
                            likes.text=td.size.toString()
                        }

                    }catch (ex:Exception){}
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    fun LoadDislikes(){
        myRef.child("dislikes_node").push()
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        val td=snapshot.value as HashMap<String, Any>
                        for(key in td.keys){

                            //TODO: error in loading likes and dislikes.

                            // val like=td[key] as HashMap<String, Any>
                            dislikes.text=td.size.toString()
                        }

                    }catch (ex:Exception){}
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }



}