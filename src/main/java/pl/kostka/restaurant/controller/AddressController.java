package pl.kostka.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Address;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.AddressRepository;
import pl.kostka.restaurant.repository.UserRepository;
import pl.kostka.restaurant.service.AddressService;

import javax.validation.Valid;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

@RestController
public class AddressController {

    private AddressRepository addressRepository;

    private UserRepository userRepository;

    private AddressService addressService;

    @Autowired
    public AddressController(AddressRepository addressRepository, UserRepository userRepository, AddressService addressService) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/addresses")
    public List<Address> getAdressesByUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user -> addressRepository.findByUserIdAndIsAvailable(user.getId(), true))
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/addresses/unsupported")
    public List<Address> getUnsupportedAdressesByUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName()).map(user -> addressRepository.findByUserIdAndIsAvailable(user.getId(), false))
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("addresses")
    public Address createAddress(Principal principal,
                                 @Valid @RequestBody Address address) {
        return addressService.addNewAddress(principal.getName(), address);
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
            address.setLatitude(addressRequest.getLatitude());
            address.setLongitude(addressRequest.getLongitude());
            return addressRepository.save(address);
        }).orElseThrow(() -> new ResourceNotFoundException("AddressId " + addressId + "not found"));
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/addresses/selected")
    public Address getSelectedAddress(Principal principal) {

        User user = userRepository.findByUsername(principal.getName()).orElseThrow(()-> new ResourceNotFoundException("User " + principal.getName() + " not found"));
        if(user.getSelectedAddress() != null) {
            return user.getSelectedAddress();
        } else {
            throw new ResourceNotFoundException("User has no seelected address");
        }
    }


    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/addresses/{addressId}/select")
    public Address updateSelectedAddress(Principal principal,
                                              @PathVariable (value = "addressId") Long addressId) {

        return userRepository.findByUsername(principal.getName()).map(user ->
             addressRepository.findByUserIdAndId(user.getId(),addressId).map(address -> {
                user.setSelectedAddress(address);
                userRepository.save(user);
                return address;
            }).orElseThrow(() -> new ResourceNotFoundException("User "+principal.getName() + "do not have address with id " + addressId ))
       ).orElseThrow(()-> new ResourceNotFoundException("User " + principal.getName() + " not found"));
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(Principal principal,
                                           @PathVariable (value = "addressId") Long addressId) {

        return userRepository.findByUsername(principal.getName()).map(user -> {
            if(user.getSelectedAddress().getId().equals(addressId)){
                user.setSelectedAddress(null);
                userRepository.save(user);
            }
            return addressRepository.findById(addressId).map(address -> {
                addressRepository.delete(address);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new ResourceNotFoundException("AddressId " + addressId + " not found"));

        }).orElseThrow(()-> new ResourceNotFoundException("User" + principal.getName() + "not found"));
    }
}
