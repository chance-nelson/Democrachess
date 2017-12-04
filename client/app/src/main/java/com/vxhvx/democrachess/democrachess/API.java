package com.vxhvx.democrachess.democrachess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/** Class for interfacing with the Democrachess REST API
 * Created by chance on 11/29/17.
 */

public class API {
    private String url = null;
    private String jwt = null;
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public API(String url) {
        this.url = url;
    }

    public String get_jwt() {
        return this.jwt;
    }

    public boolean register(String username, String password) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, "{username: "+ username + "," +
                                                            "password: "+ password + "}");

        HTTPPoster poster = new HTTPPoster(this.url + "/register", this.client, body);
        new Thread(poster).start();

        Response response = poster.get_response();
        while(response == null) response = poster.get_response();

        if(response.code() != 200) return false;

        jwt = new JSONObject(response.body().string()).getJSONObject("jwt").toString();

        return true;
    }

    public boolean login(String username, String password) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, "{username: "+ username + "," +
                "password: "+ password + "}");

        HTTPPoster poster = new HTTPPoster(this.url + "/register", this.client, body);
        new Thread(poster).start();

        Response response = poster.get_response();
        while(response == null) response = poster.get_response();

        if(response.code() != 200) return false;

        jwt = new JSONObject(response.body().string()).getJSONObject("jwt").toString();

        return true;
    }

    public boolean vote(String move) {
        return false;
    }

    public String[] get_player_info(String[] username) {
        return null;
    }
}
