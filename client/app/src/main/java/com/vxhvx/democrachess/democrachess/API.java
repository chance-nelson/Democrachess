package com.vxhvx.democrachess.democrachess;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;


/** Class for interfacing with the Democrachess REST API
 * Created by chance on 11/29/17.
 */

public class API{
    private String url = null;
    private String jwt = null;
    private OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String username;

    public API(String url) {
        this.url = url;
    }

    public API(String url, String jwt) {
        this.url = url;
        this.jwt = jwt;
    }

    public API(String url, String jwt, String username) {
        this.url = url;
        this.jwt = jwt;
        this.username = username;
    }

    public String get_jwt() {
        return this.jwt;
    }

    public String get_username() {return this.username; }

    public boolean register(String username, String password, int team) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, "{\"username\":\""+ username + "\"," +
                                                            "\"password\":\""+ password + "\"," +
                                                            "\"team\":" + Integer.toString(team) + "}");

        HTTPPoster poster = new HTTPPoster(this.url + "/register", this.client, body);
        new Thread(poster).start();

        Response response = poster.get_response();
        while(response == null) response = poster.get_response();

        if(response.code() != 200) return false;

        jwt = new JSONObject(response.body().string()).getString("jwt");
        jwt = jwt.substring(2, jwt.length() - 1);

        this.username = username;

        return true;
    }

    public boolean login(String username, String password) throws IOException, JSONException {
        RequestBody body = RequestBody.create(JSON, "{\"username\":\""+ username + "\"," +
                                                            "\"password\":\""+ password + "\"" + "}");

        HTTPPoster poster = new HTTPPoster(this.url + "/auth", this.client, body);
        new Thread(poster).start();

        Response response = poster.get_response();
        while(response == null) response = poster.get_response();

        if(response.code() != 200) return false;

        jwt = new JSONObject(response.body().string()).getString("jwt");
        jwt = jwt.substring(2, jwt.length() - 1);

        this.username = username;

        return true;
    }

    public String[] get_game_state() throws IOException, JSONException {
        HTTPGetter getter = new HTTPGetter(this.url + "/game", this.client,
                                   "Authorization",
                                   "Bearer " + this.jwt);

        new Thread(getter).start();

        Response response = getter.get_response();
        while(response == null) response = getter.get_response();

        if(response.code() != 200) return null;

        String json = getter.get_response().body().string();

        while(json == null) {
            json = getter.get_response().body().string();
        }

        String gameState = new JSONObject(json).getString("state");
        JSONObject votes = new JSONObject(json).getJSONObject("votes");
        double nextVoteAt = new JSONObject(json).getDouble("nextvoteat");

        return new String[]{gameState, votes.toString(), Double.toString(nextVoteAt)};
    }

    public boolean vote(String move) {
        RequestBody body = RequestBody.create(JSON, "{\"vote\":\""+ move + "\"}");

        HTTPPoster poster = new HTTPPoster(this.url + "/game", this.client, body,
                                   "Authorization",
                                   "Bearer " + this.jwt);

        new Thread(poster).start();

        Response response = poster.get_response();
        while(response == null) response = poster.get_response();

        if(response.code() != 200) return false;

        return true;
    }

    public String[] get_player_info(String username) throws IOException, JSONException {
        HTTPGetter getter = new HTTPGetter(this.url + "/player/" + username,
                this.client, null, null);

        new Thread(getter).start();

        Response response = getter.get_response();
        while(response == null) response = getter.get_response();

        if(response.code() != 200) return null;

        String json = getter.get_response().body().string();

        while(json == null) {
            json = getter.get_response().body().string();
        }

         return new String[] {
                new JSONObject(json).getString("username"),
                new JSONObject(json).getString("team"),
                new JSONObject(json).getString("wins"),
                new JSONObject(json).getString("losses")
        };
    }
}
