/** Class for interfacing with the Democrachess REST API
 * @author Chance Nelson
 */

package com.vxhvx.democrachess.democrachess;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class API{
    private String url = null;                         // URL of the API's server
    private String jwt = null;                         // JSON Web Token granted to user
    private String username = null;                    // Username of user interfacing with API
    private OkHttpClient client = new OkHttpClient();  // OkHttpClient object for sending requests

    // Declare the MediaType for JSON strings in the body
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Constructor for just setting the URL and nothing more. Used when the app is first opened
     */
    public API(String url) {
        this.url = url;
    }

    /**
     * Constructor for setting both the url and jwt
     */
    public API(String url, String jwt) {
        this.url = url;
        this.jwt = jwt;
    }

    /**
     * Constructor for setting the url, JWT, and username. Used on init of GameActivity
     */
    public API(String url, String jwt, String username) {
        this.url = url;
        this.jwt = jwt;
        this.username = username;
    }

    /**
     * Get the JSON Web Token
     */
    public String get_jwt() {

        return this.jwt;
    }

    /**
     * Get the username
     */
    public String get_username() {
        return this.username;
    }

    /**
     * Attempt to register with the API
     * @param username
     * @param password
     * @param team
     * @return If registration is successful, this.username and this.jwt are set, and true is returned.
     *         If it is unsuccessful, false is returned and the fields are not set.
     * @throws IOException There was a problem parsing the request on OkHttp's part
     * @throws JSONException JSON was not readed successfully
     */
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

    /**
     * Attempt to login with the API
     * @param username
     * @param password
     * @return If registration is successful, this.username and this.jwt are set, and true is returned.
     *         If it is unsuccessful, false is returned and the fields are not set.
     * @throws IOException There was a problem parsing the request on OkHttp's part
     * @throws JSONException JSON was not readed successfully
     */
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

    /**
     * Retrieve the game state from the API
     * @return If a 200 response is received (JWT verification was successful), a String array of
     *         length 3 is returned of the structure ["A FEN String representing the board",
     *         "A JSON string containing current vote stats for the turn",
     *         "Unix timestamp for when the next check of votes will occur on the server"].
     *         Otherwise, null is returned
     * @throws IOException OkHttp error
     * @throws JSONException JSON string received could not be parsed
     */
    public String[] get_game_state() throws IOException, JSONException {
        // Initialize a HTTPGetter object
        HTTPGetter getter = new HTTPGetter(this.url + "/game", this.client,
                                   "Authorization",
                                   "Bearer " + this.jwt);

        new Thread(getter).start();  // Send the request

        // Retrieve the response. Loop until the response is received
        Response response = getter.get_response();
        while(response == null) response = getter.get_response();

        // If a non-200 response is sent back, return null
        if(response.code() != 200) return null;

        // Get the JSON string
        String json = getter.get_response().body().string();

        while(json == null) {
            json = getter.get_response().body().string();
        }

        // Get the needed values
        String gameState = new JSONObject(json).getString("state");
        JSONObject votes = new JSONObject(json).getJSONObject("votes");
        double nextVoteAt = new JSONObject(json).getDouble("nextvoteat");

        // Return array
        return new String[]{gameState, votes.toString(), Double.toString(nextVoteAt)};
    }

    /**
     * Send a move vote to the server
     * @param move Valid chess move string (ex "a1b2", "b3a4", etc.)
     * @return If the vote is counted by the server, true is returned. Else false
     */
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

    /**
     * Get the information of a certain player
     * @param username username of player
     * @return String array containing all user information of structure ["username", "team", "wins",
     *         "losses"]
     * @throws IOException
     * @throws JSONException
     */
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
