package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static String API_KEY = "api_key";
    private static String apiKey = "9346b156838a1d030580dfddf98237f4";


    public static URL buildUrl(String query) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(query)
                .appendQueryParameter(API_KEY, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Url" + url);
        return url;
    }

    public static URL buildImageUrl(String imageString) {
        imageString = imageString.replace("/", "");

        Uri builtUri = Uri.parse(BASE_IMAGE_URL).buildUpon()
                .appendPath("w185")
                .appendPath(imageString)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Url" + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}