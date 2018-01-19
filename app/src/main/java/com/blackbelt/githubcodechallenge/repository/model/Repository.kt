package com.blackbelt.githubcodechallenge.repository.model

import android.os.Parcel
import android.os.Parcelable


data class Repository(
        val ownerId: Int? = -1,
        val repositoryId: Int? = -1,
        val ownerName: String? = null,
        val ownerUrl: String? = null,
        val repositoryName: String? = null,
        val description: String? = null,
        val nuOfForks: Int? = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(ownerId)
        writeValue(repositoryId)
        writeString(ownerName)
        writeString(ownerUrl)
        writeString(repositoryName)
        writeString(description)
        writeValue(nuOfForks)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Repository> = object : Parcelable.Creator<Repository> {
            override fun createFromParcel(source: Parcel): Repository = Repository(source)
            override fun newArray(size: Int): Array<Repository?> = arrayOfNulls(size)
        }
    }
}