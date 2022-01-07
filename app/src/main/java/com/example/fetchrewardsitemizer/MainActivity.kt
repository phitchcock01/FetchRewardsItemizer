package com.example.fetchrewardsitemizer

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.ColorStateList.*
import android.graphics.PorterDuff
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.json.JSONArray
import org.json.JSONException
import java.net.URL
import android.graphics.drawable.Drawable




class MainActivity : AppCompatActivity() {

    //
    // Attributes
    //

    // Constants
    val DATA_URL: String = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

    // Variables
    private lateinit var itemAdapter: ItemAdapter

    //
    // Methods
    //
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemAdapter = ItemAdapter(mutableListOf())

        itemListRecyclerView.adapter = itemAdapter
        itemListRecyclerView.layoutManager = LinearLayoutManager(this)

        itemsLoadingProgressBar.visibility = View.VISIBLE

        // Do async data fetch
        doAsync {

            try {
                // Read in JSON result string from URL
                val result = URL(DATA_URL).readText()

                // Get array of item objects from JSON result string
                val itemArray = JSONArray(result)
                val items: MutableList<Item> = mutableListOf<Item>()
                for (i in 0 until itemArray.length()) {
                    val itemDetail = itemArray.getJSONObject(i)
                    val id: Long = itemDetail.getLong("id")
                    val listId: Long = itemDetail.getLong("listId")
                    val name: String? = itemDetail.getString("name")
                    if (name != null && name.isNotEmpty() && name !== "null") {
                        items.add(Item(id, listId, name))
                    }
                }

                // Sort array by list ID, then name
                items.sortBy { item -> item.name }
                items.sortBy { item -> item.listId }

                uiThread {
                    try {
                        // Add array into adapter
                        for (i in 0 until items.size) {
                            itemAdapter.addItem(items[i])
                        }
                    } catch (e: JSONException) {
                        e.message?.let { it1 -> toast(it1) }
                        e.printStackTrace()
                    }
                }
            } catch(e: Exception) {
                uiThread {
                    e.message?.let { it1 -> toast(it1) }
                    e.printStackTrace()
                }
            } finally {
                uiThread {
                    itemsLoadingProgressBar.visibility = View.GONE
                }
            }
        }
    }
}