package com.blackbelt.githubcodechallenge.android

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.reactivex.Observable
import java.io.Closeable
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object JsonFileReader {

    fun <T> read(context: Context, fileName: String, gson: Gson, convertTo: Class<T>): Observable<T> {
        return try {
            val inputStream = context.assets.open("mock/$fileName")
            return read(inputStream, gson, convertTo)
        } catch (e: Exception) {
            Observable.empty()
        }
    }

    fun <T> readList(context: Context, fileName: String, gson: Gson, listType: TypeToken<T>): Observable<T> {
        return try {
            val inputStream = context.assets.open("mock/$fileName")
            return Observable.just(readList(inputStream, gson, listType))
        } catch (e: Exception) {
            Observable.empty()
        }
    }

    fun <T> read(inputStream: InputStream, gson: Gson, convertTo: Class<T>): Observable<T> {
        try {
            val jsonReader = JsonReader(InputStreamReader(inputStream))
            return Observable.just(gson.fromJson(jsonReader, convertTo))
        } finally {
            closeStream(inputStream)
        }
    }

    fun <T> readList(inputStream: InputStream, gson: Gson, listType: TypeToken<T>): T {
        try {
            val jsonReader = JsonReader(InputStreamReader(inputStream))
            return gson.fromJson(jsonReader, listType.type)
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