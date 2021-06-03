package com.example.adviceapp.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post_item.*
import kotlinx.coroutines.*

import com.example.adviceapp.Controller.PostData
import com.example.adviceapp.Model.FirebaseData
import com.example.adviceapp.R

import java.io.ByteArrayOutputStream

class PostItem : AppCompatActivity() {

    private lateinit var context: Context


    companion object {

        var CAMERA_PERMISSION = 123
        var CAMERA_REQUEST_CODE = 134
    }


    private var finalImageURL = MutableLiveData<String>()

    fun getFinalImageURL(): LiveData<String> {
        return finalImageURL
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)


        selectImage()

        saveImageRecyclerview()

        postToRecyclerview()



    }

    // Listener on the image to go to the camera
    private fun selectImage() {

        add_image.setOnClickListener {

            addPicture()

            //Log.d("!!!", "button was pressed")

        }

    }


    // Add the texts and the image to be displayed in the recyclerview
    private fun postToRecyclerview() {

        //TODO POST text and image!
        val postAdvice = findViewById<Button>(R.id.post_data)
        postAdvice.setOnClickListener {

            addPost()

        }

    }

    // reference to what the user input (what the user has written inside the textinputfield)
    private fun addPost() {

        val imageURL = finalImageURL.value.toString();
        val decribeInput = describe_title.text.toString()
        val descriptionInput = description_title.text.toString()

        /* When post the data we have to check that the values are not empty. If
        any of the text fields are empty we return and display a toast message */

        if (decribeInput.isEmpty() || descriptionInput.isEmpty()) {
            Toast.makeText(applicationContext, "No empty fields allowed", Toast.LENGTH_LONG).show()

        } else {

            // When post the data we call the function from the Firebase class
            FirebaseData().PostAdviceData(decribeInput, descriptionInput,imageURL)
            toFirstPage()


        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun chooseTakenPhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { addImageIntent ->
            addImageIntent.resolveActivity(this.packageManager)?.also {
                startActivityForResult(addImageIntent, CAMERA_REQUEST_CODE)

            }

        }
    }


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

    // When user press the post button the button takes the user back to the firstpage(the recyclerview)
    private fun toFirstPage() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
    }

    // The images that has been captured by the camera will be saved in a list inside Firebase Storage
    private fun saveImageRecyclerview() {

        val imageStorage = FirebaseStorage.getInstance()
        val imageRef = imageStorage.reference.child("posts")
        val postedImageList = mutableListOf<PostData>()

        val listImages: Task<ListResult> = imageRef.listAll()
        listImages.addOnCompleteListener { result ->
            val images: List<StorageReference> = result.result!!.items
            images.forEachIndexed { index, item ->
                item.downloadUrl.addOnSuccessListener {
                    Log.d("post", "$it")
                    postedImageList.add(PostData(it.toString()))
                }

            }

        }

    }


    // The permission to use the camera has been accepted.
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap


            // This is where we add the image to the image from camera simulator
            add_image.setImageBitmap(imageBitmap)


            getAndSaveUri(imageBitmap)

            //imageThread(imageBitmap)
            println("!!! SATT BILD FRÅN KAMERA = ${imageBitmap}")


           // println("___STEG 3____");


        }


    }

     fun getAndSaveUri(bitmap: Bitmap) = runBlocking() {


        launch {


            val baos = ByteArrayOutputStream()
            val storageImage = FirebaseStorage.getInstance()
                    .reference
                    .child("MinBild${System.currentTimeMillis()}")

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val image = baos.toByteArray()

            val uploadToFirebase = storageImage.putBytes(image)

            uploadToFirebase.addOnCompleteListener { uploadTask ->
                if (uploadTask.isSuccessful) {
                    storageImage.downloadUrl.addOnCompleteListener { urlTask ->
                        urlTask.result?.let {

                            add_image.setImageBitmap(bitmap)

                            var theIMGURI = it;

                            finalImageURL.value = theIMGURI.toString();
                            //println("???" + theIMGURI)


                            // ""theIMGURI"""  skickar du upp med själva inlägget till databasen som en vanlig sträng
                            // Här nere kan du nu göra själva requested till databasen för Posten
                            //  ulpoadThePost();


                        }

                    }
                }
            }
        }

    }


       /* fun imageThread(bitmap: Bitmap) {


           var theFire = FirebaseData();

         theFire.getImageUrl(bitmap);


         theFire.thefinalImageURL.observe(this, Observer {

                finalImageURL.value = it.toString()
                println("!!! the Image URL Uploaded::: ${it}")

            })
        */




            ///add_image.setImageBitmap(bitmap)



        // HAR INTE FIXAT ÄN, MEN HÄR LADDAR VI UPP BILDEN TILL FIREBASE :)


        // Bättre att sätta bilden i denna klass och göra Firebase i Firebase klassen


        //println("???" + theIMGURI)


        // ""theIMGURI"""  skickar du upp med själva inlägget till databasen som en vanlig sträng
        // Här nere kan du nu göra själva requested till databasen för Posten
        //  ulpoadThePost();

    }












