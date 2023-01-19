package com.example.capsule.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capsule.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext:CoroutineContext
        get() = job + Dispatchers.Main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}