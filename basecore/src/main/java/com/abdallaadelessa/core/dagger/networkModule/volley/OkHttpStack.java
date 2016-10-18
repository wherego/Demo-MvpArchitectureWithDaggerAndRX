package com.abdallaadelessa.core.dagger.networkModule.volley;


import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;


/**
 * An {@link com.android.volley.toolbox.HttpStack HttpStack} implementation
 * which uses OkHttp as its transport.
 */
public class OkHttpStack extends HurlStack {

    private OkUrlFactory mFactory;

    public OkHttpStack() {
        this(getClient());
    }

    public OkHttpStack(OkHttpClient client) {
        if(client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        mFactory = new OkUrlFactory(client);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return mFactory.open(url);
    }

    // ---------------->

    private static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        return builder.build();
    }
}