package com.retrofit.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.retrofit.R
import com.retrofit.activities.DestinationCreateActivity
import com.retrofit.helpers.DestinationAdapter
import com.retrofit.helpers.SampleData
import com.retrofit.models.Destination
import com.retrofit.services.DestinationService
import com.retrofit.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationListActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_destiny_list)

		setSupportActionBar(toolbar)
		toolbar.title = title

		fab.setOnClickListener {
			val intent = Intent(this@DestinationListActivity, DestinationCreateActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()

		loadDestinations()
	}

	private fun loadDestinations() {

        // To be replaced by retrofit code

		val destinationService = ServiceBuilder.builderService(DestinationService::class.java)

		val filter = HashMap<String, String>()
//		filter.put("country", "India")
//		filter.put("count", "1")

//		filter["country"] = "India"
//		filter["count"] = "1"

		val requestCall = destinationService.getDestinationList(filter)

		//the enqueue function performs the request operation asynchronously in the background thread
		//Retrofit uses the call interface to make the network calls
		requestCall.enqueue(object: Callback<List<Destination>> {

			override fun onResponse(
				call: Call<List<Destination>>,
				response: Response<List<Destination>>
			) {
				if (response.isSuccessful){
					val destinationList = response.body()!!
					destiny_recycler_view.adapter = DestinationAdapter(destinationList)
					println("Response: Success")
				}else if(response.code() == 401){
					Toast.makeText(this@DestinationListActivity,"Your session has expired. Please Login again",Toast.LENGTH_SHORT).show()
				}else{//Application-level failure
					Toast.makeText(this@DestinationListActivity,"Failed to retrieve items",Toast.LENGTH_SHORT).show()

				}
			}

			//Possible cases of throwable error that can Invoked this
			//Network Error - No internet connection
			//IO Exception - Establishing connection with server
			//or Error creating http requests
			// or Error Processing Http Response
			override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
				println("Response: Failure ${t.localizedMessage}")
				Toast.makeText(this@DestinationListActivity,"Error occurred ${t.toString()}",Toast.LENGTH_SHORT).show()
			}

		})

    }
}
