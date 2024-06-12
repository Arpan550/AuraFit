package com.example.aurafit.authentication


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.aurafit.MainActivity
import com.example.aurafit.R
import com.example.aurafit.databinding.ActivityDataConfirmationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class DataConfirmationActivity : AppCompatActivity() {

    private lateinit var dataConfirmationBinding: ActivityDataConfirmationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageRef: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var currentUser: FirebaseUser
    private var imageUri: Uri? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataConfirmationBinding = ActivityDataConfirmationBinding.inflate(layoutInflater)
        setContentView(dataConfirmationBinding.root)

        // Initialize FirebaseAuth, FirebaseFirestore, and StorageReference
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        currentUser = auth.currentUser!!

        // Progress dialog setup
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)

        // Set name and email text
        dataConfirmationBinding.textViewHelloUser.text = "Hello, " + intent.getStringExtra("username") + " !!"

        // Adjust padding to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(dataConfirmationBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Upload profile picture to Firebase Storage
        dataConfirmationBinding.addProfileImg.setOnClickListener {
            chooseImageSource()
        }

        // Hide edit profile button after uploading image
        dataConfirmationBinding.imageButtonEditProfile.visibility = View.GONE

        // Continue button click listener
        dataConfirmationBinding.buttonNext.setOnClickListener {
            val intent = Intent(this@DataConfirmationActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun chooseImageSource() {
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_choose_image, null)
        val textGallery = dialogView.findViewById<TextView>(R.id.textGallery)
        val textTakePhoto = dialogView.findViewById<TextView>(R.id.textTakePhoto)
        val textCancel = dialogView.findViewById<TextView>(R.id.textCancel)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()

        textGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
            dialog.dismiss()
        }

        textTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            dialog.dismiss()
        }

        textCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        // Set dialog position and animations
        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.windowAnimations = R.style.DialogAnimation // Set dialog animation
        layoutParams?.gravity = Gravity.CENTER // Set dialog position
        window?.attributes = layoutParams
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    data?.data?.let { uri ->
                        imageUri = uri
                        setImageAndUpload(uri)
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    (data?.extras?.get("data") as? Bitmap)?.let { bitmap ->
                        val uri = getImageUri(bitmap)
                        imageUri = uri
                        setImageAndUpload(uri)
                    }
                }
            }
        }
    }

    private fun setImageAndUpload(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .into(dataConfirmationBinding.addProfileImg)
        uploadImageToFirebaseStorage(uri)
    }

    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        progressDialog.show()
        val filePath = storageRef.child("profileImages").child(currentUser.uid)
        filePath.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                progressDialog.dismiss()
                Toast.makeText(
                    this@DataConfirmationActivity,
                    "Profile picture uploaded successfully",
                    Toast.LENGTH_SHORT
                ).show()

                filePath.downloadUrl.addOnSuccessListener { uri: Uri ->
                    val photoUrl = uri.toString()
                    // Update Firestore document with the new photoUrl
                    updateUserPhotoUrl(photoUrl)
                }
            }
            .addOnFailureListener { e: Exception ->
                progressDialog.dismiss()
                Toast.makeText(
                    this@DataConfirmationActivity,
                    "Failed " + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun updateUserPhotoUrl(photoUrl: String) {
        // Update Firestore document with the new photoUrl
        val userRef = firestore.collection("users").document(currentUser.uid)
        userRef.update("photoUrl", photoUrl)
            .addOnSuccessListener {
                println("Photo URL updated successfully")
                // Now, you can proceed to the next activity or perform any other actions
            }
            .addOnFailureListener { e ->
                println("Error updating photo URL: ${e.message}")
                // Handle failure
            }
    }


    companion object {
        private const val REQUEST_IMAGE_GALLERY = 1
        private const val REQUEST_IMAGE_CAPTURE = 2
    }
}
