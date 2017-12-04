package com.vxhvx.democrachess.democrachess;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.Serializable;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by chance on 12/1/17.
 */

public class HTTPPoster implements Runnable{
    private RequestBody body;
    private OkHttpClient client;
    private String url;
    private volatile Response response = null;
    private String headerName = null;
    private String headerValue = null;

    public HTTPPoster(String url, OkHttpClient client, RequestBody body) {
        this.url = url;
        this.client = client;
        this.body = body;
    }

    public HTTPPoster(String url, OkHttpClient client, RequestBody body, String headerName, String headerValue) {
        this.url = url;
        this.client = client;
        this.body = body;
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public void run() {
        Request request;

        if(headerName != null) {
            request = new Request.Builder()
                    .url(this.url)
                    .post(this.body)
                    .addHeader(headerName, headerValue)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(this.url)
                    .post(this.body)
                    .build();
        }

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response get_response() {
        return this.response;
    }
}
