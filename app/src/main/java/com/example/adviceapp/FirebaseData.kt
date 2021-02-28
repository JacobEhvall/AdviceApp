package com.example.adviceapp

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
/*class FirebaseData {

    //private val storageRef = FirebaseStorage.getInstance().reference

    private lateinit var imageUri : Uri

     fun addImageFirebase(bitmap: Bitmap) {
         val baos = ByteArrayOutputStream()
         val storage = FirebaseStorage.getInstance( ).reference.child("posts/postedImages.png")

         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
         val image = baos.toByteArray()

         val uploadToFirebase = storage.putBytes(image)

         uploadToFirebase.addOnCompleteListener { uploadTask ->
             if (uploadTask.isSuccessful) {
                 storage.downloadUrl.addOnCompleteListener { urlTask ->
                     urlTask.result?.let {
                         imageUri = it

                     }

                 }
             }
         }






         // Kolla denna tutorial för bilder!!
        // https://www.youtube.com/watch?v=YGgauhOiF1c&ab_channel=TheAndroidEnigma
         //https://www.youtube.com/watch?v=-Kuhqq2ipXM&ab_channel=SimplifiedCoding






    }





}

 */