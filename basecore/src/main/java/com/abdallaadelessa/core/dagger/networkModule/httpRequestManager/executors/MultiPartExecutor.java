package com.abdallaadelessa.core.dagger.networkModule.httpRequestManager.executors;

import android.text.TextUtils;

import com.abdallaadelessa.core.dagger.networkModule.httpRequestManager.BaseHttpExecutor;
import com.abdallaadelessa.core.dagger.networkModule.httpRequestManager.requests.MultiPartRequest;
import com.abdallaadelessa.core.dagger.networkModule.subModules.okhttpModule.DaggerOkHttpComponent;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MultiPartExecutor<M> extends BaseHttpExecutor<M, MultiPartRequest<M>> {
    private volatile Call call;

    //=====================>

    @Override
    public Observable<M> buildObservable(final MultiPartRequest multiPartRequest) {
        return Observable.create(new Observable.OnSubscribe<M>() {
            @Override
            public void call(Subscriber<? super M> subscriber) {
                try {
                    MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    //======> Files
                    List<MultiPartRequest.MultiPartFile> files = multiPartRequest.getFiles();
                    if (files != null) {
                        for (int i = 0; i < files.size(); i++) {
                            MultiPartRequest.MultiPartFile multiPartFileObj = files.get(i);
                            String fileUrl = multiPartFileObj.getUrl();
                            String fileName = TextUtils.isEmpty(multiPartFileObj.getName()) ? "file" + i : multiPartFileObj.getName();
                            String fileMimeType = multiPartFileObj.getMimeType();
                            File sourceFile = new File(fileUrl);
                            if (sourceFile.exists()) {
                                final MediaType MEDIA_TYPE = MediaType.parse(fileMimeType);
                                multipartBodyBuilder.addFormDataPart(fileName, sourceFile.getName(), RequestBody.create(MEDIA_TYPE, sourceFile));
                            }
                        }
                    }
                    //======> Parameters
                    Map<String, String> parameters = multiPartRequest.getFormParams();
                    if (parameters != null) {
                        for (String key : parameters.keySet()) {
                            String value = parameters.get(key);
                            multipartBodyBuilder.addFormDataPart(key, value);
                        }
                    }
                    // Send Request
                    RequestBody requestBody = multipartBodyBuilder.build();
                    Request request = new Request.Builder().url(multiPartRequest.getUrlWithQueryParams()).post(requestBody).build();
                    OkHttpClient client = DaggerOkHttpComponent.create().getOkHttpClientBuilder().writeTimeout(multiPartRequest.getTimeout(), TimeUnit.MILLISECONDS)
                            .readTimeout(multiPartRequest.getTimeout(), TimeUnit.MILLISECONDS).build();
                    call = client.newCall(request);
                    Response response = call.execute();
                    String responseStr = response.body().string();
                    onNext(subscriber, multiPartRequest, responseStr);
                    onCompleted(subscriber,multiPartRequest);
                } catch (Throwable e) {
                    onError(subscriber, multiPartRequest, e, false);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    @Override
    protected void cancelExecutor() {
        if (call != null) {
            call.cancel();
        }
    }

    //=====================>

}