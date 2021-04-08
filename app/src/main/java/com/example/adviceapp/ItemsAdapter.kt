package com.example.adviceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ItemsAdapter(

        private val context: HomePage,
        private val adviceList: MutableList<PostData>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.advice_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = adviceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val advice = adviceList[position]

        val adviceTitle = advice.title
        val adviceDescription = advice.description
        val imageHolder = advice.imageUrl
        holder.titleText.text = adviceTitle
        holder.descriptionText.text = adviceDescription


        println(imageHolder)

        Picasso.get().load(imageHolder).into(holder.uploadedImage)

        /* Bra videolänk för att använda Glide (Kollat på tidigare)
         //https://www.youtube.com/watch?v=EmomJDgR7UQ&t=103s&ab_channel=RahulPandey
         */

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.description_text)
        val uploadedImage : ImageView = itemView.findViewById(R.id.uploaded_image)
    }

}

