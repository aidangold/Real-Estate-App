package com.example.realestateapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.realestateapp.R
import com.example.realestateapp.WishlistAdapter
import com.example.realestateapp.models.Listing
import com.example.realestateapp.models.SharedViewModel
import com.parse.*


class WishlistFragment : Fragment() {

    var allWishlist: MutableList<Listing> = mutableListOf()
    var listingsFromW: MutableList<Listing> = mutableListOf()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    fun queryWishlist(adapter: WishlistAdapter) {
        val user = ParseUser.getCurrentUser()
        val wishlistJson = user.getJSONArray("wishlist")
        val wishlist = mutableListOf<String>()
        if (wishlistJson != null) {
            for (i in 0 until wishlistJson.length()) {
                val listingId = wishlistJson.getString(i)
                wishlist.add(listingId)
            }
            val query: ParseQuery<ParseObject> = ParseQuery.getQuery("Listing")
            query.whereContainedIn("listingId", wishlist)
            query.findInBackground(object : FindCallback<ParseObject> {
                override fun done(listings: MutableList<ParseObject>?, e: ParseException?) {
                    if (e != null) {
                        Log.e("wishlist", "Error fetching posts")
                    } else {
                        if (listings != null) {
                            var convertedListing: Listing
                            for (listing in listings) {
                                convertedListing = createListing(listing)
                                listingsFromW.add(convertedListing)
                            }
                            adapter.clear()
                            adapter.addAll(listingsFromW)
                        }
                    }
                }
            })
        }

    }

    fun createListing(listing: ParseObject): Listing {
        val propertyID = listing.getString("listingId").toString()
        val primaryPhoto = listing.getString("primaryPhoto").toString()
        val photosJson = listing.getJSONArray("photos")

        val photos = mutableListOf<String>()
        if (photosJson  != null) {
            for (i in 0 until photosJson.length()) {
                val url = photosJson.getString(i)
                photos.add(url)
            }
        }
        val listPrice = listing.getInt("listPrice")
        val yearBuilt = listing.getInt("yearBuilt")
        val baths = listing.getInt("baths")
        val stories = listing.getInt("stories")
        val type = listing.getString("type").toString()
        val beds = listing.getInt("beds")
        val sqft = listing.getInt("sqft")
        val lotSqft = listing.getInt("lotSqft")
        val postalCode = listing.getString("postalCode").toString()
        val city = listing.getString("city").toString()
        val stateCode = listing.getString("stateCode").toString()
        val streetAddr = listing.getString("streetAddr").toString()
        val convertedListing = Listing(propertyID, listPrice, primaryPhoto, photos, yearBuilt, baths, stories, beds, sqft, lotSqft, type, postalCode, city, stateCode, streetAddr)
        return convertedListing
    }

    lateinit var rvWishlist: RecyclerView
    lateinit var adapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rvWishlist)

        rvWishlist = view.findViewById(R.id.rvWishlist)
        adapter = WishlistAdapter(requireContext(),allWishlist, sharedViewModel)
        rvWishlist.adapter = adapter
        rvWishlist.layoutManager = LinearLayoutManager(requireContext())
        queryWishlist(adapter)
    }
}