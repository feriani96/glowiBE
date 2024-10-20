package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.dto.ProductDto;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "products")
@TypeAlias("Product")
@Data
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private Float price;
    private int quantity;
    private List<String> colors;
    private List<String> availableSizes;
    private List<String> imgUrls;

    private String categoryId;
    private String categoryName;

    private Category category;


    public ProductDto getDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setColors(colors);
        productDto.setQuantity(quantity);
        productDto.setPrice(price);
        productDto.setAvailableSizes(availableSizes);

        // Gérer le cas où category est null
        productDto.setCategoryId(category != null ? category.getId() : null);
        productDto.setCategoryName(category != null ? category.getName() : null);
        productDto.setImages(null);

        return productDto;
    }
}
