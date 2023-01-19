package com.example.capsule.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capsule.R
import com.example.capsule.activities.FoodHomeActivity
import com.example.capsule.activities.GymActivity
import com.example.capsule.adapter.execiseAdapter.ExerciseAdapter
import com.example.capsule.databinding.FragmentHomeBinding
import com.example.capsule.viewmodel.ExercisesViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var exerciseViewModel: ExercisesViewModel
    private lateinit var exerciseAdapter: ExerciseAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.options)
        binding.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

//        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.let {
            exerciseViewModel = ViewModelProvider(it!!).get(ExercisesViewModel::class.java)
        }
        exerciseAdapter = ExerciseAdapter(mutableListOf())

        exerciseAdapter.setOnExerciseListener { exercise ->
            exerciseViewModel.setSelectedExercise(exercise)
            findNavController().navigate(
                R.id.detailsFragment, arguments, NavOptions.Builder().setPopUpTo(
                    R.id.detailsFragment, true).build())
        }

        withTags()
        attachObservers()
    }

    private fun attachObservers() {
        exerciseViewModel.exercises.observe(viewLifecycleOwner) {
            binding.rvExercises.apply {
                adapter = exerciseAdapter
                layoutManager = LinearLayoutManager(activity)
            }
            exerciseAdapter.showData(it)
        }
    }


    private fun withTags() {
        binding.chipGroupFilter.children.forEach {
            val chip  = (it as Chip)
            chip.setOnCheckedChangeListener { _, b ->
                if (b) {
                    exerciseViewModel.difficulty.value = chip.tag as String
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.gym ->{
                val intent=Intent(this@HomeFragment.requireContext(), GymActivity::class.java)
                startActivity(intent)
                return true}
            R.id.food ->{
                val intent=Intent(this@HomeFragment.requireContext(), FoodHomeActivity::class.java)
                startActivity(intent)
                return true}
        } //when ends
        return super.onOptionsItemSelected(item)
    } //end fun

}// main