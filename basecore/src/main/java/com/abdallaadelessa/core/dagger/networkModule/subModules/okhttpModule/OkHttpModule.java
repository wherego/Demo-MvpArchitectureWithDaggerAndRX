package com.abdallaadelessa.core.dagger.networkModule.subModules.okhttpModule;

import com.abdallaadelessa.core.app.BaseCoreApp;
import com.android.volley.toolbox.HttpStack;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by abdullah on 12/28/16.
 */
@Module
public class OkHttpModule {

    @Provides
    public OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient().newBuilder().cache(new Cache(new File(BaseCoreApp.getAppDownloadsPath()), 1024 * 1024 * 10));
    }


    @Provides
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder) {
        return builder.build();
    }

    @Provides
    public HttpStack provideHttpStack(OkHttpClient okHttpClient) {
        return new OkHttpStack(okHttpClient);
    }
}
