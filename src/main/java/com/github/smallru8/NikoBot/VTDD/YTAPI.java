package com.github.smallru8.NikoBot.VTDD;

import com.github.smallru8.NikoBot.Core;
import com.github.smallru8.NikoBot.StdOutput;
import com.github.smallru8.NikoBot.VTDD.commands.Reaction;
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
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Map;

public class YTAPI {

	//API KEY
	@SuppressWarnings("unused")
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
    
    /**
     * 取得會限撥放清單
     * @param channelID
     * @return
     */
    private String getMembersOnlyPlayListByChannelID(String channelID) {
    	return channelID.replaceFirst("UC", "UUMO");
    }
    
    
    private String[] getVideoList(String refToken,String channelID) {
    	try {
			YouTube youtubeService = getService(refToken);
			YouTube.PlaylistItems.List request =  youtubeService.playlistItems().list("contentDetails");
			PlaylistItemListResponse response = request
					.setMaxResults((long) 5)//回傳5筆
		            .setPlaylistId(getMembersOnlyPlayListByChannelID(channelID))
		            .execute();//throw GoogleJsonResponseException
			
			String[] videoLs = new String[response.getItems().size()];
			for(int i=0;i<response.getItems().size();i++)
				videoLs[i] = response.getItems().get(i).getContentDetails().getVideoId();

			return videoLs;
			
		}catch (GoogleJsonResponseException e) {
			if(e.getStatusMessage().startsWith("404")) {
				return null;
			}else if(e.getStatusMessage().startsWith("403")) {
				StdOutput.errorPrintln("Get video 403");
				return null;
			}else {
				e.printStackTrace();
			}

		}catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}
    	return null;
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
    
    /**
     * 驗證全部 認證到期的user
     * 到期會刪除資料 沒到期則更新TS
     */
    public void verifyAll() {
    	Map<String,String> channelMap = VTDD.vtdd.getChannelMap();//channelNickname, ChannelID
    	Map<String,ArrayList<String>> userMap = VTDD.vtdd.getExpiredUser();//channelNickname, [DiscordIDs...]
    	
    	for (Map.Entry<String, ArrayList<String>> chNameUsersMap : userMap.entrySet()) {//for channelNickname
    		boolean multiLevel = VTDD.vtdd.isChannelhaveMultilevelMembers(chNameUsersMap.getKey());//是否有多種等級會員
    		String[] videoId = null;
    		
    		for(String userID:chNameUsersMap.getValue()) {//for userID
    			String refToken = VTDD.vtdd.getRefTokenById(userID);
    			if(videoId==null && (videoId = getVideoList(refToken,channelMap.get(chNameUsersMap.getKey())))==null) {
    				//Video id 無法取得跳過處理
    				break;//身分保留
    			}
    			if(multiLevel) {//有多種等級會員
    				boolean flag = false;
    				for(int i=0;i<videoId.length;i++) {
    					if(verify(refToken,videoId[i])) {//通過
    						flag = true;
    						VTDD.vtdd.updateVerifyStatusNocheck(userID, chNameUsersMap.getKey(), true);//通過
    						break;
    					}
    				}
    				if(!flag)//沒通過
    					removeUser(userID,chNameUsersMap.getKey());
    				
    			}else {
    				if(verify(refToken,videoId[0])) {//驗證
    					VTDD.vtdd.updateVerifyStatusNocheck(userID, chNameUsersMap.getKey(), true);//通過
    				}else {//沒通過
    					removeUser(userID,chNameUsersMap.getKey());
    				}
    			}
    		}
    	}
    }
    
    /**
     * 驗證單一使用者 僅驗證 不會刪除/新增資料
     * @param userID
     * @param channelNickname
     * @return
     */
    public boolean verifyUser(String userID,String channelNickname) {
    	String refToken = VTDD.vtdd.getRefTokenById(userID);
    	String[] videoId = getVideoList(refToken,VTDD.vtdd.getChannelId(channelNickname));
    	boolean multiLevel = VTDD.vtdd.isChannelhaveMultilevelMembers(channelNickname);//是否有多種等級會員
    	if(multiLevel) {//有多種等級會員
			for(int i=0;i<videoId.length;i++) {
				if(verify(refToken,videoId[i])) {//通過
					return true;
				}
			}
		}else {
			if(verify(refToken,videoId[0])) {//驗證
				return true;
			}
		}
    	return false;
    }
    
    private boolean verify(String refToken,String videoID) {
    	try {
			YouTube youtubeService = getService(refToken);
			YouTube.CommentThreads.List request = youtubeService.commentThreads()
		            .list("id");
		        CommentThreadListResponse response = request//.setKey(DEVELOPER_KEY)
		            .setVideoId(videoID)
		            .execute();
		        if(response.getKind()!=null)
		        	return true;
		        //System.out.println(response);
    	} catch (GoogleJsonResponseException e) {//如果回傳404 是影片不存在 403才是驗證未過
    		StdOutput.warnPrintln("Verifiy failed");
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    /**
     * 沒通過刪認證跟群組
     * @param userID
     * @param channelNickname
     */
    private void removeUser(String userID,String channelNickname) {
    	Map<String,String> ServerTag = VTDD.vtdd.getServerTagByUserandNickname(userID,channelNickname);
		for (Map.Entry<String, String> entry : ServerTag.entrySet()) {//拔這個user在各個server的對應role
			Guild g = Core.botAPI.getGuildById(entry.getKey());
			Reaction.removeReaction(g.getId(), userID, channelNickname);//拔Vote reaction
			g.removeRoleFromMember(userID, g.getRoleById(entry.getValue())).queue();//拔role
			VTDD.vtdd.delMAP(g.getId(), userID, channelNickname);//remove map
		}
		//刪認證
		VTDD.vtdd.delVerifyStatus(userID, channelNickname);
    }
    
}
