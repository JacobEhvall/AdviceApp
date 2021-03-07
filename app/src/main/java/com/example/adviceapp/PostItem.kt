package com.example.adviceapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.number.NumberFormatter.with
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
//import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_item.*
import kotlinx.android.synthetic.main.advice_card.*
import java.io.ByteArrayOutputStream



class PostItem : AppCompatActivity() {

    private lateinit var context: Context
    lateinit var db: FirebaseFirestore
    //private lateinit var imageUri : Uri         // unsure if the images can be private?
    //private var storageRef: StorageReference? = null
    //private var firebaseStore: FirebaseStorage? = null

    companion object {

        var CAMERA_PERMISSION = 123
        var CAMERA_REQUEST_CODE = 134
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        //selectImage()

        postToRecyclerview()


        db = FirebaseFirestore.getInstance()
       //storageRef = FirebaseStorage.getInstance().reference.child("posts/")
        //firebaseStore = FirebaseStorage.getInstance()


        /* This button do we call to post the userInput for now,
        but we want aswell the image to be posted! */

    }

    /*
    private fun selectImage() {

        add_image.setOnClickListener {

            addPicture()

            //Log.d("!!!", "button was pressed")

        }

    }

     */

    private fun postToRecyclerview() {

        //TODO POST text and image!
        val postAdvice = findViewById<Button>(R.id.post_data)
        postAdvice.setOnClickListener {

            addPost()
        }

    }

    private fun addPost() {

        /* TODO When we post the text we also want the image together white the text
            to be posted that´s sets a Bitmap as the content of this ImageView why we also what to declare the image in the "addPost" function.
         */

        val decribeInput = describe_title.text.toString()
        val descriptionInput = description_title.text.toString()


        // TODO We can specify an error message in here instead of regular Toast!

        if (decribeInput.isEmpty() || descriptionInput.isEmpty()) {
            Toast.makeText(applicationContext, "No empty fields allowed", Toast.LENGTH_LONG).show()

        } else {
            // TODO maybe rename PostData to AdviceData?
            val advice = PostData(
                    decribeInput,
                    descriptionInput
            )
            db.collection("advice").add(advice)
            toFirstPage()

        }

    }
    /*
    @SuppressLint("QueryPermissionsNeeded")
    private fun chooseTakenPhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { addImageIntent ->
            addImageIntent.resolveActivity(this.packageManager)?.also {
                startActivityForResult(addImageIntent, CAMERA_REQUEST_CODE)

            }

        }
    }

     */
  /*
    private fun addPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions,
                        CAMERA_PERMISSION
                )
            } else {
                chooseTakenPhoto()
            }
        } else {
            chooseTakenPhoto()
        }
    }
   */

    private fun toFirstPage() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

    /*
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
            requestCode: Int, permissions:
            Array<out String>,
            grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseTakenPhoto()
                } else {
                    Toast.makeText(this, "Permisson denied, you can´t take photo", Toast.LENGTH_SHORT).show()
                }
        }
    }

     */
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            getAndSaveUri(imageBitmap)

        }

    }

 */

    /*
    private fun getAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val storageImage = FirebaseStorage.getInstance()
                .reference
                .child("home/")

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val uploadToFirebase = storageImage.putBytes(image)

        uploadToFirebase.addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                storageImage.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it

                        add_image.setImageBitmap(bitmap)

                    }

                }
            }
        }
     */

       // Uri.fromFile(imageUri.toFile())
       // Picasso.with(context).load(imageUri).resize(100, 100).centerCrop().into(item_image)
         //storageImage.child("home/").putFile(imageUri).addOnCompleteListener {
         //storageImage.child("home/").downloadUrl


    }






/*
 @RequiresApi(Build.VERSION_CODES.M)
 private fun picturePermission() {
     if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
         addPicture()
         val permissions = arrayOf(Manifest.permission.CAMERA)
         requestPermissions(permissions, CAMERA_REQUEST_CODE)
     }
 }
 */



















// imageUri = UUID.randomUUID().toString()


/*
val bmp = data?.extras?.get("data") as Bitmap
add_image.setImageBitmap(bmp)
val bitmap = (add_image.drawable as BitmapDrawable).bitmap
val baos = ByteArrayOutputStream()
bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
val data = baos.toByteArray()
*/


// To be able to fit the image inside the imageview I think we have to crop the image


// Check out 1:07:50 to see the croping



/*
if (requestCode == CAMERA_REQUEST_CODE && data!= null && resultCode == Activity.RESULT_OK) {
imageUri = data.data.toString()
add_image.setImageURI(Uri.parse(imageUri))
}
println("!!!${add_image}")
}
*/


//val uri = data.data

/*
val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
val bitmapImage = BitmapDrawable(bitmap)
add_image.setBackgroundDrawable(bitmapImage)
*/
