package com.example.capsule.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.capsule.R
import com.example.capsule.data.foodEntity.MealResponse
import com.example.capsule.databinding.ActivityFoodDetailsBinding
import com.example.capsule.retrofit.foodAPI.APIClient
import com.example.capsule.retrofit.foodAPI.APIInterface
import kotlinx.android.synthetic.main.activity_food_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodDetailsActivity : BaseActivity() {

    lateinit var binding:ActivityFoodDetailsBinding

    var youtb=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityFoodDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var id=intent.getStringExtra("id")
        getItem(id!!)


        binding.imgToolbarBtnBack.setOnClickListener {
            val bckhome= Intent(this@FoodDetailsActivity, FoodHomeActivity::class.java)
            startActivity(bckhome)
            finish()
        }// on click
    }// create


    fun getItem(id :String){


        val apiCleint= APIClient.retrofitInstance!!.create(APIInterface::class.java)
        val apiinterface= apiCleint.getSpecificItem(id)

        apiinterface.enqueue(object : Callback<MealResponse>{
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {

                Glide.with(this@FoodDetailsActivity)
                    .load(response.body()!!.mealsEntity[0].strmealthumb)
                    .into(imgItem)

                tvCategoryi.text=response.body()!!.mealsEntity[0].strmeal
                var ingredient = "${response.body()!!.mealsEntity[0].stringredient1}      ${response.body()!!.mealsEntity[0].strmeasure1}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient2}      ${response.body()!!.mealsEntity[0].strmeasure2}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient3}      ${response.body()!!.mealsEntity[0].strmeasure3}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient4}      ${response.body()!!.mealsEntity[0].strmeasure4}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient5}      ${response.body()!!.mealsEntity[0].strmeasure5}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient6}      ${response.body()!!.mealsEntity[0].strmeasure6}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient7}      ${response.body()!!.mealsEntity[0].strmeasure7}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient8}      ${response.body()!!.mealsEntity[0].strmeasure8}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient9}      ${response.body()!!.mealsEntity[0].strmeasure9}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient10}      ${response.body()!!.mealsEntity[0].strmeasure10}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient11}      ${response.body()!!.mealsEntity[0].strmeasure11}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient12}      ${response.body()!!.mealsEntity[0].strmeasure12}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient13}      ${response.body()!!.mealsEntity[0].strmeasure13}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient14}      ${response.body()!!.mealsEntity[0].strmeasure14}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient15}      ${response.body()!!.mealsEntity[0].strmeasure15}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient16}      ${response.body()!!.mealsEntity[0].strmeasure16}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient17}      ${response.body()!!.mealsEntity[0].strmeasure17}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient18}      ${response.body()!!.mealsEntity[0].strmeasure18}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient19}      ${response.body()!!.mealsEntity[0].strmeasure19}\n" +
                        "${response.body()!!.mealsEntity[0].stringredient20}      ${response.body()!!.mealsEntity[0].strmeasure20}\n"

                tvIngredients.text = ingredient
                tvInstructions.text = response.body()!!.mealsEntity[0].strinstructions


            }// response

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Toast.makeText(this@FoodDetailsActivity,"An error appeared", Toast.LENGTH_SHORT).show()
                Log.d("fail", "Details $t")
            }
        })//object

    }

}// main