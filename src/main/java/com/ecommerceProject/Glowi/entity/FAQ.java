package com.ecommerceProject.Glowi.entity;

import com.ecommerceProject.Glowi.dto.FAQDto;
import lombok.Data;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "faq")
@TypeAlias("Faq")
@Data
public class FAQ {

    private String id;

    private String question;

    private String answer;

    @DBRef
    private Product product;

    public FAQDto getFAQDto(){
        FAQDto faqDto = new FAQDto();

        faqDto.setId(id);
        faqDto.setQuestion(question);
        faqDto.setAnswer(answer);
        faqDto.setProductId(product.getId());

        return faqDto;
    }
}
