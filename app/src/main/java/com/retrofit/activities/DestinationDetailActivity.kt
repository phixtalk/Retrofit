package com.retrofit.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.retrofit.R
import com.retrofit.helpers.SampleData
import com.retrofit.models.Destination
import com.retrofit.services.DestinationService
import com.retrofit.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationDetailActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_destiny_detail)

		setSupportActionBar(detail_toolbar)
		// Show the Up button in the action bar.
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		val bundle: Bundle? = intent.extras

		if (bundle?.containsKey(ARG_ITEM_ID)!!) {

			val id = intent.getIntExtra(ARG_ITEM_ID, 0)

			loadDetails(id)

			initUpdateButton(id)

			initDeleteButton(id)
		}
	}

	private fun loadDetails(id: Int) {

		// To be replaced by retrofit code
//		val destination = SampleData.getDestinationById(id)
//
		val destinationService= ServiceBuilder.builderService(DestinationService::class.java)
		val requestCall = destinationService.getDestination(id)

		//the enqueue function performs the request operation asynchronously in the background thread
		//Retrofit uses the call interface to make the network calls
		requestCall.enqueue(object: Callback<Destination> {

			override fun onResponse(
				call: Call<Destination>,
				response: Response<Destination>
			) {
				if (response.isSuccessful){
					val destination = response.body()!!

					destination?.let {
						et_city.setText(destination.city)
						et_description.setText(destination.description)
						et_country.setText(destination.country)

						collapsing_toolbar.title = destination.city
					}

				}else if(response.code() == 401){
					Toast.makeText(this@DestinationDetailActivity,"Your session has expired. Please Login again",
						Toast.LENGTH_SHORT).show()
				}else{//Application-level failure
					Toast.makeText(this@DestinationDetailActivity,"Failed to retrieve details", Toast.LENGTH_SHORT).show()

				}
			}

			//Possible cases of throwable error that can Invoked this
			//Network Error - No internet connection
			//IO Exception - Establishing connection with server
			//or Error creating http requests
			// or Error Processing Http Response
			override fun onFailure(call: Call<Destination>, t: Throwable) {
				println("Response: Failure ${t.localizedMessage}")
				Toast.makeText(this@DestinationDetailActivity,"Error occurred ${t.toString()}", Toast.LENGTH_SHORT).show()
			}

		})
	}

	private fun initUpdateButton(id: Int) {

		btn_update.setOnClickListener {

			val city = et_city.text.toString()
			val description = et_description.text.toString()
			val country = et_country.text.toString()

            // To be replaced by retrofit code
            val destination = Destination()
            destination.id = id
            destination.city = city
            destination.description = description
            destination.country = country

//            SampleData.updateDestination(destination);
//            finish() // Move back to DestinationListActivity

            val destinationService = ServiceBuilder.builderService(DestinationService::class.java)
            var requestCall = destinationService.updateDestination(id, city, description, country)

            requestCall.enqueue(object : Callback<Destination>{
                override fun onFailure(call: Call<Destination>, t: Throwable) {
                    Toast.makeText(this@DestinationDetailActivity,"Failed to add item",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                    if(response.isSuccessful){
                        finish() // Move back to DestinationListActivity
                        var newCreatedDestination = response.body()
                        Toast.makeText(this@DestinationDetailActivity,"Successfully Added",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@DestinationDetailActivity,"Failed to add item",
                            Toast.LENGTH_SHORT).show()
                    }
                }

            })
		}
	}

	private fun initDeleteButton(id: Int) {

		btn_delete.setOnClickListener {

            // To be replaced by retrofit code
//            SampleData.deleteDestination(id)
//            finish() // Move back to DestinationListActivity


            val destinationService = ServiceBuilder.builderService(DestinationService::class.java)
            var requestCall = destinationService.deleteDestination(id)

            requestCall.enqueue(object : Callback<Unit>{
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(this@DestinationDetailActivity,"Failed to delete item",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if(response.isSuccessful){
                        finish() // Move back to DestinationListActivity
                        var newCreatedDestination = response.body()
                        Toast.makeText(this@DestinationDetailActivity,"Successfully deleted",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@DestinationDetailActivity,"Failed to delete item",
                            Toast.LENGTH_SHORT).show()
                    }
                }

            })


		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		if (id == android.R.id.home) {
			navigateUpTo(Intent(this, DestinationListActivity::class.java))
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	companion object {

		const val ARG_ITEM_ID = "item_id"
	}
}
