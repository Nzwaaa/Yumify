package com.trens.yumify

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.trens.yumify.databinding.FragmentProfileBinding
import com.trens.yumify.ui.view.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tampilkan email pengguna (jika sudah login)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            binding.EmailInputView.text = currentUser.email
            binding.UsernameInputView.text = currentUser.displayName ?: "Guest"
        } else {
            Toast.makeText(requireContext(), "Pengguna tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }

        // Logout button
        binding.buttonLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(requireContext(), "Berhasil logout!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Tutup MainActivity atau aktivitas utama
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
