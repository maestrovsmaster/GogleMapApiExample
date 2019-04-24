package com.maestromaster.foursquaremapexample;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.maestromaster.foursquaremapexample.MapsActivity.TAG;


public class App extends Application {


    private static FoursquareApi foursquareApi;
    private Retrofit retrofit;

    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        LoggingInterceptor logging = new LoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);


        GsonBuilder gbuilder = new GsonBuilder();

        gson = gbuilder.create();


        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        foursquareApi = retrofit.create(FoursquareApi.class);


    }

    public static FoursquareApi getFoursquareApi(){

        return  foursquareApi;

    }



    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers());

            if (request.method().compareToIgnoreCase("post") == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request);
            }
            Log.d(TAG, "request" + "\n" + requestLog);

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            String responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());

            String bodyString = response.body().string();

            Log.d(TAG, "response" + "\n" + responseLog + "\n" + bodyString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
            //return response;
        }
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }






}
