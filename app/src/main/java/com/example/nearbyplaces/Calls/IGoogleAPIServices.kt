
package com.example.nearbyplaces.Calls

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleAPIServices {
    @GET
    //TODO api calls pending
    fun getMyNearbyPlaces(@Url url:String)call
}
