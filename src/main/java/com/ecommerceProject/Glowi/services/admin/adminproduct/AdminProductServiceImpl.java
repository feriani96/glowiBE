package com.ecommerceProject.Glowi.services.admin.adminproduct;

import com.ecommerceProject.Glowi.dto.ProductDto;
import com.ecommerceProject.Glowi.entity.Category;
import com.ecommerceProject.Glowi.entity.Product;
import com.ecommerceProject.Glowi.repository.CategoryRepository;
import com.ecommerceProject.Glowi.repository.ProductRepository;
import com.ecommerceProject.Glowi.services.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private static final Logger logger = Logger.getLogger(AdminProductServiceImpl.class.getName());

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public Product createproduct(ProductDto productDto) {
        String categoryId = productDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setColors(productDto.getColors());
        product.setAvailableSizes(productDto.getAvailableSizes());

        if (productDto.getImages() != null && !productDto.getImages().isEmpty()) {
            List<String> imgUrls = imageService.saveImageUrls(productDto.getImages());
            if (!imgUrls.isEmpty()) {
                product.setImgUrls(imgUrls);
            }
        }

        product.setCategoryId(categoryId);
        product.setCategoryName(category.getName());
        product = productRepository.save(product);
        product.setCategory(category);

        logger.info("Product created with ID: " + product.getId());
        return product;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new RuntimeException("No products found.");
        }

        return products.stream()
            .map(product -> {
                Category category = categoryRepository.findById(product.getCategoryId())
                    .orElse(null);

                if (category != null) {
                    product.setCategory(category);
                }
                ProductDto productDto = product.getDto();

                List<String> imgUrls = product.getImgUrls();
                if (imgUrls != null && !imgUrls.isEmpty()) {
                    List<String> fullImageUrls = imgUrls.stream()
                        .map(imgUrl -> imgUrl)
                        .collect(Collectors.toList());
                    productDto.setImageUrls(fullImageUrls);
                }

                return productDto;
            })
            .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductByName(String name) {
        List<Product> products = productRepository.findAllByNameContainingIgnoreCase(name);

        if (products.isEmpty()) {
            throw new RuntimeException("No products found.");
        }

        return products.stream()
            .map(product -> {
                Category category = categoryRepository.findById(product.getCategoryId())
                    .orElse(null);

                if (category != null) {
                    product.setCategory(category);
                }

                // Create the ProductDto
                ProductDto productDto = product.getDto();

                // Add image URLs
                List<String> imgUrls = product.getImgUrls();
                if (imgUrls != null && !imgUrls.isEmpty()) {
                    List<String> fullImageUrls = imgUrls.stream()
                        .map(imgUrl -> imgUrl)
                        .collect(Collectors.toList());
                    productDto.setImageUrls(fullImageUrls);
                }

                return productDto;
            })
            .collect(Collectors.toList());
    }

    public boolean deleteProduct(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getImgUrls() != null && !product.getImgUrls().isEmpty()) {
                for (String imgUrl : product.getImgUrls()) {
                    String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                    boolean imageDeleted = imageService.deleteImage(fileName);

                    if (imageDeleted) {
                        logger.info("Image supprimée : " + fileName);
                    } else {
                        logger.warning("Échec de la suppression de l'image : " + fileName);
                    }
                }
                product.setImgUrls(new ArrayList<>());
            }

            productRepository.deleteById(id);
            logger.info("Product successfully deleted: " + id);
            return true;
        } else {
            logger.warning("Product not found with ID: " + id);
            return false;
        }
    }

    public ProductDto getProductById(String productId){
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Category category = categoryRepository.findById(product.getCategoryId())
                .orElse(null);
            product.setCategory(category);

            return product.getDto();
        } else {
            throw new IllegalArgumentException("Product not found");
        }
    }

    public ProductDto updateProduct(String productId, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());

        if (optionalProduct.isPresent() && optionalCategory.isPresent()) {
            Product product = optionalProduct.get();
            Category category = optionalCategory.get();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setColors(productDto.getColors());
            product.setAvailableSizes(productDto.getAvailableSizes());

            product.setCategory(category);
            product.setCategoryName(category.getName());
            product.setCategoryId(category.getId());

            if (productDto.getImages() != null) {
                List<String> updatedImgUrls = imageService.updateImages(product.getImgUrls(), productDto.getImages());
                product.setImgUrls(updatedImgUrls);
            }

            return productRepository.save(product).getDto();
        } else {
            return  null;
        }
    }


}
