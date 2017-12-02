package com.vxhvx.democrachess.democrachess;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chance on 12/1/17.
 */

public class HTTPPoster implements Runnable {
    private RequestBody body;
    private OkHttpClient client;
    private String url;
    private volatile Response response = null;

    public HTTPPoster(String url, OkHttpClient client, RequestBody body) {
        this.url = url;
        this.client = client;
        this.body = body;
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

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
