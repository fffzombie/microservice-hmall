package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * ClassName: TradeClient
 * Package: com.hmall.api.client
 * Description:
 *
 * @Author ME
 * @Create 2025/3/22 22:23
 * @Version 1.0
 */
@FeignClient("trade-service")
public interface TradeClient {
    @PutMapping("/{orderId}")
    void markOrderPaySuccess(@PathVariable("orderId") Long orderId);
}
