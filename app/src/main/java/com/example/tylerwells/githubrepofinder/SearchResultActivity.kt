package com.example.tylerwells.githubrepofinder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {

    fun listRepos(repos: List<Repo>)
    {
        val listView = findViewById<ListView>(R.id.repoListView)
        val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, repos)

        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, i, l ->

            for(repo in repos) {

                val selectedRepo = repos[i]
                //open the url in a browser

                val intent = Intent(Intent.ACTION_VIEW,Uri.parse(selectedRepo.html_url))

                startActivity(intent)

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")
        val searchType = intent.getStringExtra("searchType")
        //println(searchTerm)

        val userCallback = object: Callback<List<Repo>>
        {
            override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                println("not working")
            }

            override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {

                if(response?.code() == 404)
                {
                    println("User Does not Exist")

                    Toast.makeText(this@SearchResultActivity,"User does not exist",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                else
                {
                    val repos = response?.body()

                    if (repos != null)
                    {

                        listRepos(repos!!)

                    }

                }
            }


        }

        val callback = object : Callback<GitHubSearchResult> {
            override fun onFailure(call: Call<GitHubSearchResult>?, t: Throwable?) {
                //println("not working :(")
            }

            override fun onResponse(call: Call<GitHubSearchResult>?, response: Response<GitHubSearchResult>?) {

                if(response?.code() == 404)
                {

                    Toast.makeText(this@SearchResultActivity,"No Repos by this name",Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                else
                {
                    val searchResult = response?.body()

                    if(searchResult != null)
                    {
                        listRepos(searchResult!!.items)

                    }

                }


            }


        }

        val retiever = GitHubRetriever()

        println("now Searching")
        if (searchType == "user")
        {
            println("userSearch")
            retiever.searchUsers(userCallback, searchTerm)


        }
        else {
            println("repo search")
            retiever.searchRepos(callback, searchTerm)
        }
    }
}

class RepoAdapter(context: Context?, resource: Int, objects: List<Repo>?) : ArrayAdapter<Repo>(context,resource,objects)
{
    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        val repoView = inflator.inflate(R.layout.repo_list_layout, parent, false)

        val cellTextView = repoView.findViewById<TextView>(R.id.repoTextView)
        val cellImageView = repoView.findViewById<ImageView>(R.id.repoImageView)

        val repo = getItem(position)
        Picasso.with(context).load(Uri.parse(repo.owner.avatar_url)).into(cellImageView)
        cellTextView.text = repo.full_name

        return repoView
    }


}