package pl.kostka.restaurant.unit.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import pl.kostka.restaurant.exception.ProductIncompatibilityException;
import pl.kostka.restaurant.model.Order;
import pl.kostka.restaurant.model.Product;
import pl.kostka.restaurant.model.Restaurant;
import pl.kostka.restaurant.model.User;
import pl.kostka.restaurant.model.dto.Basket;
import pl.kostka.restaurant.model.enums.OrderStatus;
import pl.kostka.restaurant.model.enums.ProductType;
import pl.kostka.restaurant.repository.OrderRepository;
import pl.kostka.restaurant.repository.ProductRepository;
import pl.kostka.restaurant.repository.RestaurantRepository;
import pl.kostka.restaurant.service.OrderService;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class OrderServiceTest {


    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private RestaurantRepository restaurantRepository;

    @MockBean
    private ProductRepository productRepository;

    private OrderService orderService;

    @Mock
    private Restaurant mockRestaurant;

    @Mock
    private User mockUser;

    private Product product;

    @Before
    public void init(){
        orderService = new OrderService(orderRepository,restaurantRepository, productRepository);
        product = new Product("Dobry kebs", "bardzo dobry kebs", ProductType.KEBAB, 15f, 1L );
        product.setId(1L);
    }

    @Test
    public void testMakeOrder() throws Exception{

        Optional<Product> optional = Optional.of(product);
        Basket basket = new Basket(45f, Arrays.asList(2,1), Arrays.asList(product, product), 1L );

        Optional<Restaurant> optionalRestaurant = Optional.of(mockRestaurant);
        Order resultOrder = new Order(OrderStatus.CONFIRMED, 45f, mockRestaurant, mockUser, Arrays.asList(product, product, product));

        when(productRepository.findById(Mockito.anyLong())).thenReturn(optional);
        when(restaurantRepository.findById(Mockito.anyLong())).thenReturn(optionalRestaurant);

        Order order = orderService.makeOrder(basket, mockUser);

       assertNotNull(order);
       assertEquals(resultOrder.getTotalPrice(), order.getTotalPrice());
       assertEquals(resultOrder.getProducts().size(), resultOrder.getProducts().size());
       assertEquals(resultOrder.getStatus(), resultOrder.getStatus());
    }

    @Test(expected = ProductIncompatibilityException.class)
    public void testMakeOrder_NotFoundInDatabase() throws Exception{

        Basket basket = new Basket(45f, Arrays.asList(2,1), Arrays.asList(product, product), 1L );

        Optional<Restaurant> optionalRestaurant = Optional.of(mockRestaurant);

        when(productRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(restaurantRepository.findById(Mockito.anyLong())).thenReturn(optionalRestaurant);

        orderService.makeOrder(basket, mockUser);
    }

    @Test(expected = ProductIncompatibilityException.class)
    public void testMakeOrder_IncompatiblyProduct() throws Exception{

        Optional<Product> optional = Optional.of(product);
        Optional<Restaurant> optionalRestaurant = Optional.of(mockRestaurant);
        Basket basket = new Basket(25f, Arrays.asList(2,1), Arrays.asList(product, product), 1L );

        when(productRepository.findById(Mockito.anyLong())).thenReturn(optional);
        when(restaurantRepository.findById(Mockito.anyLong())).thenReturn(optionalRestaurant);

        orderService.makeOrder(basket, mockUser);
    }

}