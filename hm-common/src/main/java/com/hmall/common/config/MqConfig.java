package com.hmall.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.hmall.common.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;

/**
 * ClassName: MqConfig
 * Package: com.hmall.common.config
 * Description:
 *
 * @Author ME
 * @Create 2025/3/26 11:35
 * @Version 1.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class MqConfig {
    @Bean
    public MessageConverter messageConverter(){
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(){
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                Long userId = message.getMessageProperties().getHeader("user-info");
                if(ObjectUtil.isNotEmpty(userId)){
                    UserContext.setUser(userId);
                }
                return super.fromMessage(message);
            }

            @Override
            protected Message createMessage(Object objectToConvert, MessageProperties messageProperties, Type genericType) throws MessageConversionException {
                Long userId = UserContext.getUser();
                if(ObjectUtil.isNotEmpty(userId)){
                    messageProperties.setHeader("user-info",userId);
                }
                return super.createMessage(objectToConvert, messageProperties, genericType);
            }
        };
        converter.setCreateMessageIds(true);
        return converter;
    }


}
