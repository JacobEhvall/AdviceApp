package com.example.adviceapp.model

import com.example.adviceapp.Controller.PostData

object GlobalAdviceList {

     // This list is used by adapter to render advice list. Firebase places data here.
    var globalAdviceList = mutableListOf<PostData>()
}
