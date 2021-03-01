package com.example.adviceapp

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemsAdapter (

        private val context: Context,
        private val adviceList: List<PostData>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.advice_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = adviceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val advice = adviceList[position]

        /*
        Glide.with(holder.descriptionImage.context)
                .load(imageUri)
                .into(holder.RecyclerviewImage)

         */

        val adviceTitle = advice.title
        val adviceDescription = advice.description
        holder.titleText.text = adviceTitle
        holder.descriptionText.text = adviceDescription


       /* Bra videolänk för att använda Glide (Kollat på tidigare)

        //https://www.youtube.com/watch?v=EmomJDgR7UQ&t=103s&ab_channel=RahulPandey

        */

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.description_text)
        //val descriptionImage: ImageView = itemView.findViewById(R.id.add_image)
        //val RecyclerviewImage : ImageView = itemView.findViewById(R.id.item_image)

    }

}

/*
if (itemImage != null) {
    holder.descriptionImage.setImageResource(itemImage.toInt())
}*/


        /*
        holder.itemView.setOnClickListener {
            val title : String? = itemTitle
            val description : String? = itemDesc
            val image : Int? = itemImage


            //val intent = Intent(context, AddCreatePost::class.java)

            /*
            intent.putExtra("titleText", title)
            intent.putExtra("descriptionText", description)
            //intent.putExtra("displayedImage", image)

            context.startActivity(intent)
           */

        }
         */


