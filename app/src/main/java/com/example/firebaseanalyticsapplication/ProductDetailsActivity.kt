package com.example.firebaseanalyticsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseanalyticsapplication.adapter.ProductAdapter
import com.example.firebaseanalyticsapplication.model.Category
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_products.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var firebaseAnalytics: FirebaseAnalytics
    var startHour  = 0
    var endHour  = 0
    var startMinute  = 0
    var endMinute  = 0
    var startSecond  = 0
    var endSecond  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        db= Firebase.firestore
       var name = intent.getStringExtra("pName")!!
       var desc = intent.getStringExtra("pDesc")!!
       var price = intent.getStringExtra("pPrice")!!
       var img = intent.getStringExtra("pImg")!!
        firebaseAnalytics = Firebase.analytics

        pName.text = name
        productDesc.text = desc
        productPrice.text = price
        Picasso.get().load(img).into(productImg)
        trackScreen("product details screen")
    }

    private fun trackScreen(screenName:String){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }

    fun addUserToDb(id:String,pageName:String,time:String){
        val user= hashMapOf("id" to id,"pageName" to pageName,"time" to time)
        db.collection("UsersTrack")
                .add(user)
                .addOnSuccessListener {documentReference->
                    Toast.makeText(applicationContext,"User added successfully", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"User does not added", Toast.LENGTH_SHORT).show()

                }
    }
    override fun onResume() {
        super.onResume()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())

        val values = currentDate.split(":".toRegex()).toTypedArray()
        startHour = values[0].toInt()
        startMinute = values[1].toInt()
        startSecond = values[2].toInt()
    }

    override fun onPause() {
        super.onPause()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())

        val values = currentDate.split(":".toRegex()).toTypedArray()
        endHour = values[0].toInt()
        endMinute = values[1].toInt()
        endSecond = values[2].toInt()
        // Toast.makeText(this,"$currentDate",Toast.LENGTH_SHORT).show()
        var hour =   endHour - startHour
        var minute =   endMinute - startMinute
        var second =   abs(endSecond - startSecond)

        Toast.makeText(this,"$hour:$minute:$second", Toast.LENGTH_SHORT).show()
        Log.e("Nor","$hour:$minute:$second")

        var time = "$hour:$minute:$second"
        var id = java.util.UUID.randomUUID().toString()
        var pageName = "Product Details screen"
        addUserToDb(id, pageName, time)


    }
}