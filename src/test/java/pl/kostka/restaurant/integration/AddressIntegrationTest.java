package pl.kostka.restaurant.integration;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kostka.restaurant.controller.AddressController;
import pl.kostka.restaurant.controller.UserController;
import pl.kostka.restaurant.exception.ResourceNotFoundException;
import pl.kostka.restaurant.model.Address;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.repository.AddressRepository;
import pl.kostka.restaurant.repository.RoleRepository;
import pl.kostka.restaurant.repository.UserRepository;
import pl.kostka.restaurant.service.AddressService;

import java.security.Principal;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AddressIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    private AddressController addressController;

    private User testUser;

    private Principal principal;

    private Address testAddress;

    @Before
    public void init() {
        addressController = new AddressController(addressRepository, userRepository, addressService);

        testUser = new User("test99", "superTestPass", "Test", "Test", "Test", "Test");
        userRepository.save(testUser);

        testAddress = new Address("test","test","test","test","test",1d,1d, userRepository.findByUsername("test99").orElseThrow(()-> new ResourceNotFoundException("User not found")));

        principal = new Principal() {
            @Override
            public String getName() {
                return "test99";
            }
        };
    }

    @Test
    public void testGetAllAddressesByUserId_noAddressess(){
        Assert.assertEquals(0, addressController.getAdressesByUserId(principal).size());

    }

    @Test
    public void testGetAllAddressesByUserId(){
        addressRepository.save(testAddress);
        Assert.assertEquals(1, addressController.getAdressesByUserId(principal).size());
    }

    @Test
    public void testCreateAddress(){
       Address address = addressRepository.save(testAddress);

       Assert.assertNotNull(address);
       Assert.assertEquals(address, testAddress);
       Assert.assertEquals(address.getUser(), testUser);
    }


    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateAddres_notFound() {
        addressController.updateAddres(principal,99L,testAddress);
    }

    @Test
    public void testUpdateAddres() {
        Address address = addressRepository.save(testAddress);

        Address updateAddress = new Address("test2","test2","test2","test2","test2",1d,1d,address.getUser());

        Address result = addressController.updateAddres(principal,address.getId(),updateAddress);

        Assert.assertNotNull(result);
        Assert.assertSame(updateAddress.getApartmentNumber(), result.getApartmentNumber());
        Assert.assertSame(updateAddress.getBuildingNumber(), result.getBuildingNumber());
        Assert.assertSame(updateAddress.getCity(), result.getCity());
        Assert.assertSame(updateAddress.getStreet(), result.getStreet());
        Assert.assertSame(updateAddress.getTitle(), result.getTitle());
        Assert.assertSame(address.getId(), result.getId());
    }

    @Test
    public void testDeleteAddress() {
        Address address = addressRepository.save(testAddress);
        addressController.deleteAddress(principal, address.getId());

        Assert.assertTrue(!addressRepository.existsById(address.getId()));
    }

    @Test
    public void testUpdateSelectedAddress() {
        Address address = addressRepository.save(testAddress);
        Address result = addressController.updateSelectedAddress(principal, address.getId());
        User resultUser = userRepository.findByUsername(principal.getName()).orElseThrow(()-> new ResourceNotFoundException(""));

        Assert.assertEquals(resultUser.getSelectedAddress().getId(), result.getId());
    }



}
