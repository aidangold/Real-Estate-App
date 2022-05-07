package com.example.realestateapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realestateapp.models.Listing
import com.example.realestateapp.models.SharedViewModel
import com.parse.*
import com.parse.Parse.getApplicationContext
import org.json.JSONArray
import java.util.*


open class ListingAdapter(val context: Context, val listings: MutableList<Listing>, val sharedViewModel: SharedViewModel)
    : RecyclerView.Adapter<ListingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListingAdapter.ViewHolder, position: Int) {
        val listing = listings.get(position)
        holder.bind(listing)
    }

    override fun getItemCount(): Int {
        return listings.size
    }

    fun clear() {
        listings.clear()
        notifyDataSetChanged()
    }

    fun addAll(listingList: MutableList<Listing>) {
        listings.addAll(listingList)
        notifyDataSetChanged()
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener, View.OnClickListener {
        val ivListingPhoto: ImageView
        val tvPrice: TextView
        val tvBedCount: TextView
        val tvBathCount: TextView
        val tvSqFt: TextView
        val tvAddress: TextView
        val btnSave: Button

        init {
            ivListingPhoto = itemView.findViewById(R.id.ivListingPhoto)
            tvPrice = itemView.findViewById(R.id.tvPrice)
            tvBedCount = itemView.findViewById(R.id.tvBedCount)
            tvBathCount = itemView.findViewById(R.id.tvBathCount)
            tvSqFt = itemView.findViewById(R.id.tvSqFt)
            tvAddress = itemView.findViewById(R.id.tvAddress)

            itemView.setOnLongClickListener(this)
            itemView.setOnClickListener(this)

            btnSave = itemView.findViewById(R.id.btnSave)
            btnSave.setOnClickListener {
                saveToWishlist()
            }
        }

        fun bind(listing: Listing) {
            Glide.with(itemView.context).load(listing.primaryPhoto).error(ColorDrawable(Color.LTGRAY)).into(ivListingPhoto)
            tvPrice.text = "$" + String.format("%,d", listing.listPrice)
            tvBedCount.text = listing.beds.toString() + " bds"
            tvBathCount.text = listing.baths.toString() + " ba"
            tvSqFt.text = listing.sqft.toString() + " sqft"
            tvAddress.text = "${listing.streetAddr}, ${listing.city}, ${listing.stateCode}"

            // Display null data as N/A
            val parameterList = listOf(listing.beds, listing.baths, listing.sqft)
            val textViewList = listOf(tvBedCount, tvBathCount, tvSqFt)
            val unitList = listOf("bds", "ba", "sqft")

            for (i in 0 until parameterList.size) {
                if (parameterList[i] == -1) {
                    textViewList[i].text = unitList[i] + " N/A"
                }
            }

        }

        override fun onClick(view: View?) {
            sharedViewModel.saveListing(listings[adapterPosition])
        }

        override fun onLongClick(view: View): Boolean {
            // 1. Get notified of the particular listing which was clicked
            val listing = listings[adapterPosition]
            val address = "${listing.streetAddr}, ${listing.city}, ${listing.stateCode} ${listing.postalCode}"

            // 2. Use the intent system to navigate to the new activity
            val intentUri = Uri.Builder().apply {
                scheme("https")
                authority("www.google.com")
                appendPath("maps")
                appendPath("search")
                appendPath("")
                appendQueryParameter("api", "1")
                appendQueryParameter("query", address)
            }.build()
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = intentUri
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            })
            return true
        }

        private fun saveToWishlist() {
            val property = listings[adapterPosition]
            val listingId = property.propertyID
            val savedIds = mutableListOf<String>()
            val query: ParseQuery<ParseObject> = ParseQuery.getQuery("Listing")
            val user = ParseUser.getCurrentUser()
            val wishlistJson = user.getJSONArray("wishlist")
            var inWishlist = false
            if (wishlistJson != null) {
                for (i in 0 until wishlistJson.length()) {
                    if (wishlistJson.getString(i) == listingId) {
                        Toast.makeText(getApplicationContext(), "Listing is Already in Wishlist!", Toast.LENGTH_SHORT).show()
                        inWishlist = true
                    }
                }
                if (!inWishlist) {
                    Toast.makeText(getApplicationContext(), "Listing Saved to Wishlist!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(getApplicationContext(), "Listing Saved to Wishlist!", Toast.LENGTH_SHORT).show()
            }
            user.addAllUnique("wishlist", Arrays.asList(listingId))
            user.saveInBackground()
            query.findInBackground(object : FindCallback<ParseObject> {
                override fun done(savedListings: MutableList<ParseObject>?, e: ParseException?) {
                    if (e != null) {
                        Log.e("savedListings", "Error fetching posts")
                    } else {
                        if (savedListings != null) {
                            for (i in 0 until savedListings.size) {
                                savedIds.add(savedListings[i].getString("listingId").toString())
                            }
                            Log.i("savedListings", savedIds.toString())
                        }
                        if (!(listingId in savedIds)) {
                            saveParseObject(property)
                        }
                    }
                }
            })
        }

        fun saveParseObject(property: Listing){
            val parseListing = ParseObject.create("Listing")
            parseListing.put("listingId",property.propertyID)
            parseListing.put("primaryPhoto", property.primaryPhoto)
            parseListing.put("photos", property.photos)
            parseListing.put("listPrice", property.listPrice)
            parseListing.put("yearBuilt", property.yearBuilt)
            parseListing.put("baths", property.baths)
            parseListing.put("stories", property.stories)
            parseListing.put("type",property.type)
            parseListing.put("beds", property.beds)
            parseListing.put("sqft", property.sqft)
            parseListing.put("lotSqft", property.lotSqft)
            parseListing.put("postalCode", property.postalCode)
            parseListing.put("city", property.city)
            parseListing.put("stateCode", property.stateCode)
            parseListing.put("streetAddr", property.streetAddr)
            parseListing.saveInBackground {exception ->
                if (exception != null) {
                    Log.e("saving", "Error while saving listing")
                    exception.printStackTrace()
                } else {
                    Log.i("saving", "Successfully saved listing")
                }
            }
        }
    }
}