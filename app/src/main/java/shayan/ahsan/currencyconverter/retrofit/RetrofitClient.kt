package shayan.ahsan.currencyconverter.retrofit

import android.app.Application
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RetrofitClient {
    private var BaseUrl = "https://v6.exchangerate-api.com/v6/5fc690fb66fdf50f3151a601/latest/"

    var  logging : HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun getClient(app: Application): ApiInterface {
        return Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                ))
            .client( OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build())
            .build().create(ApiInterface::class.java)

    }


}