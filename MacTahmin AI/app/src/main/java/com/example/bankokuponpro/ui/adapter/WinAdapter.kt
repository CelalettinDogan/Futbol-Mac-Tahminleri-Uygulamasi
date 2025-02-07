package com.example.bankokuponpro.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bankokuponpro.R
import com.example.bankokuponpro.data.entity.Match
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class WinAdapter(
    private val context: Context,
    private var matchList: List<Match>,
    private val currentUser: FirebaseUser? // CurrentUser değişkeni
) : RecyclerView.Adapter<WinAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matchInfo: TextView = itemView.findViewById(R.id.match_info)
        val league: TextView = itemView.findViewById(R.id.tournament)
        val matchTime: TextView = itemView.findViewById(R.id.match_time)
        val crdImgFav: ImageView = itemView.findViewById(R.id.crdImgFavorite)
        val txtPrediction: TextView = itemView.findViewById(R.id.txtPrediction)
         val imgDeleteMatch: ImageView = itemView.findViewById(R.id.imgDeleteMatch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false)
        return ViewHolder(view)
    }

    val database = FirebaseDatabase.getInstance()
    val dataref = database.getReference("winMatch") // Favori öğeleri için winMatch düğümüne bağlan

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matchList[position]
        holder.matchInfo.text = "${match.Team1} vs ${match.Team2}"
        holder.league.text = match.League
        holder.txtPrediction.text = "Tahmin : ${match.prediction}"
        holder.imgDeleteMatch.visibility = View.GONE
        holder.crdImgFav.visibility= View.GONE

        if (currentUser?.email == "celaldogan@hotmail.com") {
            holder.imgDeleteMatch.visibility = View.VISIBLE
            holder.crdImgFav.visibility=View.VISIBLE
            holder.imgDeleteMatch.setOnClickListener {
                if (!match.DateTime.isNullOrEmpty()) {
                    dataref.child(match.DateTime).removeValue()
                }
            }
        }

        // Favori işlemleri
        var isFavorite = false
        holder.crdImgFav.setOnClickListener {
            isFavorite = !isFavorite
            holder.crdImgFav.setImageResource(
                if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
            
            val uniqueId = match.Team1 + "_" + match.Team2 + "_" + match.DateTime
            if (isFavorite) {
                val matchn =
                    Match(match.Team1, match.Team2, match.DateTime, match.League, match.prediction)
                dataref.child(uniqueId).setValue(matchn)
            } else {
                dataref.child(uniqueId).removeValue()
                // Listeyi güncelle
                val newList = matchList.filter { it != match }
                updateList(newList)
            }
        }
    }

    override fun getItemCount(): Int = matchList.size

    fun updateList(newList: List<Match>) {
        matchList = newList
        notifyDataSetChanged()
    }
}
