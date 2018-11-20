package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Address;
import pl.kostka.restaurant.repository.AddressRepository;
import pl.kostka.restaurant.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
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

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/addresses")
    public List<Address> getAllAdressesByUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user -> addressRepository.findByUserId(user.getId()))
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("addresses")
    public Address createAddress(Principal principal,
                                 @Valid @RequestBody Address address) {
        return userRepository.findByUsername(principal.getName()).map(user -> {
            address.setUser(user);
            return addressRepository.save(address);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/addresses/{addressId}")
    public Address updateAddres(Principal principal,
                                 @PathVariable (value = "addressId") Long addressId,
                                 @Valid @RequestBody Address addressRequest) {
        if(!userRepository.existsByUsername(principal.getName())) {
            throw new ResourceNotFoundException("User " + principal.getName() + " not found");
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

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(Principal principal,
                                           @PathVariable (value = "addressId") Long addressId) {
        if(!userRepository.existsByUsername(principal.getName())) {
            throw new ResourceNotFoundException("UserId " + principal.getName() + " not found");
        }

        return addressRepository.findById(addressId).map(address -> {
            addressRepository.delete(address);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("AddressId " + addressId + " not found"));
    }
}
