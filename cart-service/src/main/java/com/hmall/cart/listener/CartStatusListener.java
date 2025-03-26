package com.hmall.cart.listener;

import com.hmall.cart.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: CartStatusListener
 * Package: com.hmall.cart.listener
 * Description:
 *
 * @Author ME
 * @Create 2025/3/26 12:19
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CartStatusListener {

    private final ICartService cartService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "cart.clear.queue"),
            exchange = @Exchange(name = "trade.topic"),
            key = "order.create"
    ))
    public void deleteCartItemByIds(List<Long> ids){
        cartService.removeByItemIds(ids);
    }

}
