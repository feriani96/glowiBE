package com.ecommerceProject.Glowi.services.customer.cart;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.CartItemsDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.entity.*;
import com.ecommerceProject.Glowi.enums.OrderStatus;
import com.ecommerceProject.Glowi.exceptions.ValidationException;
import com.ecommerceProject.Glowi.repository.*;
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

    @Autowired
    private CouponRepository couponRepository;

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

    @Override
    public OrderDto getCartByUserId(String userId) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);

        if (activeOrder == null) {
            throw new RuntimeException("No active order found for user");
        }

        List<CartItems> cartItems = cartItemsRepository.findByOrderId(activeOrder.getId());

        List<CartItemsDto> cartItemsDtosList = cartItems.stream()
            .map(cartItem -> {
                CartItemsDto dto = cartItem.getCartDto();
                if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
                    // Assurez-vous de récupérer uniquement la première image
                    dto.setMainImageUrl(dto.getImageUrls().get(0));
                }
                return dto;
            })
            .collect(Collectors.toList());

        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(activeOrder.getAmount());
        orderDto.setId(activeOrder.getId());
        orderDto.setOrderStatus(activeOrder.getOrderStatus());
        orderDto.setDiscount(activeOrder.getDiscount());
        orderDto.setTotalAmount(activeOrder.getTotalAmount());
        orderDto.setCartItems(cartItemsDtosList);
        if(activeOrder.getCoupon() != null){
            orderDto.setCouponName(activeOrder.getCoupon().getName());
        }

        return orderDto;
    }

    public OrderDto applyCoupon(String userId, String code){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Coupon Not Found."));
        if(couponIsExpired(coupon)){
            throw new ValidationException("Coupon Has Expired.");
        }

        double discountAmount = ((coupon.getDiscount() / 100.0) * activeOrder.getTotalAmount());
        double netAmount = activeOrder.getTotalAmount() - discountAmount;

        activeOrder.setAmount((long) netAmount);
        activeOrder.setDiscount((long) discountAmount);
        activeOrder.setCoupon(coupon);

        orderRepository.save(activeOrder);
        return activeOrder.getOrderDto();
    }

    private boolean couponIsExpired(Coupon coupon){
        Date currentdate = new Date();
        Date expirationDate = coupon.getExpirationDate();

        return expirationDate != null && currentdate.after(expirationDate);
    }

    public OrderDto increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

        Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());

        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId()
        );

        if (optionalProduct.isPresent() && optionalCartItems.isPresent()) {
            CartItems cartItem = optionalCartItems.get();
            Product product = optionalProduct.get();

            long priceAsLong = Math.round(product.getPrice());


            activeOrder.setAmount(activeOrder.getAmount() + priceAsLong);
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + priceAsLong);

            cartItem.setQuantity(cartItem.getQuantity() + 1);

            if (activeOrder.getCoupon() != null) {
                double discountAmount = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);
            }

            cartItemsRepository.save(cartItem);
            orderRepository.save(activeOrder);

            return activeOrder.getOrderDto();
        }

        return null;
    }


}
