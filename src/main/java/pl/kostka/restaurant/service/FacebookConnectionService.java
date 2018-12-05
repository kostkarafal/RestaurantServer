package pl.kostka.restaurant.service;

import org.bouncycastle.crypto.prng.RandomGenerator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;
import pl.kostka.restaurant.client.FacebookClient;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.FacebookUser;
import pl.kostka.restaurant.model.Role;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.FacebookUserRepository;
import pl.kostka.restaurant.repository.RoleRepository;
import pl.kostka.restaurant.repository.UserRepository;

import java.io.Serializable;
import java.util.*;

@Service
public class FacebookConnectionService {

    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.scope-read}")
    private String scopeRead;

    @Value("${security.jwt.scope-write}")
    private String scopeWrite = "write";

    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FacebookUserRepository facebookUserRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;

    @Autowired
    private FacebookClient facebookClient;


    public OAuth2AccessToken facebookLogin(String facebookToken){

        FacebookUser fbUser = facebookClient.getFacebookUser(facebookToken);
        Optional<User> user = Optional.empty();
        Optional<FacebookUser> facebookUser = facebookUserRepository.findById(fbUser.getId());

        if(facebookUser.isPresent()) {
            user = userRepository.findById(facebookUser.get().getUser().getId());
        }
        if(user.isPresent()){
            return generateOAuth2AccessToken(user.get());
        } else {
            User newUser = new User();
                    newUser.setUsername(fbUser.getEmail());
                    newUser.setPassword(BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt()));
                    newUser.setName(fbUser.getFirst_name());
                    newUser.setSurname(fbUser.getLast_name());
                    newUser.setEmail(fbUser.getEmail());
                    newUser.setRoles(Collections.singletonList((roleRepository.findByRoleName("USER").orElseThrow(()-> new ResourceNotFoundException("User role not found")))));
            newUser = userRepository.save(newUser);
            fbUser.setUser(newUser);
            facebookUserRepository.save(fbUser);

            return generateOAuth2AccessToken(newUser);
        }
    }


    private OAuth2AccessToken generateOAuth2AccessToken(User user) {
        List<String> scopes = Arrays.asList(scopeRead, scopeWrite);
        Map<String, String> requestParameters = new HashMap<>();
        Map<String, Serializable> extensionProperties = new HashMap<>();

        boolean approved = true;
        Set<String> responseTypes = new HashSet<>();
        responseTypes.add("code");

        // Authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role: user.getRoles())
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

        OAuth2Request oauth2Request = new OAuth2Request(requestParameters, clientId, authorities, approved,
                new HashSet<>(scopes), new HashSet<>(Arrays.asList(resourceIds)), null, responseTypes, extensionProperties);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), "N/A", authorities);

        OAuth2Authentication auth = new OAuth2Authentication(oauth2Request, authenticationToken);

        AuthorizationServerTokenServices tokenService = configuration.getEndpointsConfigurer().getTokenServices();

        OAuth2AccessToken token = tokenService.createAccessToken(auth);

        return token;
    }

}
