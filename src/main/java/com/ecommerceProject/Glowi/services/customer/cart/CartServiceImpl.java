package com.ecommerceProject.Glowi.services.customer.cart;

import com.ecommerceProject.Glowi.dto.AddProductInCartDto;
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

import java.util.Optional;

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
        // Chercher la commande active (Pending) pour l'utilisateur
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(addProductInCartDto.getUserId(), OrderStatus.Pending);

        // Chercher si le produit est déjà dans le panier
        Optional<CartItems> optionalCartItems = cartItemsRepository.findByProductIdAndOrderIdAndUserId(
            addProductInCartDto.getProductId(), activeOrder.getId(), addProductInCartDto.getUserId());

        // Si le produit est déjà dans le panier, retourner un conflit
        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> optionalProduct = productRepository.findById(addProductInCartDto.getProductId());
            Optional<User> optionalUser = userRepository.findById(addProductInCartDto.getUserId());

            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                CartItems cart = new CartItems();
                cart.setProduct(optionalProduct.get());
                cart.setPrice(optionalProduct.get().getPrice());
                cart.setQuantity(1);
                cart.setUser(optionalUser.get());
                cart.setOrder(activeOrder);

                CartItems updatedCart = cartItemsRepository.save(cart);

                // Convertir le prix de float en long avant de l'ajouter à totalAmount et amount
                long priceAsLong = (long) cart.getPrice(); // Conversion explicite de float en long

                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + priceAsLong);
                activeOrder.setAmount(activeOrder.getAmount() + priceAsLong);
                activeOrder.getCartItems().add(cart);

                orderRepository.save(activeOrder);

                return ResponseEntity.status(HttpStatus.CREATED).body(cart);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product not found");
            }
        }
    }
}
