package com.ecommerceProject.Glowi.services.admin.coupon;

import com.ecommerceProject.Glowi.entity.Coupon;

import java.util.List;

public interface AdminCouponService {

    Coupon createCoupon(Coupon coupon);

    List<Coupon> getAllCoupons();
}
