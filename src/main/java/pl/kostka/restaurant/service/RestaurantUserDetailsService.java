package pl.kostka.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Component
public class RestaurantUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       return userRepository.findByUsername(s).map(user -> {
            List<GrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            });

            return new org.springframework.security.core.userdetails.
                    User(user.getUsername(), user.getPassword(), authorities);

        }).orElseThrow(()-> new UsernameNotFoundException(String.format("The username %s doesn't exist", s)));
    }

}