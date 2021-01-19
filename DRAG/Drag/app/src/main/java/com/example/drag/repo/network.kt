package com.example.drag.repo

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper


class RandomWordDTO(val id: Int, val word: String) {

}

class GetRandomWordsRequest(
        url: String,
        private val mapper: ObjectMapper,
        success: Response.Listener<List<RandomWordDTO>>,
        error: Response.ErrorListener
) : JsonRequest<List<RandomWordDTO>>(Request.Method.GET, url, "", success, error) {

    override fun parseNetworkResponse(response: NetworkResponse): Response<List<RandomWordDTO>> {
        println(String(response.data))
        val randomWordsDto : List<RandomWordDTO> = mapper.readValue(String(response.data), object : TypeReference<List<RandomWordDTO>>() {})
        return Response.success(randomWordsDto, null)
    }
}