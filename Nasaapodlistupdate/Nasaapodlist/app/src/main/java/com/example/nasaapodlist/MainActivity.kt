package com.example.nasaapodlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Headers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //Recycler view/ mutable list for APOD data
    private lateinit var Apodlist: MutableList<String>
    private lateinit var Apoddatelist: MutableList<String>
    private lateinit var Apodtitlelist: MutableList<String>
    private lateinit var rvapod: RecyclerView



    var apodurl = ""
    var apodtitle = ""
    var apoddate = ""

    //Random dates
    fun randomDateInRange(from: Date, to: Date): String {
        val diffInMillis = to.time - from.time
        val randomFactor = Random().nextDouble()
        val randomMillis = (diffInMillis * randomFactor).toLong()
        val randomDate = Date(from.time + randomMillis)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(randomDate)
    }

    val fromDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("1995-06-16")!!
    val toDate = Calendar.getInstance().time


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initalize the list and the recyclerview
        rvapod = findViewById(R.id.apodlist)
        Apodlist = mutableListOf()
        Apoddatelist= mutableListOf()
        Apodtitlelist = mutableListOf()





        //Layout manager
        rvapod.layoutManager = LinearLayoutManager(this@MainActivity)


        val dividerItemDecoration = DividerItemDecoration(rvapod.context, (rvapod.layoutManager as LinearLayoutManager).orientation)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.dividerdesign)!!)
        rvapod.addItemDecoration(dividerItemDecoration)


        //creates and sets the APOD adpater
        val adapter = APODadapter(Apodlist,Apodtitlelist,Apoddatelist)
        rvapod.adapter = adapter

        fun apodpicurl(date: String) {
            val client = AsyncHttpClient()
            val params = RequestParams()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            params["date"] = date
            params["thumbs"] = "false"
            params["api_key"] = "3QiKkBvp9mExWQULKjlsSYh0nPiworqBTNXvSGeg"

            client["https://api.nasa.gov/planetary/apod", params, object :
                JsonHttpResponseHandler() {
                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    response: String?,
                    throwable: Throwable?
                ) {
                    if (response != null) {
                        Log.d("Picture Error", response)
                    }
                    if (throwable != null) {
                        Log.d("Picture Errors", "Throwable: ", throwable)
                    }
                }

                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                    if (json != null) {
                        apodurl = json.jsonObject.getString("url")
                        apodtitle = json.jsonObject.getString("title")
                        apoddate = json.jsonObject.getString("date")
                        Log.d("apodImageUrl", " APOD image URL is set")
                        Log.d("More problems", "APOD image URL is set: $json")

                        // Puts the data into the apoddata class

                        Apodlist.add("$apodurl")
                        Apoddatelist.add("$apoddate")
                        Apodtitlelist.add("$apodtitle")



                        Log.d("apodimg messgae", "APOD image URL is almost set: $json")
                        adapter.notifyDataSetChanged()
                    }
                }
            }]
        }

        fun getAPOD() {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            for (i in 1..50) {
                val randomDate = randomDateInRange(fromDate, toDate)
                apodpicurl(randomDate)
            }
        }

        getAPOD()


        val fab: FloatingActionButton = findViewById(R.id.button_load_more)
        fab.setOnClickListener {
            // Clear the existing data
            Apodlist.clear()
            Apodtitlelist.clear()
            Apoddatelist.clear()

            // Fetch new APODs
            getAPOD()
        }
    }
}