package com.example.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

//https://api.weatherapi.com/v1/current.json?key=b185e4362edf430d90b92737233001&q=multan&aqi=yes
class MainActivity : AppCompatActivity() {
//    val city = "multan,pk"
//    val api = "b185e4362edf430d90b92737233001"
    val CITY: String = "multan,pk"
    val API: String = "06c921750b9a82d8f5d1294e1586276f"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch {
            val response:String? = try {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)

            } catch (e:java.lang.Exception){
                null
            }

            withContext(Dispatchers.Main){
                binding.apply {
                    loadar.visibility = View.VISIBLE
                    temp.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                }

                try {
                    val jsonObj = JSONObject(response)
                    val main  = jsonObj.getJSONObject("main")
                    val sys = jsonObj.getJSONObject("sys")
                    val wind = jsonObj.getJSONObject("wind")
                    val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                    val updatedAt = jsonObj.getLong("dt")
                    val updateAtText = "Updated at: "+SimpleDateFormat("dd/mm/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt*1000)
                    )
                    val temp = main.getString("temp")+"°C"
                    val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
                    val temMax = "Max Temp: "+main.getString("temp_max")+"°C"
                    val pressure = main.getString("pressure")
                    val humidity = main.getString("humidity")
//                val windSpeed = main.getString("speed")
                    val sunrise = sys.getLong("sunrise")
                    val sunset = sys.getLong("sunset")
                    val weatherDescription = weather.getString("description")
                    val  address = jsonObj.getString("name")+","+sys.getString("country")
                    binding.address.text = address
                    binding.temp.text = temp
                    binding.minTemp.text = tempMin
                    binding.maxTemp.text = temMax
//                binding.wind.text = windSpeed
                    binding.pressure.text = pressure
                    binding.humidity.text = humidity
                    binding.status.text = weatherDescription.capitalize()
                    binding.updatedAt.text = updateAtText
                    binding.sunrise.text = SimpleDateFormat(" hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                    binding.sunset.text =SimpleDateFormat(" hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                    binding.loadar.visibility = View.GONE
                    binding.temp.visibility = View.VISIBLE
                }catch (e:java.lang.Exception){
                    binding.loadar.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE
                    Log.d("ERROR","${e.message}")
                }
            }
        }
    }

}