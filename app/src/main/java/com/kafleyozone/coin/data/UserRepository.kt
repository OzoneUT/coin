package com.kafleyozone.coin.data

import android.util.Log
import com.kafleyozone.coin.data.models.LoginResponse
import com.kafleyozone.coin.data.models.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend fun login(basicValue: String) : Resource<LoginResponse> {
        lateinit var r: Resource<LoginResponse>
        try {
            authenticationService.login(basicValue).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    r = Resource.success(response.body())
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    r = Resource.error(t.message!!, null)
                }
            })
        } catch (e: Exception) {
            r = Resource.error(e.message!!, null)
        }

        return r
    }
}