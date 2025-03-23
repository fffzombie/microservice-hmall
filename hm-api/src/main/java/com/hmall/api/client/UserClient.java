package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ClassName: UserClient
 * Package: com.hmall.api.client
 * Description:
 *
 * @Author ME
 * @Create 2025/3/22 22:18
 * @Version 1.0
 */
@FeignClient("user-service")
public interface UserClient {
    @PutMapping("/users/money/deduct")
    void deductMoney(@RequestParam("pw") String pw, @RequestParam("amount") Integer amount);


}
