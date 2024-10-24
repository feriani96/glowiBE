package com.ecommerceProject.Glowi.services.admin.faq;

import com.ecommerceProject.Glowi.dto.FAQDto;

public interface FAQService {

    FAQDto postFAQ(String productId, FAQDto faqDto);
}
