package pl.kostka.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Address;
import pl.kostka.restaurant.model.Restaurant;
import pl.kostka.restaurant.repository.*;

import java.util.List;

@Service
public class AddressService {

    private UserRepository userRepository;

    private RestaurantRepository restaurantRepository;

    private AddressRepository addressRepository;

    @Autowired
    public AddressService(UserRepository userRepository, RestaurantRepository restaurantRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.addressRepository = addressRepository;
    }


    public Address addNewAddress(String username, Address address) {
        return userRepository.findByUsername(username).map(user -> {
            address.setUser(user);
            address.setAvailable(checkIfAddressIsInRangeOfAnyRestaurant(address.getLatitude(), address.getLongitude()));
            return addressRepository.save(address);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    private boolean checkIfAddressIsInRangeOfAnyRestaurant(Double latitude, Double longitude) {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        for(Restaurant restaurant: restaurants) {
            if(getDistanceBetweenToPointsInKm(restaurant.getLatitude(),restaurant.getLongitude(), latitude, longitude) < 15){
                return true;
            }
        }
        return false;
    }

    private double getDistanceBetweenToPointsInKm(Double lat1, Double lng1, Double lat2, Double lng2) {
        //Haversine formula
        double R = 6371D;  //Earth radious
        double hav = (1 - Math.cos(DegreeToRadians(lat2 - lat1))) / 2 + Math.cos(DegreeToRadians(lat1)) * Math.cos(DegreeToRadians(lat2)) *
                ((1 - Math.cos(DegreeToRadians(lng2 - lng1))) / 2);

        return 2 * R * Math.asin(Math.sqrt(hav));
    }

    private Double DegreeToRadians(Double degree){
        return degree * (Math.PI/180);
    }
}
