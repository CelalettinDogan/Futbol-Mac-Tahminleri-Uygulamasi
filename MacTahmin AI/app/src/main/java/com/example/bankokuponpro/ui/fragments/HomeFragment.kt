package com.example.bankokuponpro.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bankokuponpro.R
import com.example.bankokuponpro.data.entity.Match
import com.example.bankokuponpro.databinding.FragmentHomeBinding
import com.example.bankokuponpro.ui.adapter.HomeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter
    private var matchList: List<Match> = emptyList()
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val email= currentUser?.email

        // RecyclerView ayarları
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        homeAdapter = HomeAdapter(requireContext(), matchList,currentUser)
        binding.recyclerview.adapter = homeAdapter

        // Firebase'den veri çekme işlemi
        fetchMatchesFromFirebase()

        // Arama özelliği
        binding.searchView2.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = matchList.filter {
                    it.Team1.contains(newText ?: "", ignoreCase = true) ||
                            it.Team2.contains(newText ?: "", ignoreCase = true)
                }
                homeAdapter.updateList(filteredList)
                return true
            }
        })


        if(email=="celaldogan@hotmail.com"){
            binding.textView6.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.toAdminPanel)
            }

        }




        return binding.root
    }

    private fun fetchMatchesFromFirebase() {
        val database = FirebaseDatabase.getInstance().getReference()
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewError.visibility = View.GONE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val matches = mutableListOf<Match>()
                    for (i in snapshot.children) {
                        if (i.key == "winMatch") continue

                        val homeTeam = i.child("team1").getValue(String::class.java) ?: ""
                        val awayTeam = i.child("team2").getValue(String::class.java) ?: ""
                        val league = i.child("league").getValue(String::class.java) ?: ""
                        val dateTime = i.child("dateTime").getValue(String::class.java) ?: ""
                        val prediction = i.child("prediction").getValue(String::class.java) ?: ""

                        matches.add(Match(homeTeam, awayTeam, dateTime, league, prediction))
                    }

                    matchList = matches
                    homeAdapter.updateList(matchList)
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.textViewError.visibility = View.VISIBLE
                    binding.textViewError.text = "Veri bulunamadı."
                    binding.progressBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.textViewError.visibility = View.VISIBLE
                binding.textViewError.text = "Veri alınamadı: ${error.message}"
                binding.progressBar.visibility = View.GONE
                Log.e("HomeFragment", "Firebase Hatası: ${error.message}")
            }
        })
    }
}