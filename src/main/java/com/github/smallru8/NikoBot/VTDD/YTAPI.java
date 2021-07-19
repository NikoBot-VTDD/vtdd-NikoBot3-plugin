package com.github.smallru8.NikoBot.VTDD;

import com.github.smallru8.NikoBot.StdOutput;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class YTAPI {

	//API KEY
	private static String DEVELOPER_KEY = "";
	//OAuth ID
    private static String clientID = "";
    //OAuth KEY
    private static String clientSecret = "";
	
    private static final String APPLICATION_NAME = "NikoBot VTDD Plugin";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    
    public YTAPI(String dev_Key,String cli_ID,String cli_SEC) {
    	DEVELOPER_KEY = dev_Key;
    	clientID = cli_ID;
    	clientSecret = cli_SEC;
    }
    
    public YouTube getService(String refToken) throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        
        String access_token = getNewToken(refToken,clientID,clientSecret);
        //Access token
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(access_token);
        //httpTransport.createRequestFactory(credential);
        
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build();
    }
    
    public static String getNewToken(String refreshToken, String clientId, String clientSecret) throws IOException {
        ArrayList<String> scopes = new ArrayList<>();

        scopes.add("https://www.googleapis.com/auth/youtube.force-ssl");

        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                refreshToken, clientId, clientSecret).setScopes(scopes).setGrantType("refresh_token").execute();

        return tokenResponse.getAccessToken();
    }
    
    public boolean verify(String refToken,String videoID) {
    	try {
			YouTube youtubeService = getService(refToken);
			YouTube.CommentThreads.List request = youtubeService.commentThreads()
		            .list("id");
		        CommentThreadListResponse response = request.setKey(DEVELOPER_KEY)
		            .setVideoId(videoID)
		            .execute();
		        if(response.getKind()!=null)
		        	return true;
		        //System.out.println(response);
    	} catch (GoogleJsonResponseException e) {
    		StdOutput.warnPrintln("Verifiy failed");
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
}
