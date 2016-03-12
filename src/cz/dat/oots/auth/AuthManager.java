package cz.dat.oots.auth;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Logger;

public class AuthManager {
    private final String AUTHSERVICE_URL = "http://ondryasondra.aspone.cz/tokengen.ashx";
    private final String CHECKSERVICE_URL = "http://ondryasondra.aspone.cz/tokencheck.ashx";
    private User currentUser;

    public AuthManager() {
    }

    public AuthManager(String userName, String password, String token) {
        this.currentUser = new User(userName, password);
        this.currentUser.setToken(token);

        AuthStatus a = isAuthorized();
        if (a == AuthStatus.FINE) {
            this.currentUser.setAuthenticated();
        } else {
            Logger.getGlobal().info(
                    "User " + userName + " is not authenticated! -- "
                            + a.toString());
        }
    }

    public void setDummyName(String name) {
        if (this.currentUser == null)
            this.currentUser = new User(name, "badpassword");
        else {
            this.currentUser.setUserName(name);
        }
    }

    public String getUserName() {
        if (currentUser == null)
            return "Player";

        return currentUser.getUserName();
    }

    public String getToken() {
        if (this.currentUser == null || !this.currentUser.isAuthenticated())
            return null;

        return this.currentUser.getToken();
    }

    public AuthStatus authenticate(String userName, String password) {
        String par;

        try {
            par = "username=" + URLEncoder.encode(userName, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return AuthStatus.ERROR_OTHER;
        }

        String result = this.excutePost(AUTHSERVICE_URL, par);

        if (result == null || result.isEmpty())
            return AuthStatus.ERROR_OTHER;

        try {
            return AuthStatus.valueOf(result.trim());
        } catch (Exception e) {
            currentUser = new User(userName, password);
            currentUser.setToken(result.trim());
            currentUser.setAuthenticated();
            return AuthStatus.FINE;
        }
    }

    public AuthStatus isAuthorized() {
        if (currentUser == null)
            return AuthStatus.ERROR_UNLOGGED;

        String par;

        try {
            par = "username="
                    + URLEncoder.encode(currentUser.getUserName(), "UTF-8")
                    + "&token="
                    + URLEncoder.encode(currentUser.getToken(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return AuthStatus.ERROR_OTHER;
        }

        String result = this.excutePost(CHECKSERVICE_URL, par);

        if (result == null)
            return AuthStatus.ERROR_OTHER;

        try {
            return AuthStatus.valueOf(result.trim());
        } catch (Exception e) {
            return AuthStatus.ERROR_OTHER;
        }
    }

    public boolean isAuthenticated() {
        return currentUser.isAuthenticated();
    }

    private String excutePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

	/*
     * private String excutePost(String url, String params) {
	 * Logger.getGlobal().info("POST: " + url + ", params " + params); return
	 * "FINE"; }
	 */

    public enum AuthStatus {
        ERROR_TIMEOUT, ERROR_BAD, ERROR_UNLOGGED, ERROR_OTHER, FINE
    }
}
