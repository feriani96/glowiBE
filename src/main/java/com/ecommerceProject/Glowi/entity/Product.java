package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.dto.ProductDto;
import lombok.Data;
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
    private float price;
    private int quantity;
    private List<String> colors;
    private List<String> availableSizes;
    private List<String> imgUrls;
    private String categoryId;
    private String categoryName;


    private Category category;

    public ProductDto getDto(){
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setDescription(description);
        productDto.setImgUrls(imgUrls);
        productDto.setColors(colors);
        productDto.setQuantity(quantity);
        productDto.setPrice(price);
        productDto.setAvailableSizes(availableSizes);
        productDto.setCategoryId(category.getId());
        productDto.setCategoryName(category.getName());

        return productDto;
    }
}
