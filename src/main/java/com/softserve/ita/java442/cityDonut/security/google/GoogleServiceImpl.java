package com.softserve.ita.java442.cityDonut.security.google;

import com.softserve.ita.java442.cityDonut.constant.ErrorMessage;
import com.softserve.ita.java442.cityDonut.exception.JwtAuthenticationExeption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleServiceImpl implements SocialService<UserInfo> {

    @Value("${spring.social.google.app-id}")
    private String googleId;
    @Value("${spring.social.google.app-secret}")
    private String googleSecret;
    @Value("${server.port}")
    private int serverPort;

    @Bean
    private GoogleOauth2Template oauth2Template(){
        return new GoogleOauth2Template(new RestTemplate(),true);
    }

    private GoogleConnectionFactory createGoogleConnection(){
        return new GoogleConnectionFactory(googleId,googleSecret);
    }

    @Override
    public String getLogin() {
        OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
        oAuth2Parameters.setRedirectUri("http://localhost:"+serverPort+"/api/v1/google");
        oAuth2Parameters.setScope("email openid profile");
        return createGoogleConnection().getOAuthOperations().buildAuthenticateUrl(oAuth2Parameters);
    }

    @Override
    public String getAccessToken(String code) {
        return createGoogleConnection().getOAuthOperations().exchangeForAccess(code,"http://localhost:"+serverPort+"/api/v1/google",null).getAccessToken();
    }

    @Override
    public UserInfo getUserProfile(String accessToken) {
        UserInfo user = oauth2Template().getUserInfo(accessToken);
        if (user == null){
            throw  new JwtAuthenticationExeption(ErrorMessage.CANNOT_ENTRY_WITH_GOOGLE_SERVICES );
        }else
            return user;
    }
}
