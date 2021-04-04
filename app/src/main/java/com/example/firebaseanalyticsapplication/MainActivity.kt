package com.example.firebaseanalyticsapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseanalyticsapplication.adapter.CategoryAdapter
import com.example.firebaseanalyticsapplication.model.Category
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
     var startHour  = 0
     var endHour  = 0
     var startMinute  = 0
     var endMinute  = 0
     var startSecond  = 0
     var endSecond  = 0

    lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db= Firebase.firestore
        firebaseAnalytics = Firebase.analytics
        getCategory()
        trackScreen("main screen")


    }

//    private fun selectContent(id:String , pageName:String , time:String){
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
//            param(FirebaseAnalytics.Param.ITEM_ID, id)
//            param(FirebaseAnalytics.Param.ITEM_NAME, pageName)
//            param(FirebaseAnalytics.Param.CONTENT_TYPE, time)
//        }
//    }
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

    private fun trackScreen(screenName:String){
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
    }
    private fun getCategory(){
        val category=mutableListOf<Category>()
        db.collection("Categories")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){

                    category.add(
                        Category(document.getString("name")
                        )
                    )

                    rvCategory.layoutManager = LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,false)
                    rvCategory.setHasFixedSize(true)
                    val restAdapter = CategoryAdapter(this,category)
                    rvCategory.adapter=restAdapter
                }

            }
            .addOnFailureListener { exception ->
                Log.e("nor", exception.message!!)
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

        Toast.makeText(this,"$hour:$minute:$second",Toast.LENGTH_SHORT).show()
        Log.e("Nor","$hour:$minute:$second")

        var time = "$hour:$minute:$second"
        var id = java.util.UUID.randomUUID().toString()
        var pageName = "Category screen"
        addUserToDb(id, pageName, time)



    }
}