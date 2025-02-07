package com.example.bankokuponpro.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bankokuponpro.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()

        checkUserStatus() // Kullanıcı durumunu kontrol et

        // Login işlemi
        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Giriş başarılı!", Toast.LENGTH_SHORT).show()
                        checkUserStatus()
                        binding.email.setText("")
                        binding.password.setText("")
                    } else {
                        Toast.makeText(requireContext(), "Giriş başarısız: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Register işlemi
        binding.registerButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Kayıt başarılı!", Toast.LENGTH_SHORT).show()
                        checkUserStatus()
                    } else {
                        Toast.makeText(requireContext(), "Kayıt başarısız: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Logout işlemi
        binding.logoutButton.setOnClickListener {
            auth.signOut()
            checkUserStatus()
        }

        return binding.root
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Kullanıcı giriş yapmış
            binding.loginLayout.visibility = View.GONE
            binding.profileLayout.visibility = View.VISIBLE
            binding.welcomeMessage.text = "Hoş geldiniz, ${currentUser.email}"
        } else {
            // Kullanıcı giriş yapmamış
            binding.loginLayout.visibility = View.VISIBLE
            binding.profileLayout.visibility = View.GONE
        }
    }
}
