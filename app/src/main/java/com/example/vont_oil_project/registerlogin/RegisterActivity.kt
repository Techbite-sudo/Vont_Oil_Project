package com.example.vont_oil_project.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.vont_oil_project.R
import com.example.vont_oil_project.databinding.ActivityRegisterBinding
import com.example.vont_oil_project.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.registerButtonRegister.setOnClickListener {
            PerformRegister()
        }
        binding.alreadyHaveAccountTextView.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")
            // launch the login activity somehow
            Intent(this, LoginActivity::class.java).also {
                startActivity(it)
            }

        }
        binding.selectPhotoButtonRegister.setOnClickListener{
            Log.d("RegisterActivity","Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)

        }

    }

    private fun PerformRegister() {
        val email = binding.emailEditTextRegister.text.toString()
        val password = binding.passwordEditTextRegister.text.toString()
        val username = binding.usernameEditTextRegister.text.toString()
        Log.d("RegisterActivity","Username is:$username")
        Log.d("RegisterActivity", "Email is: " + email)
        Log.d("RegisterActivity", "Password: $password")
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("RegisterActivity", "Attempting to create user with email: $email")
        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if (!it.isSuccessful)return@addOnCompleteListener
                //else if successful
                Log.d("Main","Successfully create user with uid:${it.result?.user?.uid}")
                Toast.makeText(this,"Successfully created an Account", Toast.LENGTH_SHORT).show()
                uploadImageToFireBaseStorage()
            }
            .addOnFailureListener{
                Log.d("Main","Failed to create user.${it.message}")
                Toast.makeText(this,"Failed to create user:${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImageToFireBaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")
                    saveUserTofirebaseDatabase(it.toString())
                }

            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun saveUserTofirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,binding.usernameEditTextRegister.text.toString(),profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Finally we saved the user to firebase database")
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d("RegisterActivity","Failed to set value to database: ${it.message}")
            }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null){
            //proceed to check what the selected photo was
            Log.d("RegisterActivity","Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            binding.selectPhotoImageViewRegister.setImageBitmap(bitmap)
            binding.selectPhotoButtonRegister.alpha = 0f

        }
    }
}