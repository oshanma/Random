package com.example.random

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private val petImageURLs = mutableListOf<String>()
    private var currentIndex = 0
    private val requestedCount = 5 // Change this value as needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.petButton)
        val imageView = findViewById<ImageView>(R.id.petImage)
        val textDate = findViewById<TextView>(R.id.textDate)
        val textPhotoName = findViewById<TextView>(R.id.textPhotoName)

        button.setOnClickListener {
            if (currentIndex < petImageURLs.size) {
                getNextImage(imageView, textDate, textPhotoName)
            } else {
                getDogImageURLs(requestedCount)  // Request the specified number of random dog images
            }
        }

        // Initially, request the specified number of random dog images
        getDogImageURLs(requestedCount)
    }

    @SuppressLint("SetTextI18n")
    private fun getNextImage(imageView: ImageView, textDate: TextView, textPhotoName: TextView) {
        val imageUrl = petImageURLs[currentIndex]
        Glide.with(this)
            .load(imageUrl)
            .fitCenter()
            .into(imageView)

        textDate.text = "Random number: $currentIndex"
        textPhotoName.text = "Photo name: ${imageUrl.substringAfterLast("/")}"
        currentIndex++
    }

    private fun getDogImageURLs(count: Int) {
        val client = AsyncHttpClient()
        for (i in 0 until count) {
            client["https://dog.ceo/api/breeds/image/random", object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                    val imageUrl = json.jsonObject.getString("message")
                    petImageURLs.add(imageUrl)

                    // When we have all the requested images, display the first one
                    if (petImageURLs.size == count) {
                        getNextImage(findViewById(R.id.petImage), findViewById(R.id.textDate), findViewById(R.id.textPhotoName))
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    throwable: Throwable?
                ) {
                    Log.d("Dog Error", errorResponse)
                }
            }]
        }
    }
}
