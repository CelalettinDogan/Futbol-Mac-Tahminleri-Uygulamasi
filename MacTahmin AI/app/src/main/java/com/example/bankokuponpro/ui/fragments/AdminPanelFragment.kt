package com.example.bankokuponpro.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.bankokuponpro.R
import com.example.bankokuponpro.data.entity.Match
import com.example.bankokuponpro.databinding.FragmentAdminPanelBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AdminPanelFragment : Fragment() {

    private lateinit var binding: FragmentAdminPanelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminPanelBinding.inflate(inflater)

        val database = FirebaseDatabase.getInstance().reference

        binding.btnAdminKaydet.setOnClickListener {
            // Verileri al
            val mno = binding.mno.text.toString()
            val mev = binding.mEv.text.toString()
            val mdep = binding.mDep.text.toString()
            val mlig = binding.mLig.text.toString()
            val mtah = binding.mTah.text.toString()
            val msaat = binding.mSaat.text.toString()

            // Girdi kontrolleri
            if (TextUtils.isEmpty(mno) || TextUtils.isEmpty(mev) || TextUtils.isEmpty(mdep) ||
                TextUtils.isEmpty(mlig) || TextUtils.isEmpty(mtah) || TextUtils.isEmpty(msaat)) {
                Toast.makeText(context, "Lütfen tüm alanları doldurun.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Telefon numarasını kontrol et
            val mnoInt: Int
            try {
                mnoInt = mno.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Geçersiz telefon numarası.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase'e veriyi gönder
            val match = Match(mev,mdep,msaat,mlig,mtah)
            database.child(mnoInt.toString()).setValue(match)
                .addOnSuccessListener {
                    Toast.makeText(context, "Veri başarıyla kaydedildi.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Veri kaydedilemedi, lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show()
                }
            binding.mSaat.setText(" ")
            binding.mDep.setText(" ")
            binding.mLig.setText(" ")
            binding.mEv.setText(" ")
            binding.mno.setText(" ")
            binding.mTah.setText(" ")

        }
        binding.btnGeriDon.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.homeFragment)
        }

        // Veriyi dinleme
        val getData = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sb = StringBuilder()
                for (i in snapshot.children) {
                    val homeTeam = i.child("team1").getValue(String::class.java)
                    val awayTeam = i.child("team2").getValue(String::class.java)
                    val lig = i.child("league").getValue(String::class.java)
                    val dTime = i.child("dateTime").getValue(String::class.java)
                    val predict = i.child("prediction").getValue(String::class.java)
                    val matchid=i.child("matchID").getValue(String::class.java)

                    sb.append("${i.key}, $homeTeam\n")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Hata mesajı eklenebilir
            }
        }

        database.addValueEventListener(getData)
        database.addListenerForSingleValueEvent(getData)

        return binding.root
    }
}
