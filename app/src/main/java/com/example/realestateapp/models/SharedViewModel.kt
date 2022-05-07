package com.example.realestateapp.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.security.AccessController.getContext

class SharedViewModel: ViewModel() {
    private var _listings = MutableLiveData<MutableList<Listing>>()
    val listings: LiveData<MutableList<Listing>> = _listings

    private var _listing = MutableLiveData<Listing>()
    val listing: LiveData<Listing> = _listing

    private var _initialLoad = MutableLiveData(true)
    val initialLoad: LiveData<Boolean> = _initialLoad


    fun saveListings(newListings: MutableList<Listing>) {
        onLoadSuccess()
        _listings.value = newListings
    }

    fun saveListing(newListing: Listing) {
        _listing.value = newListing
    }

    fun onLoadSuccess() {
        _initialLoad.value = false
    }
}