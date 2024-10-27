package com.ecommerceProject.Glowi.services.customer.review;

import com.ecommerceProject.Glowi.dto.OrderedProductsResponseDto;
import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.dto.ReviewDto;
import com.ecommerceProject.Glowi.entity.*;
import com.ecommerceProject.Glowi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final CartItemsRepository cartItemsRepository;

    public OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        OrderedProductsResponseDto orderedProductsResponseDto = new OrderedProductsResponseDto();

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            System.out.println("Order ID: " + order.getId());
            System.out.println("Order Amount: " + order.getAmount());

            orderedProductsResponseDto.setOrderAmount(order.getAmount());

            // Ici, récupérez les CartItems en fonction de l'ID de l'ordre
            List<CartItems> cartItemsList = cartItemsRepository.findByOrderId(order.getId());

            List<ProductDto> productDtoList = new ArrayList<>();
            if (cartItemsList != null && !cartItemsList.isEmpty()) {
                for (CartItems cartItems : cartItemsList) {
                    if (cartItems.getProduct() != null) {
                        ProductDto productDto = new ProductDto();
                        productDto.setId(cartItems.getProduct().getId());
                        productDto.setName(cartItems.getProduct().getName());
                        productDto.setPrice(cartItems.getPrice());
                        productDto.setQuantity(cartItems.getQuantity());
                        productDto.setImageUrls(cartItems.getProduct().getImgUrls());

                        System.out.println("Product added to productDtoList: " + productDto);
                        productDtoList.add(productDto);
                    } else {
                        System.out.println("Cart item without product found, skipping...");
                    }
                }
            } else {
                System.out.println("Cart items list is empty or null for order: " + orderId);
            }

            orderedProductsResponseDto.setProductDtoList(productDtoList);
        } else {
            System.out.println("Order not found for ID: " + orderId);
        }

        return orderedProductsResponseDto;
    }


    private List<ProductDto> getProductsFromCartItems(List<CartItems> cartItemsList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (CartItems cartItems : cartItemsList) {
            if (cartItems.getProduct() != null) {
                ProductDto productDto = new ProductDto();
                productDto.setId(cartItems.getProduct().getId());
                productDto.setName(cartItems.getProduct().getName());
                productDto.setPrice(cartItems.getPrice());
                productDto.setQuantity(cartItems.getQuantity());
                productDto.setImageUrls(cartItems.getProduct().getImgUrls());

                System.out.println("Product added to productDtoList: " + productDto);
                productDtoList.add(productDto);
            } else {
                System.out.println("Cart item without product found, skipping...");
            }
        }
        return productDtoList;
    }


    public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());

        if (optionalProduct.isPresent() && optionalUser.isPresent()){
            Review review = new Review();

            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            review.setUser(optionalUser.get());
            review.setProduct(optionalProduct.get());
            review.setImg(reviewDto.getImg().getBytes());

            return reviewRepository.save(review).getDto();
        }
        return null;
    }
}
