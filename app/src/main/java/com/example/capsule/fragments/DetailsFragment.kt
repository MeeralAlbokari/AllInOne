package com.example.capsule.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.capsule.R
import com.example.capsule.databinding.FragmentDetailsBinding
import com.example.capsule.viewmodel.ExercisesViewModel


class DetailsFragment : Fragment() {

    private lateinit var _binding: FragmentDetailsBinding
    private val binding get() = _binding
    private lateinit var exerciseViewModel: ExercisesViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.let {
            exerciseViewModel = ViewModelProvider(it!!).get(ExercisesViewModel::class.java)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_detailsFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        exerciseViewModel.selectedExercise.observe(viewLifecycleOwner){
            Log.d("TAG", "onViewCreatedDetail:$it ")
            binding.apply {
                exerciseName.text = it.exerciseName
                difficultyLevelTv.text = it.difficultyLevel
                requiredEquipmentTv.text = it.requiredEquipment
                primaryMusclesTv.text = it.primaryMuscles
                secondaryMusclesTv.text = it.secondaryMuscles
                descriptionTv.text = it.description
                Glide.with(exerciseImage.context)
                    .load(it.secondaryImage)
                    .placeholder(R.drawable.image_three)
                    .fitCenter()
                    .centerCrop()
                    .into(exerciseImage)
            }
        }
    }

}