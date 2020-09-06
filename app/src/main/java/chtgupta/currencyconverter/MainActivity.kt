package chtgupta.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivityAPI"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {

            val fromInput = fromCurrency.text.toString().trim()
            val toInput = toCurrency.text.toString().trim()
            val countInput = count.text.toString().trim()

            when {
                fromInput.isEmpty() -> fromCurrency.error = "This field is required"
                toInput.isEmpty() -> toCurrency.error = "This field is required"
                countInput.isEmpty() -> count.error = "This field is required"
                else -> callAPI(fromInput, toInput)
            }

        }

    }

    private fun callAPI(fromCurrency: String, toCurrency: String) {

        val url = "https://free.currconv.com/api/v7/convert?q=${fromCurrency}_${toCurrency}&apiKey=f71ff0484710ae039196"
        val request = StringRequest(Request.Method.GET, url, Response.Listener { response ->

            Log.d(TAG, "callAPI: $response")

            try {

                val json = JSONObject(response)
                val currencyKey = "${fromCurrency}_${toCurrency}"

                val resultsObject = json.getJSONObject("results")
                val currencyJson = resultsObject.getJSONObject(currencyKey)
                val value = currencyJson.getString("val").toFloat()

                val countInput = count.text.toString().trim().toFloat()
                val answer = value * countInput

                result.text = answer.toString()

            } catch (e : Exception) {
                Log.d(TAG, "callAPI: $e")
                result.text = "Exception encountered"
            }

        }, Response.ErrorListener { error ->

            Log.d(TAG, "callAPI: error: ${error.networkResponse.statusCode}")
            result.text = "Error ${error.networkResponse.statusCode}"

        })
        Volley.newRequestQueue(this).add(request)
        Log.d(TAG, "callAPI: requesting to $url")
        result.text = "Loading..."

    }

}