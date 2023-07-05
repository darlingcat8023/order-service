package com.star.test.remote;

import com.star.enterprice.orderservice.OrderServiceApplication;
import com.star.enterprise.order.remote.course.RemoteCourseService;
import com.star.enterprise.order.remote.security.RemoteSecurityService;
import com.star.enterprise.order.remote.security.request.RemoteTokenVerifyRequest;
import com.star.enterprise.order.remote.student.RemoteStudentService;
import com.star.enterprise.order.remote.student.request.RemoteStudentInfoRequest;
import com.star.enterprise.order.remote.wallet.RemoteWalletCourseService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author xiaowenrou
 * @date 2022/10/28
 */
@SpringBootTest(classes = {OrderServiceApplication.class})
public class RemoteCourseServiceTest {

    @Autowired
    private RemoteCourseService remoteCourseService;

    @Autowired
    private RemoteStudentService remoteStudentService;

    @Autowired
    private RemoteSecurityService remoteSecurityService;

    @Test
    @SneakyThrows
    public void testCourse() {
        var ret = this.remoteCourseService.getCoursePriceById("21447109-aeca-418b-8b95-f65b8b694d11", "630da044-6072-429d-bcb7-8571e67efc1b", "ad780fa6-c134-43d2-b8d2-42d78a0ffaf8 ");
        var ret1 = this.remoteCourseService.getArticlePriceById("89f14af3-ce2b-47d9-8351-25ca0f56687d", "4c9d3c21-c2da-4fb5-aa55-2874a6a0d825");
        assert ret != null && ret1 != null;
    }

    @Test
    public void testStudent() {
        var ret = this.remoteStudentService.getUserInfoByTargetId(new RemoteStudentInfoRequest("b3712306-cdd5-4b62-9a1d-04b3dee70015", "ad780fa6-c134-43d2-b8d2-42d78a0ffaf8"));
        assert ret != null;
    }

    @Test
    public void testToken() {
        var ret = this.remoteSecurityService.verifyToken(new RemoteTokenVerifyRequest("123"));
        assert ret != null;
    }

    @Autowired
    private RemoteWalletCourseService remoteWalletCourseService;

    @Test
    public void testFallBack() {
        var ret = this.remoteWalletCourseService.walletCourseDetail("123");
        System.out.println("123");
    }

}
