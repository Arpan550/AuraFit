package com.example.aurafit.ui.drawer.view

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.aurafit.databinding.ActivityNewPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap

class NewPostActivity : AppCompatActivity() {
    private lateinit var newPostBinding: ActivityNewPostBinding
    private lateinit var imageUri: Uri
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var storageReference: StorageReference

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 101
        private const val REQUEST_IMAGE_CAPTURE = 102
        private const val PICK_IMAGE_REQUEST = 103
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newPostBinding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(newPostBinding.root)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference.child("post_images")

        // Initialize progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...Uploading..")
        progressDialog.setCancelable(false)

        // Set up image picker dialog
        newPostBinding.imagePost.setOnClickListener { showImagePickerDialog() }

        // Set up post button click listener
        newPostBinding.buttonPost.setOnClickListener {
            // Show progress dialog
            progressDialog.show()

            // Upload image to Firebase Storage
            uploadImage()
        }

        // Set up cancel button functionality
        newPostBinding.buttonCancel.setOnClickListener {
            finish()
        }
    }

    // Method to show image picker dialog
    private fun showImagePickerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(arrayOf("Take photo", "Choose from gallery")) { dialog, which ->
            when (which) {
                0 -> checkCameraPermissionAndTakePhoto()
                1 -> choosePhotoFromGallery()
            }
        }
        builder.show()
    }

    // Method to check camera permission and take photo
    private fun checkCameraPermissionAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            takePhotoFromCamera()
        }
    }

    // Method to take photo from camera
    private fun takePhotoFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Method to choose photo from gallery
    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Method to upload image to Firebase Storage
    private fun uploadImage() {
        if (::imageUri.isInitialized) {
            val fileReference = storageReference.child("${System.currentTimeMillis()}.${getFileExtension(imageUri)}")
            fileReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Get the download URL of the uploaded image
                    fileReference.downloadUrl.addOnSuccessListener { uri ->
                        // Save caption and image URL to Firestore
                        saveToFirestore(uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    // Handle upload failure
                    progressDialog.dismiss()
                    Toast.makeText(this@NewPostActivity, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            progressDialog.dismiss()
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    // Method to save caption and image URL to Firestore
    // Method to save caption and image URL to Firestore
    private fun saveToFirestore(imageUrl: String) {
        val caption = newPostBinding.editTextCaption.text.toString()
        val userID = auth.currentUser!!.uid

        // Reference the collection where you want to add the new document
        val collectionReference: CollectionReference = firestore.collection("userPosts")

        val userPosts = HashMap<String, Any>()
        userPosts["caption"] = caption
        userPosts["imageUrl"] = imageUrl
        userPosts["userID"] = userID

        // Generate a sequential document ID
        val postNumber = (System.currentTimeMillis() / 1000).toString() // Example: Use timestamp as a sequential ID
        val documentReference: DocumentReference = collectionReference.document(postNumber)

        // Set the data to the document
        documentReference.set(userPosts)
            .addOnSuccessListener {
                // Hide progress dialog
                progressDialog.dismiss()
                startActivity(Intent(this, CommunitySupportActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // Handle failure
                progressDialog.dismiss()
                Toast.makeText(this@NewPostActivity, "Failed to add post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    // Method to get file extension from image URI
    private fun getFileExtension(uri: Uri): String? {
        return contentResolver.getType(uri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val extras = data?.extras
                    if (extras != null) {
                        val imageBitmap = extras.get("data") as Bitmap?
                        newPostBinding.imagePost.setImageBitmap(imageBitmap)
                        imageUri = getImageUri(imageBitmap!!)
                    }
                }
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        try {
                            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                            newPostBinding.imagePost.setImageBitmap(bitmap)
                            imageUri = selectedImageUri
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    // Method to get image URI from bitmap
    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
}