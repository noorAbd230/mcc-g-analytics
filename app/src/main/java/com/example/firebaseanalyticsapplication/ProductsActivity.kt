package com.example.firebaseanalyticsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseanalyticsapplication.adapter.CategoryAdapter
import com.example.firebaseanalyticsapplication.adapter.ProductAdapter
import com.example.firebaseanalyticsapplication.model.Category
import com.example.firebaseanalyticsapplication.model.Products
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_products.*
import java.text.SimpleDateFormat
import java.util.*

class ProductsActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var firebaseAnalytics: FirebaseAnalytics
    lateinit var name : String
    var startHour  = 0
    var endHour  = 0
    var startMinute  = 0
    var endMinute  = 0
    var startSecond  = 0
    var endSecond  = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        db= Firebase.firestore
        firebaseAnalytics = Firebase.analytics
         name = intent.getStringExtra("productName")!!
        getProduct()
        trackScreen("product screen")
    }

    private fun trackScreen(screenName:String){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }
    private fun getProduct(){
        val product=mutableListOf<Products>()
        db.collection("Categories").whereEqualTo("name",name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){

                    db.collection("Categories").document(document.id).collection("Products")
                        .get()
                        .addOnSuccessListener { querySnapshott ->
                            for (documentT in querySnapshott) {
                                product.add(
                                    Products(
                                        documentT.getString("productName"),
                                        documentT.getString("productDesc"),
                                        documentT.getString("price"),
                                        documentT.getString("img")
                                    )
                                )

                                rvProduct.layoutManager = LinearLayoutManager(
                                    this,
                                    LinearLayoutManager.VERTICAL, false
                                )
                                rvProduct.setHasFixedSize(true)
                                val productAdapter = ProductAdapter(this, product)
                                rvProduct.adapter = productAdapter
                            }
                        }

                }

            }
            .addOnFailureListener { exception ->
                Log.e("nor", exception.message!!)
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
        var second =   endSecond - startSecond

        Toast.makeText(this,"$hour:$minute:$second", Toast.LENGTH_SHORT).show()
        Log.e("Nor","$hour:$minute:$second")

        var time = "$hour:$minute:$second"
        var id = java.util.UUID.randomUUID().toString()
        var pageName = "Product screen"
        addUserToDb(id, pageName, time)


    }
}