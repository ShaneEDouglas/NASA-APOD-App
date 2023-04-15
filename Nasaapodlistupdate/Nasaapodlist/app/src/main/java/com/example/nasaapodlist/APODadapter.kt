package com.example.nasaapodlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class APODadapter(private val apodlist: List<String>,private val apodtitlelist: List<String>,private val apoddatelist:List<String>) : RecyclerView.Adapter<APODadapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ApodImage: ImageView
        val ApodTitle: TextView
        val ApodDate: TextView


        init {
            // Find our RecyclerView item's ImageView for future use
            //Image
            ApodImage = view.findViewById(R.id.apodimg)
            //Title
            ApodTitle = view.findViewById(R.id.apodtitle)
            //Date
            ApodDate = view.findViewById(R.id.apoddate)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apoditem, parent, false)

        return ViewHolder(view)
    }

     override fun getItemCount() = apodlist.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apodImageUrl = apodlist[position]
        val apodTitle = apodtitlelist[position]
        val apodDate = apoddatelist[position]

        Glide.with(holder.itemView.context)
            .load(apodImageUrl)
            .into(holder.ApodImage)

        holder.ApodTitle.text = apodTitle
        holder.ApodDate.text = apodDate
    }

}