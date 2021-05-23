package com.example.adviceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ItemsAdapter(

        private val context: HomePage,
        private var globalAdviceList: MutableList<PostData>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.advice_card, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount() = GlobalAdviceList.globalAdviceList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val advice = GlobalAdviceList.globalAdviceList[position]

        val adviceTitle = advice.title
        val adviceDescription = advice.description
        val imageHolder = advice.imageUrl
        holder.titleText.text = adviceTitle
        holder.descriptionText.text = adviceDescription


        Picasso.get().load(imageHolder).into(holder.uploadedImage)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val descriptionText: TextView = itemView.findViewById(R.id.description_text)
        val uploadedImage : ImageView = itemView.findViewById(R.id.uploaded_image)
    }

}


