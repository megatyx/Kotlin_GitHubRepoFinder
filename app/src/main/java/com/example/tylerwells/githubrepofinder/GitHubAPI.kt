package com.example.tylerwells.githubrepofinder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by tyler.wells on 2017/09/14.
 */


interface GitHubService {

    @GET("search/repositories?q={}")
    fun searchRepos(@Query("q") searchTerm: String) : Call<GitHubSearchResult>

    @GET("users/{user}/repos")
    fun searchUsers(@Path("user") searchTerm: String) : Call<List<Repo>>




}

class GitHubSearchResult(val items: List<Repo>) {

}

class Repo(val full_name: String, val owner: GitHubUser, val html_url: String){
}

class GitHubUser(val avatar_url: String){
}

class GitHubRetriever {

    val service: GitHubService

    init {

        val retroFit = Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build()
        service = retroFit.create(GitHubService::class.java)
    }

    fun searchRepos(callback: Callback<GitHubSearchResult>, searchTerm: String)
    {
        var searchT = searchTerm
        if (searchT == "") {
            searchT = "Eggs"
        }

        val call = service.searchRepos(searchT)
        call.enqueue(callback)


    }

    fun searchUsers(callback: Callback<List<Repo>>, searchTerm: String)
    {
        var searchT = searchTerm
        if (searchT == "") {
            searchT = "Eggs"
        }

        val call = service.searchUsers(searchT)
        call.enqueue(callback)

    }

}