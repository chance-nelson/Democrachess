package com.vxhvx.democrachess.democrachess;

/**
 * Created by chance on 12/1/17.
 */

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HTTPGetter implements Runnable{
    private OkHttpClient client;
    private String url;
    private volatile Response response = null;
    private String headerName = null;
    private volatile String headerValue = null;

    public HTTPGetter(String url, OkHttpClient client, String headerName, String headerValue) {
        this.url = url;
        this.client = client;
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public void run() {
        Request request;

        if(this.headerValue != null && this.headerName != null) {
            request = new Request.Builder()
                    .url(this.url)
                    .addHeader(this.headerName, this.headerValue)
                    .get()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(this.url)
                    .get()
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
