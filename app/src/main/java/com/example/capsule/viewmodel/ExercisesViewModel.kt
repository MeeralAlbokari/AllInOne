package com.example.capsule.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.capsule.data.exerciseDB.ExerciseDatabase

import com.example.capsule.data.exerciseEntity.Exercise
import com.example.capsule.repository.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ExercisesViewModel(application: Application) : AndroidViewModel(application)  {

    private val repository: ExerciseRepository
    val difficulty = MutableStateFlow("all")

    init {
       val exerciseDao = ExerciseDatabase
           .getDatabase(application, viewModelScope, application.resources)
           .exerciseDao()
        repository = ExerciseRepository(exerciseDao)
    }

     val selectedExercise = MutableLiveData<Exercise>()

    fun setSelectedExercise(exercise: Exercise){
        selectedExercise.value = exercise
    }

    fun getAllExercises(): LiveData<List<Exercise>>{
        return repository.getAllExercises()
    }

    val exercises = difficulty.flatMapLatest {
        repository.getAllOrSearch(it)
    }.asLiveData()

    fun getExercisesByDifficulty(difficulty: String): LiveData<List<Exercise>>{
        return repository.getExercisesByDifficulty(difficulty)
    }


}