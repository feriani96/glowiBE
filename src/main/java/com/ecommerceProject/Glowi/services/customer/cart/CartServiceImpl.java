package com.ecommerceProject.Glowi.services.customer.cart;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
import com.ecommerceProject.Glowi.dto.CartItemsDto;
import com.ecommerceProject.Glowi.dto.OrderDto;
import com.ecommerceProject.Glowi.dto.PlaceOrderDto;
import com.ecommerceProject.Glowi.entity.*;
import com.ecommerceProject.Glowi.enums.OrderStatus;
import com.ecommerceProject.Glowi.exceptions.ValidationException;
import com.ecommerceProject.Glowi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

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

        Product product = productRepository.findById(addProductInCartDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getPrice() < 0) {
            throw new ValidationException("Product price cannot be negative");
        }

        CartItems existingCartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(), activeOrder.getId(), user.getId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemsRepository.save(existingCartItem);
            updateOrderTotal(activeOrder, Math.round(existingCartItem.getPrice()));
            return ResponseEntity.status(HttpStatus.OK).body(existingCartItem);
        }

        CartItems cartItem = createCartItem(activeOrder, product, user);
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
        if (order.getTotalAmount() < 0) {
            order.setTotalAmount(0L);
        }
    }

    private CartItems createCartItem(Order order, Product product, User user) {
        CartItems cartItem = new CartItems();
        cartItem.setProduct(product);
        cartItem.setUser(user);
        cartItem.setPrice(product.getPrice());
        cartItem.setQuantity(1);
        cartItem.setOrder(order);
        return cartItem;
    }

    public ResponseEntity<?> deleteProductsFromCart(String userId, String productId) {
        if (productId.startsWith("[") && productId.endsWith("]")) {
            productId = productId.substring(1, productId.length() - 1).replace("\"", "");
        }

        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.Pending);
        if (activeOrder == null) {
            logger.warn("No active order found for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active order found.");
        }

        CartItems cartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(productId, activeOrder.getId(), userId);
        if (cartItem != null) {
            long priceAsLong = Math.round(cartItem.getPrice());

            cartItemsRepository.delete(cartItem);

            long newTotalAmount = Math.max(0, activeOrder.getTotalAmount() - (priceAsLong * cartItem.getQuantity()));
            activeOrder.setTotalAmount(newTotalAmount);
            activeOrder.setAmount(newTotalAmount);

            orderRepository.save(activeOrder);

            logger.info("Product with id: {} removed from cart for userId: {}", productId, userId);
            return ResponseEntity.ok("Product removed from cart successfully.");
        } else {
            logger.warn("No CartItem found for productId: {} in orderId: {}", productId, activeOrder.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }
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

        if (activeOrder.getTotalAmount() < 0) {
            activeOrder.setTotalAmount(0L);
        }

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

        activeOrder.setAmount(Math.max(0, (long) netAmount));
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
        logger.info("increaseProductQuantity called for userId: {} and productId: {}", addProductInCartDto.getUserId(), addProductInCartDto.getProductId());

        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) {
            logger.warn("No active order found for userId: {}", addProductInCartDto.getUserId());
            throw new RuntimeException("No active order found");
        }

        Product product = productRepository.findById(addProductInCartDto.getProductId())
            .orElseThrow(() -> {
                logger.error("Product not found for productId: {}", addProductInCartDto.getProductId());
                return new RuntimeException("Product not found");
            });

        if (product.getPrice() < 0) {
            throw new ValidationException("Product price cannot be negative");
        }

        CartItems cartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (cartItem != null) {
            long priceAsLong = Math.round(product.getPrice());
            logger.info("CartItem found for productId: {}. Current quantity: {}", addProductInCartDto.getProductId(), cartItem.getQuantity());

            activeOrder.setAmount(activeOrder.getAmount() + priceAsLong);
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() + priceAsLong);
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            logger.info("New Quantity after increment: {}", cartItem.getQuantity());

            if (activeOrder.getCoupon() != null) {
                double discountAmount = (activeOrder.getCoupon().getDiscount() / 100.0) * activeOrder.getTotalAmount();
                double netAmount = activeOrder.getTotalAmount() - discountAmount;

                activeOrder.setAmount((long) netAmount);
                activeOrder.setDiscount((long) discountAmount);
                logger.info("Coupon applied. Discount: {}, New Amount: {}", discountAmount, netAmount);
            }

            cartItemsRepository.save(cartItem);
            orderRepository.save(activeOrder);

            return activeOrder.getOrderDto();
        } else {
            logger.warn("No CartItem found for productId: {} in orderId: {}", addProductInCartDto.getProductId(), activeOrder.getId());
        }

        return null;
    }

    public OrderDto decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

        Product product = productRepository.findById(addProductInCartDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItems cartItem = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        if (cartItem != null) {
            long priceAsLong = Math.round(product.getPrice());

            activeOrder.setAmount(activeOrder.getAmount() - priceAsLong);
            activeOrder.setTotalAmount(activeOrder.getTotalAmount() - priceAsLong);
            cartItem.setQuantity(cartItem.getQuantity() - 1);

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

    public OrderDto placeOrder(PlaceOrderDto placeOrderDto){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
        if (activeOrder == null) {
            System.out.println("No pending order found for user: " + placeOrderDto.getUserId());
            return null;
        }


        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());

        if (optionalUser.isPresent()){
            System.out.println("Order Description: " + placeOrderDto.getOrderDescription());

            activeOrder.setOrderDescription(placeOrderDto.getOrderDescription());
            activeOrder.setAddress(placeOrderDto.getAddress());
            activeOrder.setDate(new Date());
            activeOrder.setOrderStatus(OrderStatus.Placed);
            activeOrder.setTrackingId(UUID.randomUUID());

            orderRepository.save(activeOrder);

            Order order = new Order();
            order.setAmount(0L);
            order.setTotalAmount(0L);
            order.setDiscount(0L);
            order.setUser(optionalUser.get());
            order.setOrderStatus(OrderStatus.Pending);
            orderRepository.save(order);

            return activeOrder.getOrderDto();

        }
        return null;
    }

    public List<OrderDto> getMyPlacedOrders(String userId){
        return orderRepository.findByUserIdAndOrderStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    public OrderDto searchOrderByTrackingId(UUID trackingId){
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);

        if (optionalOrder.isPresent()){
            return optionalOrder.get().getOrderDto();
        }
        return null;
    }

}
