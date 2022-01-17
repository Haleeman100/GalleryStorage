/*
package com.example.gallerystorage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View.inflate
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.resources.Compatibility.Api21Impl.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.viewbinding.ViewBinding
import com.example.gallerystorage.databinding.ActivityGalleryBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.IOException
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import android.graphics.Bitmap
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.UploadTask
import java.util.*


class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    var activityResultLauncher: ActivityResultLauncher<*>? = null

    private val CAMERA_ACTION_CODE = 1

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference




        btn_choose_image.setOnClickListener {
           // launchGallery()
        }
        btn_upload_image.setOnClickListener {
            uploadImage()
        }

        activityResultLauncher = registerForActivityResult(StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val filePath = result.data!!.extras
                    val bitmap = filePath!!["data"] as Bitmap?
                    image_preview.setImageBitmap(bitmap)
                }
            }
        )


//        val getImage = registerForActivityResult(
//            ActivityResultContracts.GetContent(),
//            ActivityResultCallback {
//                binding.imagePreview.setImageURI(it)
//
//            }
//        )
//
//        binding.btnChooseImage.setOnClickListener {
//            getImage.launch("image/*")
//        }

        btn_choose_image.setOnClickListener {

                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(getPackageManager()) != null){
                    ((activityResultLauncher as ActivityResultLauncher<Intent>).launch(intent))

                } else {
                    Toast.makeText(this,"No image",Toast.LENGTH_SHORT).show()
                }

            }

        }


    private fun uploadImage(){
        if( filePath != null){
            val ref = storageReference?.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        db.collection("posts")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }



    }





}





    */
/*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                uploadImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }*/
    */