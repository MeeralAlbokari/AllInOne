package com.example.capsule.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capsule.adapter.foodAdapter.MainCategoryAdapter
import com.example.capsule.adapter.foodAdapter.SubCategoryAdapter
import com.example.capsule.data.foodDB.RecipeDatabase
import com.example.capsule.data.foodEntity.Category
import com.example.capsule.data.foodEntity.CategoryItems
import com.example.capsule.data.foodEntity.Meal
import com.example.capsule.data.foodEntity.MealsItems
import com.example.capsule.databinding.ActivityFoodMainBinding
import com.example.capsule.retrofit.foodAPI.APIClient
import com.example.capsule.retrofit.foodAPI.APIInterface
import kotlinx.android.synthetic.main.activity_food_main.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodHomeActivity : BaseActivity() {

    lateinit var binding:ActivityFoodMainBinding

    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<MealsItems>()

    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFoodMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        getDataFromDb()
        mainCategoryAdapter.setClickListener(onCLicked)
        subCategoryAdapter.setClickListener(onCLickedSubItem)


    }// create


    private val onCLicked  = object : MainCategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }
    }

    private val onCLickedSubItem  = object : SubCategoryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@FoodHomeActivity,FoodDetailsActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }

    fun getCategories(){
        val apiCleint=APIClient.retrofitInstance!!.create(APIInterface::class.java)
        val apiinterface= apiCleint.getCategoryList()
        apiinterface.enqueue(object : Callback<Category>{
            override fun onResponse(call: Call<Category>, response: Response<Category>) {

                for (arr in response.body()!!.categorieitems!!){
                    getMeals(arr.strcategory)
                }// for
                insertDataIntoRoomDb(response.body())
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Toast.makeText(this@FoodHomeActivity,"An error appeared", Toast.LENGTH_SHORT).show()
                Log.d("fail", "onFailure: $t")
            }
        })
    }

    fun getMeals(name:String){
        val apiCleint=APIClient.retrofitInstance!!.create(APIInterface::class.java)
        val apiinterface= apiCleint.getMealList(name)
        apiinterface.enqueue(object :Callback<Meal>{
            override fun onResponse(call: Call<Meal>, response: Response<Meal>) {
                insertMealDataIntoRoomDb(name, response.body())
            }// response

            override fun onFailure(call: Call<Meal>, t: Throwable) {
                Toast.makeText(this@FoodHomeActivity,"An error appeared", Toast.LENGTH_SHORT).show()
                Log.d("fail", "onFailure: get meals $t")
            }// failue

        })// obj
    }// fun meals

    fun insertDataIntoRoomDb(category: Category?) {
        launch {
            this.let {

                for (arr in category!!.categorieitems!!) {
                    RecipeDatabase.getDatabase(this@FoodHomeActivity)
                        .recipeDao().insertCategory(arr)
                }//for
            }//let
        }//launch
    }//fun

    fun insertMealDataIntoRoomDb(categoryName: String, meal: Meal?) {

        launch {
            this.let {

                for (arr in meal!!.mealsItem!!) {
                    var mealItemModel = MealsItems(
                        arr.id,
                        arr.idMeal,
                        categoryName,
                        arr.strMeal,
                        arr.strMealThumb
                    )//data
                    RecipeDatabase.getDatabase(this@FoodHomeActivity).recipeDao().insertMeal(mealItemModel)
                    Log.d("mealData", arr.toString())
                }//for
            }//let
        }//launch
    }// fun


    private fun getDataFromDb(){
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@FoodHomeActivity).recipeDao().getAllCategory()
                arrMainCategory = cat as ArrayList<CategoryItems>
                arrMainCategory.reverse()

                getMealDataFromDb(arrMainCategory[0]!!.strcategory)
                mainCategoryAdapter.setData(arrMainCategory)
                rv_main_category.layoutManager = LinearLayoutManager(this@FoodHomeActivity,
                    LinearLayoutManager.HORIZONTAL,false)
                rv_main_category.adapter = mainCategoryAdapter
            }


        }
    }

    private fun getMealDataFromDb(categoryName:String){
//        binding.apply {
//
//        }

        launch {
            this.let {
                tvCategoryi.text = "$categoryName Category"
                var cat = RecipeDatabase.getDatabase(this@FoodHomeActivity).recipeDao().getSpecificMealList(categoryName)
                arrSubCategory = cat as ArrayList<MealsItems>
                subCategoryAdapter.setData(arrSubCategory)
                rv_sub_category.layoutManager = LinearLayoutManager(this@FoodHomeActivity,LinearLayoutManager.HORIZONTAL,false)
                rv_sub_category.adapter = subCategoryAdapter
            }


        }
    }


}//main