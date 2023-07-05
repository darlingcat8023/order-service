package com.star.test;

import com.star.enterprise.order.base.utils.Jackson;
import com.star.enterprise.order.base.utils.SnowflakeGenerator;
import com.star.enterprise.order.charge.matcher.ChargeMatchResult;
import com.star.enterprise.order.core.calculator.processor.ChargeCategoryProcessor;
import com.star.enterprise.order.http.refund.request.OrderRefundExtendRecord;
import com.star.enterprise.order.remote.callback.BusinessFeeRecord;
import com.star.enterprise.order.remote.callback.CoursePaidCallBackRequest;
import com.star.enterprise.order.remote.callback.ExpireStrategyRecord;
import com.star.enterprise.order.remote.callback.TargetRecord;
import com.star.enterprise.order.remote.coupon.request.CouponRecord;
import com.star.enterprise.order.remote.wallet.RemoteWalletCourseService;
import com.star.enterprise.wallet.api.response.WalletCourseDetailRecord;
import com.star.test.model.MonoData;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import javax.validation.Validation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StaticTest {

    @Test
    public void test() {
        var record = new CouponRecord("123", "123", 1);
        var string = Jackson.writeString(record);
        var des = Jackson.read(string, CouponRecord.class);
        System.out.println(des);
    }

    /**
     * 20221103615409192470272
     * 202211031772853564928
     */
    @Test
    public void dateTest() {
        var date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddssSSS"));
        var gen = String.valueOf(new SnowflakeGenerator().nextId());
        gen = gen.substring(gen.length() - 8);
        System.out.println(date + gen);
    }

    @Test
    public void jacksonTest() {
        var str = """
                {"walletId":"17b2e969-4bec-4e56-bc6a-2667902d63ea","businessId":"db76e48e-bb68-4ae2-95fb-94d9e93610e5","businessName":"N1-一对一课","orderItemId":"c63eb8bf-7b6a-47ac-9010-cd0d2377b216","orderId":"202302282027894005760","number":11.00,"numberLeft":9.00,"apportion":0.00,"apportionLeft":0.00,"totalLeft":9.00,"balance":450.00}
                """;
        var record = Jackson.read(str, WalletCourseDetailRecord.class);
        if (StringUtils.hasText(record.walletId()) && BigDecimal.ZERO.compareTo(record.totalLeft()) < 0) {
            System.out.println(true);
            return;
        }
        System.out.println(false);
    }


    @Test
    public void reactorTest() {
        Mono<String> f = Mono.just("111");
        Mono<String> s = Mono.defer(() -> Mono.just("222"));
        Mono<String> t = Mono.defer(() -> Mono.just("333"));
        f.map(x -> new MonoData().setFirst(x))
                .flatMap(x -> s.map(y -> {
                    x.setSecond(y);
                    return x;
                })).flatMap(x -> t.map(y -> {
                    x.setThird(y);
                    return x;
                })).subscribe(System.out::println);
    }

    @Test
    public void optional() {
        var business = new BusinessFeeRecord(new BigDecimal(100), new BigDecimal(1000), new BigDecimal(90), new BigDecimal(900));
        var expires = new ExpireStrategyRecord("time_range", 30, "2023-02-20 00:00:00:000", "2023-04-01 23:59:59:999");
        var model = new CoursePaidCallBackRequest("202302145717887996160", "a246b021-4762-4296-9e32-d34bcdf4f04c", "7e614934-03f3-4924-98f8-98f260ab8ccc", "课时包-强制划课消", new BigDecimal(10), new BigDecimal(5), business, expires, new TargetRecord("f8619a4f-722c-4229-aee3-f6a2b009c958", "e7c12eb9-851a-47da-961f-87c93fae9381"));
        System.out.println(Jackson.writeString(model));
    }


    @Test
    public void rawTest() {
        var match = new ChargeMatchResult("123", "renew");
        System.out.println(Jackson.writeString(new ChargeCategoryProcessor.MatchResultWrapper(match)));
    }

    @Test
    public void annotationTest() throws Throwable {
        var method = RemoteWalletCourseService.class.getMethod("walletCourseDetail", String.class);
        GetMapping annotation = AnnotationUtils.findAnnotation(method, GetMapping.class);
        System.out.println(123);
    }

    @Test
    public void streamTest() {
        var valid = Validation.byProvider(HibernateValidator.class).configure()
                .failFast(true).buildValidatorFactory().getValidator();
        var record = new OrderRefundExtendRecord("transfer", "123", "", "123", "", "", "", null);
        var c = valid.validate(record);
        assert c == null;
    }

    @Test
    public void encryptTest() {
        var date = LocalDateTime.now();
    }
}
