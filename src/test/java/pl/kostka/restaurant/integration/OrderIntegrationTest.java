package pl.kostka.restaurant.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.kostka.restaurant.controller.OrderController;
import pl.kostka.restaurant.controller.ProductController;
import pl.kostka.restaurant.controller.UserController;
import pl.kostka.restaurant.exception.ProductIncompatibilityException;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.Restaurant;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.model.enums.ProductType;
import pl.kostka.restaurant.repository.*;
import pl.kostka.restaurant.service.OrderService;

import java.security.Principal;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ProductRepository productRepository;

    private OrderController orderController;

    private Order testOrder;

    private Product product;
    private Product product2;
    private Product product3;
    private Product product4;

    private Basket testBasket;

    private User testUser;
    private Restaurant restaurant;

    private Principal principal;

    @Before
    public void init() {
        orderController = new OrderController(orderRepository, userRepository, new OrderService(orderRepository, restaurantRepository, productRepository));

        product = new Product("Dobry kebs", "bardzo dobry kebs", ProductType.KEBAB, 15f, 1L );
        product2 = new Product("Pizza", "bardzo dobry kebs", ProductType.PIZZA, 25.5f, 1L );
        product3 = new Product("Tortilla", "bardzo dobry kebs", ProductType.WRAP, 15.7f, 1L );
        productRepository.saveAll(Arrays.asList(product,product2,product3));

        restaurant = new Restaurant("test","test","test","test","test","test");
        restaurantRepository.save(restaurant);

        testBasket = new Basket(281.7f, Arrays.asList(4,5,6), productRepository.findAll(), restaurantRepository.findAll().get(0).getId());

        testUser = new User("test99", "superTestPass", "Test", "Test", "Test", "Test");
        userRepository.save(testUser);

        principal = new Principal() {
            @Override
            public String getName() {
                return "test99";
            }
        };
        product4 = new Product("Tortilla not working", "bardzo dobry kebs", ProductType.WRAP, 15.7f, 1L );
        product4.setId(99L);
    }

    @Test
    public void testMakeOrder(){
        Order order = orderController.makeOrder(testBasket, principal);

        Assert.assertNotNull(order);
        Assert.assertEquals(testBasket.getTotalPrize(), order.getTotalPrice(), 0.001f);
        Assert.assertEquals(order.getProducts().size(), 15);
        Assert.assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test(expected = ProductIncompatibilityException.class)
    public void testMakeOrder_NotFoundInDatabase(){
        Basket changedBasket = new Basket(250.7f, Arrays.asList(4,5,6), Arrays.asList(product4, product4, product4), 4L);
        orderController.makeOrder(changedBasket, principal);
    }


    @Test(expected = ProductIncompatibilityException.class)
    public void testMakeOrder_IncompatiblyProduct(){
        Product changedProduct = new Product(product);
        changedProduct.setPrice(22f);

        Basket changedBasket = new Basket(250.7f, Arrays.asList(4,5,6), Arrays.asList(product, product2, product3), 4L);

        orderController.makeOrder(changedBasket, principal);
    }

    @Test
    public void testGetAllOrdersByUserId(){
        Assert.assertEquals(0, orderController.getAllOrdersByUserId(principal).size());

        orderController.makeOrder(testBasket,principal);
        Assert.assertEquals(1, orderController.getAllOrdersByUserId(principal).size());
    }

    @Test
    public void testGetAllOrdersByRestaurantId(){
        Assert.assertEquals(0, orderController.getAllOrdersByRestaurantId(restaurantRepository.findAll().get(0).getId()).size());

        orderController.makeOrder(testBasket,principal);
        Assert.assertEquals(1, orderController.getAllOrdersByRestaurantId(restaurantRepository.findAll().get(0).getId()).size());
    }

}
