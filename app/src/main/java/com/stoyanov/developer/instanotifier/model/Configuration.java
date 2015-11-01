package com.stoyanov.developer.instanotifier.model;

public class Configuration {

    public static final String CLIENT_ID = "0c77d806f6344923aee13566ad72b544";

    public static final String REDIRECT_URL = "http://nfb3806s.bget.ru/url/";

    public static final String SCOPE = "basic";

    public static final String AUTH_URL = "https://instagram.com/oauth/authorize/?client_id="
                             + CLIENT_ID + "&redirect_uri="
                             + REDIRECT_URL + "&response_type=token&scope="
                             + SCOPE;

}
