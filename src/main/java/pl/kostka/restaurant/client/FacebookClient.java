package pl.kostka.restaurant.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.kostka.restaurant.model.FacebookUser;




@Service
public class FacebookClient extends RestTemplate {

    @Value("${security.jwt.scope-read}")
    private String scopeRead;

    @Value("${facebook-user-info-uri}")
    private String facebookUrl;

    public FacebookUser getFacebookUser(String facebookToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+ facebookToken);
        return exchange(facebookUrl, HttpMethod.GET, new HttpEntity<>(headers), FacebookUser.class).getBody();
    }
}
