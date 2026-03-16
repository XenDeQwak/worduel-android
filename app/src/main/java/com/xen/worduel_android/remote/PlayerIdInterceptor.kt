package com.xen.worduel_android.remote

import okhttp3.Interceptor
import okhttp3.Response

class PlayerIdInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        PlayerIdHolder.guestId?.let { guestId ->
            request = request.newBuilder()
                .header("X-Player-ID", guestId)
                .build()
        }
        val response = chain.proceed(request)

        response.header("X-Player-ID")?.let { newGuestId ->
            PlayerIdHolder.guestId = newGuestId
        }

        return response
    }

}