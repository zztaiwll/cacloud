//package com.cacloud.gateway.filter;
//
//import com.cacloud.common.constant.HttpStatus;
//import com.cacloud.common.utils.ServletUtils;
//import com.cacloud.common.utils.StringUtils;
//import com.cacloud.gateway.domain.AuthorizationMessage;
//import com.cacloud.redis.common.config.RedisService;
//import lombok.SneakyThrows;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.util.Arrays;
//import java.util.BitSet;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class AkSkSignFilter implements GlobalFilter, Ordered {
//    private static final Logger log = LoggerFactory.getLogger(AkSkSignFilter.class);
//    @Autowired
//    private RedisService redisService;
//
//    private static final BitSet URLENCODER = new BitSet(256);
//
//    private static final String CONST_ENCODE = "0123456789ABCDEF";
//    public static final Charset UTF_8 = StandardCharsets.UTF_8;
//
//    @SneakyThrows
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
//        //首先确认是否是aksk方式的验签，如果不是直接跳过
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpRequest.Builder mutate = request.mutate();
//
//        String authorization=request.getHeaders().getFirst("Authorization");
//        if(StringUtils.isEmpty(authorization)){
//            return chain.filter(exchange);
//        }
//        //解析出ak等信息
//        AuthorizationMessage msg=this.convertAuthorizationMessage(authorization);
//        //没有签名信息
//        if(msg==null||StringUtils.isEmpty(msg.getAk())){
//            return chain.filter(exchange);
//        }
//        String path = request.getURI().getPath();
//        HttpMethod method=request.getMethod();
//        String host=request.getHeaders().getFirst("Host");
//        String xDate=request.getHeaders().getFirst("X-Date");
//        String sha256=request.getHeaders().getFirst("X-Content-Sha256");
//        String contentType=request.getHeaders().getFirst("Content-Type");
//        String timeStamp=request.getHeaders().getFirst("timeStamp");
//        //验证时间戳，最好是在30秒内的请求
//        Long sendTime=Long.parseLong(timeStamp);
//        if(System.currentTimeMillis()-sendTime>30000){
//            return unauthorizedResponse(exchange, "请求时间应该在30秒以内");
//        }
//        Map<String,String> realQueryList=new HashMap<>();
//        byte[] body=new  byte[0];
//        if(method.matches(HttpMethod.GET.toString())||method.matches(HttpMethod.DELETE.toString())){
//            //从拼接字符串中获取
//
//        }else{
//            //从body中获取
//        }
//        //解析authorization获得其他参数
//        StringBuilder querySB = new StringBuilder();
//        for (String key : realQueryList.keySet()) {
//            querySB.append(signStringEncoder(key)).append("=").append(signStringEncoder(realQueryList.get(key))).append("&");
//        }
//        //使用ak从redis中获取sk
//        String sk=redisService.getCacheObject(msg.getAk());
//        String canonicalStringBuilder = method + "\n" + path + "\n" + querySB + "\n" +
//                "host:" + host + "\n" +
//                "x-date:" + xDate + "\n" +
//                "x-content-sha256:" + sha256 + "\n" +
//                "content-type:" + contentType + "\n" +
//                "\n" +
//                msg.getSignedHeaders()+ "\n" +
//                sha256;
//        String hashcanonicalString = hashSHA256(canonicalStringBuilder.getBytes());
//        String credentialScope=msg.getCredentialScope();
//        String dataArr[]=credentialScope.split("/");
//        if(dataArr==null||dataArr.length<3){
//            return unauthorizedResponse(exchange, "credentialScope格式不正确");
//        }
//        String shortXDate=dataArr[0];
//        String region=dataArr[1];
//        String service=dataArr[2];
//        String signString = "HMAC-SHA256" + "\n" + xDate + "\n" + credentialScope+ "\n" + hashcanonicalString;
//        byte[] signKey = this.genSigningSecretKeyV4(sk, shortXDate, region, service);
//        String signature = this.bytesTohex(hmacSHA256(signKey, signString));
//        if(!signature.equals(msg.getSignature())){
//            return unauthorizedResponse(exchange, "签名错误");
//        }
//        return chain.filter(exchange.mutate().request(mutate.build()).build());
//    }
//
//    @Override
//    public int getOrder() {
//        return -300;
//    }
//    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg)
//    {
//        log.error("[验签异常处理]请求路径:{}", exchange.getRequest().getPath());
//        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.VALIDATE_FORMAT_ERROR);
//    }
//    private String signStringEncoder(String source) {
//        if (source == null) {
//            return null;
//        }
//        StringBuilder buf = new StringBuilder(source.length());
//        ByteBuffer bb = UTF_8.encode(source);
//        while (bb.hasRemaining()) {
//            int b = bb.get() & 255;
//            if (URLENCODER.get(b)) {
//                buf.append((char) b);
//            } else if (b == 32) {
//                buf.append("%20");
//            } else {
//                buf.append("%");
//                char hex1 = CONST_ENCODE.charAt(b >> 4);
//                char hex2 = CONST_ENCODE.charAt(b & 15);
//                buf.append(hex1);
//                buf.append(hex2);
//            }
//        }
//
//        return buf.toString();
//    }
//    private byte[] genSigningSecretKeyV4(String secretKey, String date, String region, String service) throws Exception {
//        byte[] kDate = hmacSHA256((secretKey).getBytes(), date);
//        byte[] kRegion = hmacSHA256(kDate, region);
//        byte[] kService = hmacSHA256(kRegion, service);
//        return hmacSHA256(kService, "request");
//    }
//    public static byte[] hmacSHA256(byte[] key, String content) throws Exception {
//        try {
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(new SecretKeySpec(key, "HmacSHA256"));
//            return mac.doFinal(content.getBytes());
//        } catch (Exception e) {
//            throw new Exception(
//                    "Unable to calculate a request signature: "
//                            + e.getMessage(), e);
//        }
//    }
//
//    public  String hashSHA256(byte[] content) throws Exception {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            return bytesTohex(md.digest(content));
//        } catch (Exception e) {
//            throw new Exception(
//                    "Unable to compute hash while signing request: "
//                            + e.getMessage(), e);
//        }
//    }
//    //将bytep[]转化为十六进制字符串
//    private  String bytesTohex(byte[] bytes) {
//        StringBuilder hex = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            boolean flag = false;
//            if (b < 0) flag = true;
//            int absB = Math.abs(b);
//            if (flag) absB = absB | 0x80;
//            System.out.println(absB & 0xFF);
//            String tmp = Integer.toHexString(absB & 0xFF);
//            if (tmp.length() == 1) { //转化的十六进制不足两位，需要补0
//                hex.append("0");
//            }
//            hex.append(tmp.toLowerCase());
//        }
//        return hex.toString();
//    }
//    //根据header的Authorization解析出验签信息
//    private AuthorizationMessage convertAuthorizationMessage(String authorization){
//        if(StringUtils.isEmpty(authorization)){
//            return null;
//        }
//        if(!authorization.startsWith("HMAC-SHA256")){
//            return null;
//        }
//        authorization=authorization.replace("HMAC-SHA256","");
//        String [] authorizationArr=authorization.split(",");
//        if(authorizationArr==null|| authorizationArr.length==0){
//            return null;
//        }
//        AuthorizationMessage result=new AuthorizationMessage();
//        result.setProtocol("HMAC-SHA256");
//        for(String item:authorizationArr){
//            if(StringUtils.isEmpty(item)){
//                return null;
//            }
//            String[] itemArr=item.split("=");
//            if(itemArr==null||itemArr.length==0){
//                String name=itemArr[0];
//                String value=itemArr[1];
//                if(StringUtils.isEmpty(name)){
//                    return null;
//                }
//                if(name.equals("Credential")){
//                    int pos=value.indexOf("/");
//                    String ak=value.substring(0,pos);
//                    String credentialScope=value.substring(pos+1);
//                    result.setAk(ak);
//                    result.setCredentialScope(credentialScope);
//                }else if(name.equals("SignedHeaders")){
//                    result.setSignedHeaders(value);
//                }else if(name.equals("Signature")){
//                    result.setSignature(value);
//                }
//            }
//        }
//        return result;
//    }
//    //读取body的信息
//    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest){
//        //Get the request body
//        Flux<DataBuffer> body = serverHttpRequest.getBody();
//        StringBuilder sb = new StringBuilder();
//        body.subscribe(buffer -> {
//            byte[] bytes = new byte[buffer.readableByteCount()];
//            buffer.read(bytes);
//            DataBufferUtils.release(buffer);
//            String bodyString = new String(bytes, StandardCharsets.UTF_8);
//            sb.append(bodyString);
//        });
//        return sb.toString();
//    }
//}
