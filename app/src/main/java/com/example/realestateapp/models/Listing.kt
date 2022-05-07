package com.example.realestateapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

@Parcelize
data class Listing(
    var propertyID: String,
    var listPrice: Int,
    var primaryPhoto: String,
    var photos: MutableList<String> = mutableListOf(),
    var yearBuilt: Int,
    var baths: Int,
    var stories: Int,
    var beds: Int,
    var sqft: Int,
    var lotSqft: Int,
    var type: String,
    var postalCode: String,
    var city: String,
    var stateCode: String,
    var streetAddr: String
): Parcelable {
    companion object {
        fun fromJsonArray(jsonArray: JSONArray): List<Listing> {
            val listings = mutableListOf<Listing>()
            for (i in 0 until jsonArray.length()) {
                val listingJson = jsonArray.getJSONObject(i)
                listings.add(parseListingJson(listingJson))
            }
            return listings
        }

        fun parseListingJson(listingJson: JSONObject): Listing {
            val descriptionJson = listingJson.getJSONObject("description")
            val addressJson = listingJson.getJSONObject("location").getJSONObject("address")
            return Listing (
                listingJson.getString("property_id"),
                listingJson.getInt("list_price"),
                handleNullPrimaryPhoto(listingJson),
                parsePhotosJsonArray(listingJson),
                handleNullDescription(descriptionJson, "year_built"),
                handleNullDescription(descriptionJson, "baths"),
                handleNullDescription(descriptionJson, "stories"),
                handleNullDescription(descriptionJson, "beds"),
                handleNullDescription(descriptionJson, "sqft"),
                handleNullDescription(descriptionJson, "lot_sqft"),
                descriptionJson.getString("type"),
                addressJson.getString("postal_code"),
                addressJson.getString("city"),
                addressJson.getString("state_code"),
                addressJson.getString("line"),
            )
        }

        fun parsePhotosJsonArray(listingJson: JSONObject): MutableList<String> {
            val photos = mutableListOf<String>()
            try {
                val jsonArray = listingJson.getJSONArray("photos")
                for (i in 0 until jsonArray.length()) {
                    val photoJson = jsonArray.getJSONObject(i)
                    photos.add(photoJson.getString("href"))
                }
                return photos
            } catch (error: JSONException) {
                return photos
            }
        }

        fun handleNullDescription(descriptionJson: JSONObject, key: String): Int {
            try {
                return descriptionJson.getInt(key)
            } catch (error: JSONException) {
                return -1
            }
        }

        fun handleNullPrimaryPhoto(listingJson: JSONObject): String {
            try {
                return listingJson.getJSONObject("primary_photo").getString("href")
            } catch (error: JSONException) {
                return ""
            }
        }
    }
}
