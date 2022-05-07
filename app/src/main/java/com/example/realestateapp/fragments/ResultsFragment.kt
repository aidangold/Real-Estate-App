package com.example.realestateapp.fragments

import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realestateapp.ListingAdapter
import com.example.realestateapp.R
import com.example.realestateapp.models.Listing
import com.example.realestateapp.models.SharedViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ResultsFragment : Fragment() {

    lateinit var rvListings: RecyclerView
    lateinit var adapter: ListingAdapter
    var allListings: MutableList<Listing> = mutableListOf()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var flContainer: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.rvListings)

        rvListings = view.findViewById(R.id.rvListings)
        adapter = ListingAdapter(requireContext(), allListings, sharedViewModel)
        rvListings.adapter = adapter
        rvListings.layoutManager = LinearLayoutManager(requireContext())
        flContainer = requireActivity().findViewById(R.id.flContainer)

        // loads from internal storage if no search has been made
        if (sharedViewModel.initialLoad.value == true)  {
            loadFromStorage()
            sharedViewModel.onLoadSuccess()
        }

        sharedViewModel.listings.observe(viewLifecycleOwner) { listings ->
            adapter.clear()
            adapter.addAll(listings)
            persistToStorage(listings)
        }

        // For DetailFragment
        sharedViewModel.listing.observe(viewLifecycleOwner) { listing ->
            //Toast.makeText(context, "${listing}", Toast.LENGTH_SHORT).show()
            var transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.flContainer, DetailsFragment())
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.addToBackStack(null)
            transaction.hide(this)
            transaction.commit()
        }
    }

    private fun persistToStorage(listings: MutableList<Listing>?) {
        val editor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
        val gson = Gson()
        editor.putString("results", gson.toJson(listings))
        editor.apply()
    }

    private fun loadFromStorage() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        val gson = Gson()
        val json = prefs.getString("results", null)
        val itemType = object : TypeToken<MutableList<Listing>>() {}.type
        sharedViewModel.saveListings(gson.fromJson(json, itemType))
    }

    companion object {
        const val TAG = "ResultsFragment"
        const val LIMIT = 20
    }
}