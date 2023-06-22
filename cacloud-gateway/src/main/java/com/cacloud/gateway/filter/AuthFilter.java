package com.cacloud.gateway.filter;

import com.cacloud.common.constant.CacheConstants;
import com.cacloud.common.constant.HttpStatus;
import com.cacloud.common.constant.SecurityConstants;
import com.cacloud.common.constant.TokenConstants;
import com.cacloud.common.utils.JwtUtils;
import com.cacloud.common.utils.ServletUtils;
import com.cacloud.common.utils.StringUtils;
import com.cacloud.gateway.config.IgnoreWhiteProperties;
import com.cacloud.gateway.domain.AuthorizationMessage;
import com.cacloud.redis.common.config.RedisService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 网关鉴权
 *
 * @author ruoyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered
{
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Autowired
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        //验证是token验证还是aksk的验签,若是aksk则直接跳过token验证
        String authorization=request.getHeaders().getFirst("Authorization");
        AuthorizationMessage msg=this.convertAuthorizationMessage(authorization);
        //没有签名信息
        if(msg!=null&&StringUtils.isNotEmpty(msg.getAk())) {
            return chain.filter(exchange);
        }
        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites()))
        {
            return chain.filter(exchange);
        }

        String token = getToken(request);
        if (StringUtils.isEmpty(token))
        {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null)
        {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(claims);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin)
        {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userId = JwtUtils.getUserId(claims);
        String userName = JwtUtils.getUserName(claims);
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(userName))
        {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userId);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, userName);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }


    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value)
    {
        if (value == null)
        {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name)
    {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg)
    {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token)
    {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request)
    {
        String token = request.getHeaders().getFirst(TokenConstants.TOKEN);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }


    //根据header的Authorization解析出验签信息
    private AuthorizationMessage convertAuthorizationMessage(String authorization){
        if(StringUtils.isEmpty(authorization)){
            return null;
        }
        if(!authorization.startsWith("HMAC-SHA256")){
            return null;
        }
        authorization=authorization.replace("HMAC-SHA256","");
        String [] authorizationArr=authorization.split(",");
        if(authorizationArr==null|| authorizationArr.length==0){
            return null;
        }
        AuthorizationMessage result=new AuthorizationMessage();
        result.setProtocol("HMAC-SHA256");
        for(String item:authorizationArr){
            if(StringUtils.isEmpty(item)){
                return null;
            }
            String[] itemArr=item.split("=");
            if(itemArr==null||itemArr.length==0){
                return null;
            }
            String name=itemArr[0];
            String value=itemArr[1];
            if(StringUtils.isEmpty(name)){
                return null;
            }
            if(name.trim().equals("Credential")){
                int pos=value.indexOf("/");
                String ak=value.substring(0,pos);
                String credentialScope=value.substring(pos+1);
                result.setAk(ak.trim());
                result.setCredentialScope(credentialScope);
            }else if(name.trim().equals("SignedHeaders")){
                result.setSignedHeaders(value.trim());
            }else if(name.trim().equals("Signature")){
                result.setSignature(value.trim());
            }

        }
        return result;
    }
    @Override
    public int getOrder()
    {
        return -200;
    }


}