package com.hmall.gateway.filters;

import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * ClassName: AuthGlobalFilter
 * Package: com.hmall.gateway.filters
 * Description:
 *
 * @Author ME
 * @Create 2025/3/23 11:07
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request
        ServerHttpRequest request = exchange.getRequest();

        //2.判断是否需要登录校验
        if (isExclude(request.getPath().toString())) {
            //放行
            return chain.filter(exchange);
        }

        //3.获取token
        List<String> headers = request.getHeaders().get("authorization");
        String token = null;
        if (headers != null && !headers.isEmpty()) {
            token = headers.get(0);
        }
        Long userId = null;
        //4.校验解析
        try {
            userId = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            //拦截，设置响应状态码401
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 5.传递用户信息
        String userInfo = userId.toString();
        ServerWebExchange buildExchange = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        //6.放行
        return chain.filter(buildExchange);
    }

    private boolean isExclude(String path) {
        List<String> excludePaths = authProperties.getExcludePaths();
        for (String excludePath : excludePaths) {
            if (antPathMatcher.match(excludePath,path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
