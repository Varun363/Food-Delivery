package Zomato.Copy

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// Data model for API responses with null safety
data class Restaurant(
    val name: String? = "",
    val type: String? = "",
    val rating: Double? = 0.0,
    val time: Int? = 0,
    val price: String? = ""
)

data class OrderRequest(
    val userId: String,
    val items: List<String>,
    val totalAmount: Double
)

// Adding timeouts to prevent the app from "hanging"
private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8080/")
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface ApiService {
    @GET("orders/fetch")
    suspend fun getRestaurants(): List<Restaurant>

    @POST("orders/save")
    suspend fun saveOrder(@Body order: OrderRequest): Any
}

val apiService: ApiService = retrofit.create(ApiService::class.java)
