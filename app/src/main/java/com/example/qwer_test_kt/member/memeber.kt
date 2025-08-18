package com.example.qwer_test_kt.member

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException

data class Member(
    val name: String,
    val profileImageResId: String,
    val wallPaperImageUrls: List<String>
)

fun loadMembersFromAssets(context: Context): List<Member> {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val type = Types.newParameterizedType(List::class.java, Member::class.java)
    val adapter: JsonAdapter<List<Member>> = moshi.adapter(type)

    return try {
        context.assets.open("member.json").bufferedReader().use {
            adapter.fromJson(it.readText()) ?: emptyList()
        }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        emptyList()
    }
}