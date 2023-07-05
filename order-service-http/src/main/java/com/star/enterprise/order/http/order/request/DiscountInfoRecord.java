package com.star.enterprise.order.http.order.request;

import com.star.enterprise.order.core.calculator.CouponDetail;
import com.star.enterprise.order.core.calculator.DiscountDetail;
import com.star.enterprise.order.core.calculator.WalletDiscountDetail;
import com.star.enterprise.order.core.model.impl.DefaultWalletDiscountDetail;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaowenrou
 * @date 2022/9/28
 */
public record DiscountInfoRecord(

        @NotNull(message = "use direct discount not valid")
        BigDecimal useDirectDiscount,

        @NotNull(message = "useCoupons not valid")
        List<CouponRecord> useCoupons,

        @NotNull(message = "use wallet discount not valid")
        BigDecimal useWallet,

        DefaultWalletDiscountDetail walletInfo

) implements DiscountDetail {

        public DiscountInfoRecord(BigDecimal useDirectDiscount, List<CouponRecord> useCoupons, BigDecimal useWallet, DefaultWalletDiscountDetail walletInfo) {
                this.useDirectDiscount = useDirectDiscount;
                this.useCoupons = useCoupons;
                this.useWallet = Objects.requireNonNullElseGet(useWallet, () -> new BigDecimal(0));
                this.walletInfo = new DefaultWalletDiscountDetail(this.useWallet);
        }

        @Override
        public List<CouponDetail> couponDiscount() {
                return new ArrayList<>(this.useCoupons);
        }

        @Override
        public WalletDiscountDetail walletDiscount() {
                return this.walletInfo;
        }

}
