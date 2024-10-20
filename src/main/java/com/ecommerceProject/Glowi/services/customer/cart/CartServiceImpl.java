package com.ecommerceProject.Glowi.services.customer.cart;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.CartItemsDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.entity.CartItems;
import com.ecommerceProject.Glowi.entity.Order;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.entity.User;
import com.ecommerceProject.Glowi.enums.OrderStatus;
import com.ecommerceProject.Glowi.repository.CartItemsRepository;
import com.ecommerceProject.Glowi.repository.OrderRepository;
import com.ecommerceProject.Glowi.repository.ProductRepository;
import com.ecommerceProject.Glowi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> addProductToCart(AddProductInCartDto addProductInCartDto) {
        User user = userRepository.findById(addProductInCartDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(user.getId(), OrderStatus.Pending);
        if (activeOrder == null) {
            activeOrder = createNewOrder(user);
        }

        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(), activeOrder.getId(), user.getId());

        if (optionalCartItems.isPresent()) {
            CartItems existingCartItem = optionalCartItems.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemsRepository.save(existingCartItem);
            updateOrderTotal(activeOrder, Math.round(existingCartItem.getPrice()));
            return ResponseEntity.status(HttpStatus.OK).body(existingCartItem);
        }

        Product product = productRepository.findById(addProductInCartDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItems cartItem = createCartItem(activeOrder, product);
        cartItemsRepository.save(cartItem);
        long priceAsLong = Math.round(product.getPrice());

        updateOrderTotal(activeOrder, priceAsLong);
        orderRepository.save(activeOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    private Order createNewOrder(User user) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setOrderStatus(OrderStatus.Pending);
        newOrder.setTotalAmount(0L);
        newOrder.setAmount(0L);
        newOrder.setDate(new Date());
        return orderRepository.save(newOrder);
    }

    private void updateOrderTotal(Order order, long price) {
        order.setTotalAmount(order.getTotalAmount() + price);
        order.setAmount(order.getAmount() + price);
    }

    private CartItems createCartItem(Order order, Product product) {
        CartItems cartItem = new CartItems();
        cartItem.setProduct(product);
        cartItem.setPrice(product.getPrice());
        cartItem.setQuantity(1);
        cartItem.setOrder(order);


        return cartItem;
    }

    public OrderDto getCartByUserId(String userId){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        List<CartItemsDto> cartItemsDtosList = activeOrder.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());

        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());

        orderDto.setCartItems(cartItemsDtosList);

        return orderDto;


    }
}
