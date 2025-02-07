package com.example.bankokuponpro.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankokuponpro.data.entity.Match
import com.example.bankokuponpro.databinding.FragmentFavoritesBinding
import com.example.bankokuponpro.ui.adapter.WinAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var winAdapter: WinAdapter
    private var matchList: List<Match> = emptyList()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // RecyclerView ayarları
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        winAdapter = WinAdapter(requireContext(), matchList, currentUser)
        binding.rvFavorites.adapter = winAdapter

        // Firebase'den veri çekme işlemi
        fetchMatchesFromFirebase()

        return binding.root
    }

    private fun fetchMatchesFromFirebase() {
        val database = FirebaseDatabase.getInstance().getReference("winMatch") // Sadece winMatch düğümünü dinle

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val matches = mutableListOf<Match>()
                    for (i in snapshot.children) {
                        val homeTeam = i.child("team1").getValue(String::class.java) ?: ""
                        val awayTeam = i.child("team2").getValue(String::class.java) ?: ""
                        val league = i.child("league").getValue(String::class.java) ?: ""
                        val dateTime = i.child("dateTime").getValue(String::class.java) ?: ""
                        val prediction = i.child("prediction").getValue(String::class.java) ?: ""

                        matches.add(Match(homeTeam, awayTeam, dateTime, league, prediction))
                    }

                    matchList = matches
                    winAdapter.updateList(matchList)
                } else {
                    matchList = emptyList()
                    winAdapter.updateList(matchList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FavoritesFragment", "Firebase Hatası: ${error.message}")
            }
        })
    }
}
