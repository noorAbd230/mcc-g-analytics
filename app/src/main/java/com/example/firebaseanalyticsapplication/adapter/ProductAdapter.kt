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
import com.example.firebaseanalyticsapplication.ProductDetailsActivity
import com.example.firebaseanalyticsapplication.ProductsActivity
import com.example.firebaseanalyticsapplication.R
import com.example.firebaseanalyticsapplication.model.Category
import com.example.firebaseanalyticsapplication.model.Products
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.categories_item.view.*
import kotlinx.android.synthetic.main.product_item.view.*


class ProductAdapter(var activity: Activity, var data: MutableList<Products>) :
    RecyclerView.Adapter<ProductAdapter.MyViewHolder>()  {
    lateinit var firebaseAnalytics: FirebaseAnalytics

    lateinit var db: FirebaseFirestore
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var product = itemView.productName



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.product_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        firebaseAnalytics = Firebase.analytics
        holder.product.text = data[position].name

        holder.product.setOnClickListener {
            trackScreen("${data[position].name} Product")
            var i= Intent(activity, ProductDetailsActivity::class.java)
            i.putExtra("pName",data[position].name)
            i.putExtra("pDesc",data[position].desc)
            i.putExtra("pPrice",data[position].price)
            i.putExtra("pImg",data[position].img)

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