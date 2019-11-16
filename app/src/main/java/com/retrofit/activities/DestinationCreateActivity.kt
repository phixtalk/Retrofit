package com.retrofit.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.retrofit.R
import com.retrofit.helpers.SampleData
import com.retrofit.models.Destination
import com.retrofit.services.DestinationService
import com.retrofit.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_create.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DestinationCreateActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_destiny_create)

		setSupportActionBar(toolbar)
		val context = this

		// Show the Up button in the action bar.
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		btn_add.setOnClickListener {
			val newDestination = Destination()
			newDestination.city = et_city.text.toString()
			newDestination.description = et_description.text.toString()
			newDestination.country = et_country.text.toString()

			// To be replaced by retrofit code
			//SampleData.addDestination(newDestination)

			val destinationService = ServiceBuilder.builderService(DestinationService::class.java)
			var requestCall = destinationService.addDestination(newDestination)

			requestCall.enqueue(object : Callback<Destination>{
				override fun onFailure(call: Call<Destination>, t: Throwable) {
					Toast.makeText(this@DestinationCreateActivity,"Failed to add item",
						Toast.LENGTH_SHORT).show()
				}

				override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
					if(response.isSuccessful){
						finish() // Move back to DestinationListActivity
						var newCreatedDestination = response.body()
						Toast.makeText(this@DestinationCreateActivity,"Successfully Added",
							Toast.LENGTH_SHORT).show()
					}else{
						Toast.makeText(this@DestinationCreateActivity,"Failed to add item",
							Toast.LENGTH_SHORT).show()
					}
				}

			})

		}
	}
}
