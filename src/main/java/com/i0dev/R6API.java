package com.i0dev;

import com.i0dev.object.PlayStationUser;
import com.i0dev.object.UplayUser;
import com.i0dev.object.User;
import com.i0dev.object.XboxUser;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;


@Getter
@Setter
public class R6API {

    private static final String UBI_APPID = "39baebad-39e5-4552-8c25-2c9b919064e2";

    String ticket;
    String clientIp, profileID, userID, name, environment, spaceID, clientIpCountry, sessionID, sessionKey, rememberMeTicket;
    Date expiration, serverTime;
    PlatformType platform;


    private R6API(String ticket) {
        this.ticket = ticket;
    }

    @SneakyThrows
    public static R6API connect(String username, String password) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost("https://public-ubiservices.ubi.com/v3/profiles/sessions");
        StringEntity params = new StringEntity("{\"rememberMe\": true}");
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Ubi-AppId", UBI_APPID);
        request.addHeader("Authorization", "Basic " + convertToBase64(username, password));
        request.addHeader("Accept", "application/json");
        request.setEntity(params);
        HttpResponse response = httpClient.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        String str;
        while ((str = rd.readLine()) != null) {
            builder.append(str);
        }
        JSONObject result = ((JSONObject) new JSONParser().parse(builder.toString()));
        R6API api = new R6API(result.get("ticket").toString());
        api.setPlatform(getType(result));
        api.setProfileID(result.get("profileId").toString());
        api.setUserID(result.get("userId").toString());
        api.setName(result.get("nameOnPlatform").toString());
        api.setEnvironment(result.get("environment").toString());
        // api.setExpiration();
        api.setSpaceID(result.get("spaceId").toString());
        api.setClientIpCountry(result.get("clientIpCountry").toString());
        api.setClientIp(result.get("clientIp").toString());
        //  api.setServerTime();
        api.setSessionID(result.get("sessionId").toString());
        api.setSessionKey(result.get("sessionKey").toString());
        api.setRememberMeTicket(result.get("rememberMeTicket").toString());
        return api;
    }

    private static String convertToBase64(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    public enum PlatformType {
        U_PLAY("uplay"), XBOX("xbl"), PLAY_STATION("psn");

        @Getter
        String id;

        PlatformType(String id) {
            this.id = id;
        }
    }

    private static PlatformType getType(JSONObject object) {
        JSONObject result = object;
        switch (result.get("platformType").toString()) {
            case "uplay":
                return PlatformType.U_PLAY;
            case "psn":
                return PlatformType.PLAY_STATION;
            case "xbl":
                return PlatformType.XBOX;
            default:
                return null;
        }
    }

    // public methods

    @SneakyThrows
    public User getUser(String name, PlatformType platform) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://public-ubiservices.ubi.com/v3/profiles?nameOnPlatform=" + name + "&platformType=" + platform.getId());
        request.addHeader("Content-Type", "application/json");
        request.addHeader("ubi-appid", UBI_APPID);
        request.addHeader("authorization", "Ubi_v1 t=" + this.ticket);
        request.addHeader("Accept", "application/json");
        HttpResponse response = httpClient.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder builder = new StringBuilder();
        String str;
        while ((str = rd.readLine()) != null) {
            builder.append(str);
        }
        JSONObject result = ((JSONObject) ((JSONArray) ((JSONObject) new JSONParser().parse(builder.toString())).get("profiles")).get(0));
        PlatformType type = getType(result);

        User user = new User();
        switch (type) {
            case U_PLAY: {
                user = new UplayUser(result.get("idOnPlatform").toString());
                break;
            }
            case XBOX: {
                user = new XboxUser(result.get("idOnPlatform").toString());
                break;
            }
            case PLAY_STATION: {
                user = new PlayStationUser(result.get("idOnPlatform").toString());
                break;
            }
        }
        user.setUserID(result.get("userId").toString());
        user.setProfileID(result.get("profileId").toString());
        user.setName(result.get("nameOnPlatform").toString());
        return user;
    }


}
