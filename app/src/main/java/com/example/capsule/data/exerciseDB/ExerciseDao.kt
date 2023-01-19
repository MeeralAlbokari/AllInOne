package com.example.capsule.data.exerciseDB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.capsule.data.exerciseEntity.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertAllExercises(exercise: List<Exercise>)

    @Query("SELECT * FROM exercise")
    fun getExercises(): LiveData<List<Exercise>>
    @Query("SELECT * FROM exercise")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM exercise")
    fun getExercisesList(): List<Exercise>

    @Query("Select * from exercise WHERE difficultyLevel=:difficulty")
    fun getDifficulty(difficulty: String?): LiveData<List<Exercise>>

    @Query("Select * from exercise WHERE difficultyLevel=:difficulty")
    fun getByDifficulty(difficulty: String?): Flow<List<Exercise>>
}