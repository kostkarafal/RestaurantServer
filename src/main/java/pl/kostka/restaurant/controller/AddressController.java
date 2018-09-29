package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Address;
import pl.kostka.restaurant.repository.AddressRepository;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AddressController {

    private AddressRepository addressRepository;

    private UserRepository userRepository;

    @Autowired
    public AddressController(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{userId}/addresses")
    public List<Address> getAllCommentsByUserId(@PathVariable(value = "userId") Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @PostMapping("/users/{userId}/addresses")
    public Address createComment(@PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody Address address) {
        return userRepository.findById(userId).map(user -> {
            address.setUser(user);
            return addressRepository.save(address);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + userId + " not found"));
    }

    @PutMapping("/users/{userId}/addresses/{addressId}")
    public Address updateComment(@PathVariable (value = "userId") Long userId,
                                 @PathVariable (value = "addressId") Long addressId,
                                 @Valid @RequestBody Address addressRequest) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return addressRepository.findById(addressId).map(address -> {
            address.setTitle(addressRequest.getTitle());
            address.setCity(addressRequest.getCity());
            address.setStreet(addressRequest.getStreet());
            address.setBuildingNumber(addressRequest.getBuildingNumber());
            address.setApartmentNumber(addressRequest.getApartmentNumber());
            return addressRepository.save(address);
        }).orElseThrow(() -> new ResourceNotFoundException("AddressId " + addressId + "not found"));
    }

    @DeleteMapping("/users/{userId}/addresses/{addressId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "userId") Long userId,
                                           @PathVariable (value = "addressId") Long addressId) {
        if(!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return addressRepository.findById(addressId).map(address -> {
            addressRepository.delete(address);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("AddressId " + addressId + " not found"));
    }
}
