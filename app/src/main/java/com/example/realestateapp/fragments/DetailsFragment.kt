package com.example.realestateapp.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.realestateapp.R
import com.example.realestateapp.models.SharedViewModel
import java.util.*


class DetailsFragment: Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var tvPrice: TextView
    lateinit var tvBedRooms: TextView
    lateinit var tvBathRooms: TextView
    lateinit var tvPropertyID: TextView
    lateinit var ivMainPhoto: ImageView
    lateinit var tvYearBuilt: TextView
    lateinit var tvSquareFoot: TextView
    lateinit var tvLotSize: TextView
    lateinit var tvHomeType: TextView
    lateinit var tvAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate the layout for the details fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.listing.observe(viewLifecycleOwner) { listing ->
            var ivMainPhoto = view.findViewById<ImageView>(R.id.ivMainPhoto)
            Glide.with(view.context).load(listing.primaryPhoto).error(ColorDrawable(Color.LTGRAY)).into(ivMainPhoto)
            tvPrice = view.findViewById(R.id.tvPrice)
            tvPrice.text = "$" + listing.listPrice.toString()
            tvBedRooms = view.findViewById(R.id.tvBedRooms)
            tvBedRooms.text = listing.beds.toString() + " bds"
            tvBathRooms = view.findViewById(R.id.tvBathRooms)
            tvBathRooms.text = listing.baths.toString() + " ba"
            tvPropertyID = view.findViewById(R.id.tvPropertyID)
            tvPropertyID.text = "Listing ID: " + listing.propertyID.toString()
            tvYearBuilt = view.findViewById(R.id.tvYearBuilt)
            tvYearBuilt.text = "Year Built: " + listing.yearBuilt.toString()
            tvSquareFoot = view.findViewById(R.id.tvSquareFoot)
            tvSquareFoot.text = "Square Footage: " + listing.sqft.toString()
            tvLotSize = view.findViewById(R.id.tvLotSize)
            tvLotSize.text = "Lot Size: " + listing.lotSqft.toString()
            tvHomeType = view.findViewById(R.id.tvHomeType)
            tvHomeType.text = "Home Type: " + listing.type.toString()
            tvAddress = view.findViewById(R.id.tvAddress)
            tvAddress.text = listing.streetAddr + ", " + listing.city + ", " + listing.stateCode + " " + listing.postalCode
        }
        var closeBtn = view.findViewById<ImageButton>(R.id.closeBtn)
//        closeBtn.setOnClickListener(closeDetails())
    }

    private fun closeDetails(): View.OnClickListener? {
        parentFragmentManager.popBackStack()
        return null
    }
}
