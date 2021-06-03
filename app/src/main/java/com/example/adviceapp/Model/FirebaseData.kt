package com.example.adviceapp.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.adviceapp.Controller.PostData
import com.example.adviceapp.Controller.UserData
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseData {
    
     var thefinalImageURL = MutableLiveData<String>()

    fun getFinalImageURL(): LiveData<String>{
        return thefinalImageURL
    }



    lateinit var db: FirebaseFirestore


    var userEmail = MutableLiveData<String>()

    fun getEmail(): LiveData<String> {
        return userEmail

    }


    fun PostAdviceData(title: String, description: String, imageUrl: String) {

        db = FirebaseFirestore.getInstance()

        val advice = PostData(
            title,
            description,
            imageUrl
        )
        db.collection("advice").add(advice)

    }


    fun getUserEmail(uid: String) {

        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid)
        docRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val document = task.result
                        if (document!!.exists()) {
                            val userInfo = document.toObject(UserData::class.java)

                            userEmail.value = userInfo!!.email


                        }

                    }
                }
    }

    }



         /*
         fun getImageUrl(bitmap: Bitmap) = runBlocking() {


            var returnen = "micke"

             var ourFinalImageURL = MutableLiveData<String>()

            println("____STORAGE__STEG___1");
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

                            var theIMGURI = it;


                            thefinalImageURL.value = theIMGURI.toString();

                            println("!!!____STORAGE__STEG__4" + thefinalImageURL.value);


                        }
                    }

                    //println("???" + theIMGURI)


                    // ""theIMGURI"""  skickar du upp med själva inlägget till databasen som en vanlig sträng
                    // Här nere kan du nu göra själva requested till databasen för Posten
                    //  ulpoadThePost();

                }


            }



        }
          */






