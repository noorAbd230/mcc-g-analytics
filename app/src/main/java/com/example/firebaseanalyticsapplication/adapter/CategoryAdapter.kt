package com.example.firebaseanalyticsapplication.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseanalyticsapplication.ProductsActivity
import com.example.firebaseanalyticsapplication.R
import com.example.firebaseanalyticsapplication.model.Category
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.categories_item.view.*


class CategoryAdapter(var activity: Activity, var data: MutableList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>()  {
    lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var db: FirebaseFirestore
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var category = itemView.categoryName



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.categories_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        firebaseAnalytics = Firebase.analytics
        holder.category.text = data[position].name

        holder.category.setOnClickListener {
            trackScreen("${data[position].name} category ")
            var i= Intent(activity, ProductsActivity::class.java)
            i.putExtra("productName",data[position].name)

            activity.startActivity(i)
        }









    }


    fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun trackScreen(screenName:String){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }

}