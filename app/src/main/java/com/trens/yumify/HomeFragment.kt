package com.trens.yumify

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.trens.yumify.databinding.FragmentHomeBinding
import com.trens.yumify.di.ApiModule
import com.trens.yumify.di.DbModule
import com.trens.yumify.di.NetworkModule
import com.trens.yumify.ui.adapter.CategoriesAdapter
import com.trens.yumify.ui.adapter.FoodsAdapter
import com.trens.yumify.data.repository.MainRepository
import com.trens.yumify.utilities.CheckConnection
import com.trens.yumify.utilities.DataStatus
import com.trens.yumify.viewmodel.HomeViewModel
import com.trens.yumify.viewmodel.HomeViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.trens.yumify.ui.view.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var foodsAdapter: FoodsAdapter
    private lateinit var checkConnection: CheckConnection
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MainRepository(
            ApiModule.provideApiService(),
            DbModule.provideFoodDao(DbModule.provideDatabase(requireContext()))
        )

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(repository)
        )[HomeViewModel::class.java]

        auth = FirebaseAuth.getInstance()

        setupRecyclerViews()
        setupObservers()
        setupSearchInput()
        setupConnectionObserver()

        homeViewModel.getCategoriesList()
        homeViewModel.getFoodsList("b")

        val user = auth.currentUser
        user?.let {
            binding.profileName.text = it.displayName
        }

        binding.profileLayout.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileFragment::class.java))
        }
    }

    private fun setupRecyclerViews() {
        categoriesAdapter = CategoriesAdapter().apply {
            setOnItemClickListener { category ->
                homeViewModel.getFoodByCategory(category.strCategory ?: "")
            }
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoriesAdapter
        }

        foodsAdapter = FoodsAdapter().apply {
            setOnItemClickListener { meal ->
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra("MEAL_ID", meal.idMeal)
                }
                startActivity(intent)
            }
        }

        binding.recyclerViewRecipes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = foodsAdapter
        }
    }

    private fun setupObservers() {
        homeViewModel.categoriesList.observe(viewLifecycleOwner) { status ->
            when (status.status) {
                DataStatus.Status.LOADING -> binding.homeCategoryLoading.visibility = View.VISIBLE
                DataStatus.Status.SUCCESS -> {
                    binding.homeCategoryLoading.visibility = View.GONE
                    status.data?.let { categoriesAdapter.setData(it.categories) }
                }
                DataStatus.Status.ERROR -> binding.homeCategoryLoading.visibility = View.GONE
            }
        }

        homeViewModel.foodList.observe(viewLifecycleOwner) { status ->
            when (status.status) {
                DataStatus.Status.LOADING -> binding.homeFoodsLoading.visibility = View.VISIBLE
                DataStatus.Status.SUCCESS -> {
                    binding.homeFoodsLoading.visibility = View.GONE
                    status.data?.let { foodsAdapter.setData(it.meals ?: emptyList()) }
                }
                DataStatus.Status.ERROR -> binding.homeFoodsLoading.visibility = View.GONE
            }
        }
    }

    private fun setupSearchInput() {
        binding.SearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let {
                    if (it.isNotEmpty()) {
                        homeViewModel.getFoodBySearch(it)
                    } else {
                        homeViewModel.getFoodsList("b")
                    }
                }
            }
        })
    }

    private fun setupConnectionObserver() {
        checkConnection = CheckConnection(NetworkModule.provideConnectivityManager(requireContext()))
        checkConnection.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                binding.viewOffline.visibility = View.GONE
                binding.homeDissconect.visibility = View.GONE
                binding.viewContent.visibility = View.VISIBLE
            } else {
                binding.viewOffline.visibility = View.VISIBLE
                binding.homeDissconect.visibility = View.VISIBLE
                binding.viewContent.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
