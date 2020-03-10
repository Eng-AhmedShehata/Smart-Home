package com.ashehata.smarthomealpha

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var serverResponse: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setButtonOn()
        setButtonOff()
        //createRequest()
    }


    private fun setButtonOff() {
        btn_off.setOnClickListener {
            post(et_ip.text.toString(), 0)
        }

    }

    private fun setButtonOn() {
        btn_on.setOnClickListener {
            post(et_ip.text.toString(), 1)

        }
    }

    private fun post(ip: String?, ledCase: Int) {
        GlobalScope.launch(Dispatchers.Main){
            makeRequest(ip, ledCase)
        }
    }

    private suspend fun makeRequest(ip: String?, ledCase: Int) {
        val serverAddress = "192.168.1.$ip" + ":" + "80"
        val url = "http://$serverAddress/led/$ledCase"
        val response = fetchResponse(url)
        printToast(response)

    }

    private fun printToast(response: String?) {
        Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
    }


    suspend fun fetchResponse(url: String): String? {
        return GlobalScope.async(Dispatchers.IO) {
            // make network call
            // return String
            val url = URL(url)

            try {
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "POST"  // optional default is GET

                    println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

                    inputStream.bufferedReader().use {
                        return@async it.readLine()
                    }
                }
            }catch (e: Exception) {
                return@async e.toString()
            }

        }.await()
    }


    /*
    try {
                val client: HttpClient = DefaultHttpClient()
                val getRequest = HttpGet()
                getRequest.setURI(URI(url))
                val response: HttpResponse = client.execute(getRequest)
                var inputStream: InputStream? = null
                inputStream = response.getEntity().getContent()
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                serverResponse = bufferedReader.readLine()
                inputStream.close()
            } catch (e: Exception) {

            }
     */

    fun sendGet(url: String) {
        val url = URL(url)

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"  // optional default is GET

            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

            inputStream.bufferedReader().use {
                serverResponse = it.readLine()
            }
            Toast.makeText(this@MainActivity, serverResponse, Toast.LENGTH_SHORT).show()
        }
    }

}
