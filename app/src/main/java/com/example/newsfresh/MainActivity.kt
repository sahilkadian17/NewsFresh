package com.example.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var madapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager= LinearLayoutManager(this)
        fetchdata()
        madapter = NewsListAdapter(this)
        recyclerView.adapter = madapter
    }
    private fun fetchdata(){
        val url= "https://saurav.tech/NewsAPI/everything/cnn.json"
        val jsonObjectRequest = JsonObjectRequest( Request.Method.GET, url, null,
            Response.Listener {
                val newsJsonArray=it.getJSONArray("articles")
                val newsArray=ArrayList<News>()
                for (i in 0 until newsJsonArray.length()){
                    val obj = newsJsonArray.getJSONObject(i)
                    val news= News(
                        obj.getString("title"),
                        obj.getString("author"),
                        obj.getString("url"),
                        obj.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                madapter.updateNews(newsArray)
            },
            Response.ErrorListener {
            }
        )
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}