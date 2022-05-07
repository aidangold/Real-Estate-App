package com.example.realestateapp.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.realestateapp.R
import com.example.realestateapp.models.Listing
import com.example.realestateapp.models.SharedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.Headers
import org.json.JSONException
import java.util.regex.Matcher
import java.util.regex.Pattern


class SearchFragment : Fragment() {
    private val client = AsyncHttpClient()
    private val listings = mutableListOf<Listing>()

    lateinit var etState: EditText
    lateinit var etCity: EditText
    lateinit var btnSearch: Button
    lateinit var btnNearby: Button
    lateinit var progressBar: ProgressBar
    lateinit var bottomNavigationView: BottomNavigationView

    var latitude: Double = 0.0
    var longitude: Double = 0.0

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etState = view.findViewById(R.id.etState)
        etCity = view.findViewById(R.id.etCity)
        btnSearch = view.findViewById(R.id.btnSearch)
        btnNearby = view.findViewById(R.id.btnNearby)

        progressBar = view.findViewById(R.id.pbLoading)
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())

        btnNearby.setOnClickListener {
            getLocation()
        }

        btnSearch.setOnClickListener {
            getSearchResults(etCity.text.toString(), etState.text.toString())
        }
    }

    private fun getSearchResults(city: String?, stateCode: String?) {
        hideKeyboard()
        progressBar.visibility = ProgressBar.VISIBLE

        val headers = RequestHeaders()
        headers.put("X-RapidAPI-Host", getString(R.string.rapid_api_host))
        headers.put("X-RapidAPI-Key", getString(R.string.rapid_api_key))

        val params = RequestParams()
        params.put("city", city)
        params.put("state_code", stateCode)
        params.put("offset", "0")
        params.put("limit", "42")
        params.put("sort", "newest")
        params.put("property_type", "multi_family,single_family")

        client.get(getString(R.string.rapid_api_endpoint), headers, params, object: JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                try {
                    // Parse JSON result into Listing objects and save to ViewModel mutable list
                    val dataJson = json.jsonObject.getJSONObject("data")
                    val listingJsonArray = dataJson.getJSONObject("home_search").getJSONArray("results")
                    listings.addAll(Listing.fromJsonArray(listingJsonArray))
                    sharedViewModel.saveListings(listings)

                    // Swap to results screen
                    progressBar.visibility = ProgressBar.INVISIBLE
                    bottomNavigationView.selectedItemId = R.id.action_results

                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered exception $e")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                progressBar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(requireContext(), "Failed to get results", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure $statusCode")
            }
        })
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location != null) {
                latitude = location.latitude
                longitude = location.longitude

                val address = geocoder.getFromLocation(latitude, longitude, 1)
                if (!address.isNullOrEmpty()) {
                    getSearchResults(address[0].locality, getStateCode(address[0]))
                }
            }
            else {
                Toast.makeText(requireContext(), "Location is null", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getStateCode(USAddress: Address): String? {
        var fullAddress = ""
        for (j in 0..USAddress.getMaxAddressLineIndex()) {
            if (USAddress.getAddressLine(j) != null) {
                fullAddress = fullAddress + " " + USAddress.getAddressLine(j)
            }
        }
        var stateCode: String? = null
        val pattern: Pattern = Pattern.compile(" [A-Z]{2} ")
        val helper = fullAddress.uppercase().substring(0, fullAddress.uppercase().indexOf("USA"))
        val matcher: Matcher = pattern.matcher(helper)
        while (matcher.find()) {
            stateCode = matcher.group().trim()
        }
        return stateCode
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {
        const val TAG = "SearchFragment"
    }
}