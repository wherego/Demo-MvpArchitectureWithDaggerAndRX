package com.abdallaadelessa.core.dagger.networkModule.httpRequestManager;

import com.abdallaadelessa.core.dagger.networkModule.httpRequestManager.requests.BaseRequest;

/**
 * Created by abdullah on 12/26/16.
 */

public abstract class BaseHttpInterceptor {
    public BaseRequest interceptRequest(BaseRequest request) throws Exception {
        return request;
    }

    public String interceptResponse(BaseRequest request, String response) throws Exception {
        return response;
    }

    public Throwable interceptError(BaseRequest request, Throwable throwable, final boolean fatal) {
        return throwable;
    }
}
