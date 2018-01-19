package com.blackbelt.github

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import io.reactivex.Observable
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

object JsonFileReader {

    fun <T> read(inputStream: InputStream, gson: Gson, convertTo: Class<T>): Observable<T> {
        try {
            val jsonReader = JsonReader(InputStreamReader(inputStream))
            return Observable.just(gson.fromJson(jsonReader, convertTo))
        } finally {
            closeStream(inputStream)
        }
    }

    fun <T> readList(inputStream: InputStream, gson: Gson, listType: Type): Observable<List<T>> {
        try {
            val jsonReader = JsonReader(InputStreamReader(inputStream))
            return Observable.just(gson.fromJson(jsonReader, listType))
        } finally {
            closeStream(inputStream)
        }
    }

    private fun closeStream(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}