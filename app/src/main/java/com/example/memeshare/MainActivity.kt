package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //For Share Button Functioning
    var currentImageUrl: String? = null
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //4th Step---- call loadMeme function ------
        loadMeme()
    }
// 1st Step ------- Add--- implementation 'com.android.volley:volley:1.1.1'  ---dependency to build.gradle(Module)
//2nd Step.......Add...internet permission
//3rd step------Add Request queue and url to a new function----loadMeme---- and make that function private
//Add request queue and url from ----- Use newRequestQueue---in----Send a simple request....googleChrome(AndroidDevelopers)

    private fun loadMeme(){
// Instantiate the RequestQueue.
        //
        progressBar.visibility = View.VISIBLE
        //
    //Delete for Singleton Pattern.....  val queue = Volley.newRequestQueue(this)
    val url = "https://meme-api.herokuapp.com/gimme" // url of APImeme page (TOP ka url)

// Request a string response from the provided URL.

//            val stringRequest = StringRequest(Request.Method.GET, url,
//            { response ->
//               Log.d("success Request", response.substring(0, 500))
//            },
//            {
//                Log.d("error",it.localizedMessage)
//            }

   // NOTE : _____THis will  give String Response,___ For IMAGE we Need JSON RESPONSE___

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,null,
                Response.Listener { response ->
               // copy url of MemeAPI

                   //--- val url =response.getString("url")-----FOR SHARE BUTTON
                    currentImageUrl =response.getString("url")
                   //--------

                    // Now use GLIDE LIBRARY to put this url into IMAGE.......Add the dependencies

                   //------ Glide.with(this).load(url).into(memeImageView) -----(+) ProgressBar

                    //..... load(url)   ->->   load(currentImageUrl).... for SHARE BUTTON
                    Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                            TODO("Not yet implemented")
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                            TODO("Not yet implemented")
                        }
                    }).into(memeImageView)

                },
            {

            }


    )

// Add the request to the RequestQueue.
          //  Change this due to Singleton pattern ->    queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


    }

    fun shareMeme(view: View) {
           // use of intent
        val intent = Intent(Intent.ACTION_SEND)

        //     intent.type = "text/plain"    .....without this a text will appear....NO APPS CAN PEWRFORM THIS ACTION
        // Therefore, type btaana jaruuri hai .. ki...kis type ka data share karna hai!
        intent.type = "text/plain"

        intent.putExtra(Intent.EXTRA_TEXT,"Hey! Checkout this funny meme I got from Reddit $currentImageUrl")

           //create chooser..... konsi app se share karna hai meme ko??

        val chooser = Intent.createChooser(intent,"Share this meme using----")
        startActivity(chooser)
    }

    fun showNextMeme(view: View) {
        // call loadMeme() function to generate next new meme
        // Loader/ProgressBar for buffering generation between 2 memes-------- goto main.xml and create ProgressBar
     loadMeme()
    }
}