package com.trens.yumify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.trens.yumify.databinding.FragmentMyMenuBinding

class MyMenuFragment : Fragment() {

    private var _binding: FragmentMyMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var mainAdapter: MainAdapter

    private var recipeListener: ListenerRegistration? = null

    // Tambahkan parameter untuk menerima data dari newInstance()
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ambil parameter yang diteruskan
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = Firebase.firestore
        initRecipeList()

        binding.iconAddRecipe.setOnClickListener {
            openAddRecipe()
        }

        listenRecipes()
    }

    private fun initRecipeList() {
        mainAdapter = MainAdapter(
            requireContext(),
            onEditBtnClick = {
                openEditRecipe(it)
            },
            onViewBtnClick = {
                openViewRecipe(it)
            },
            onDeleteBtnClick = {
                deleteRecipe(it)
            }
        )

        binding.recipeRV.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeRV.adapter = mainAdapter
    }

    private fun listenRecipes() {
        recipeListener = firestore.collection("recipe").addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore fetch recipe", error.message ?: "Unknown error")
                return@addSnapshotListener
            }

            val data = value?.toObjects<Recipe>()
            data?.let {
                mainAdapter.submitList(it)
                Log.d("Recipes", it.toString())
            }
        }
    }

    private fun deleteRecipe(id: String) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Delete Recipe")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setPositiveButton(android.R.string.yes) { dialog, _ ->
            firestore.collection("recipe").document(id).delete()
            Toast.makeText(context, "Recipe has been deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun openAddRecipe() {
        val intent = Intent(requireContext(), AddRecipeActivity::class.java)
        startActivity(intent)
    }

    private fun openViewRecipe(recipe: Recipe) {
        val intent = Intent(requireContext(), ViewRecipeActivity::class.java)
        intent.putExtra("RECIPE", recipe)
        startActivity(intent)
    }

    private fun openEditRecipe(recipe: Recipe) {
        val intent = Intent(requireContext(), EditRecipeActivity::class.java)
        intent.putExtra("RECIPE", recipe)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipeListener?.remove()
        recipeListener = null
        _binding = null
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        // Fungsi untuk membuat instance dengan parameter
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
