package com.flowershop;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * 全接口测试 —— 右键 Run As → Java Application
 */
public class ApiTest {

    private static final String BASE = "http://localhost:18080/api";
    private static final RestTemplate REST = new RestTemplate();
    private static String customerToken;  // 存顾客 Token
    private static String storeToken;     // 存花店 Token

    public static void main(String[] args) {
        // ===== 1. 注册 + 登录 =====
        post("/auth/customer/register", "{\"name\":\"张三\",\"phone\":\"13800138000\",\"email\":\"z@qq.com\",\"password\":\"123456\"}", "【注册顾客】");
        String loginRsp = post("/auth/customer/login", "{\"account\":\"13800138000\",\"password\":\"123456\"}", "【顾客登录】");
        customerToken = extractToken(loginRsp);

        post("/auth/store/register", "{\"name\":\"测试花店\",\"phone\":\"13811111111\",\"address\":\"北京市\",\"password\":\"123456\"}", "【注册花店】");
        String storeRsp = post("/auth/store/login", "{\"account\":\"测试花店\",\"password\":\"123456\"}", "【花店登录】");
        storeToken = extractToken(storeRsp);

        // ===== 2. 花店先培育几种花 =====
        authPost("/store/manage/cultivate?flowerName=红玫瑰&flowerType=玫瑰&unitPrice=99.00&costPrice=50.00&flowerMeaning=我爱你&quantity=100", storeToken, "【培育红玫瑰】");
        authPost("/store/manage/cultivate?flowerName=白百合&flowerType=百合&unitPrice=88.00&costPrice=40.00&flowerMeaning=纯洁&quantity=80", storeToken, "【培育白百合】");

        // ===== 3. 鲜花浏览 =====
        get("/flowers?page=1&size=10", "【鲜花列表】");
        get("/flowers/search?name=玫瑰", "【搜索玫瑰】");
        get("/stores/top?n=5", "【热门店铺Top5】");
        get("/stores/1", "【店铺详情】");
        get("/stores/1/flowers", "【店铺的鲜花】");

        // ===== 4. 加购物车 =====
        authPost("/cart/items?flowerId=1&storeId=1&quantity=3", customerToken, "【加购物车-3朵玫瑰】");
        getAuth("/cart", customerToken, "【查看购物车】");

        // ===== 5. 下单 =====
        String orderRsp = authPost("/orders?address=北京市海淀区&paymentMethod=微信支付&shippingAddress=北京市海淀区", customerToken, "【下单】");

        // ===== 6. 查看订单 =====
        getAuth("/orders/my?page=1&size=10", customerToken, "【我的订单】");
        getAuth("/customer/orders?page=1&size=10", customerToken, "【顾客-我的订单】");
        getAuth("/customer/info", customerToken, "【顾客信息】");

        // ===== 7. 花店管理 =====
        getAuth("/store/manage/stock", storeToken, "【店铺库存】");
        getAuth("/store/manage/orders", storeToken, "【店铺订单】");
        getAuth("/store/manage/sales", storeToken, "【销售统计】");

        System.out.println("\n========== 全部测试完成！ ==========");
    }

    // ===== 工具方法 =====
    static String post(String path, String json, String label) {
        HttpHeaders h = new HttpHeaders(); h.setContentType(MediaType.APPLICATION_JSON);
        String rsp = REST.postForEntity(BASE + path, new HttpEntity<>(json, h), String.class).getBody();
        System.out.println(label + ": " + rsp.substring(0, Math.min(150, rsp.length())) + "...");
        return rsp;
    }

    static String authPost(String path, String token, String label) {
        HttpHeaders h = new HttpHeaders(); h.setContentType(MediaType.APPLICATION_JSON);
        h.set("Authorization", "Bearer " + token);
        String rsp = REST.postForEntity(BASE + path, new HttpEntity<>("", h), String.class).getBody();
        System.out.println(label + ": " + rsp.substring(0, Math.min(150, rsp.length())) + "...");
        return rsp;
    }

    static void get(String path, String label) {
        String rsp = REST.getForEntity(BASE + path, String.class).getBody();
        System.out.println(label + ": " + rsp.substring(0, Math.min(150, rsp.length())) + "...");
    }

    static void getAuth(String path, String token, String label) {
        HttpHeaders h = new HttpHeaders(); h.set("Authorization", "Bearer " + token);
        String rsp = REST.exchange(BASE + path, HttpMethod.GET, new HttpEntity<>(h), String.class).getBody();
        System.out.println(label + ": " + rsp.substring(0, Math.min(150, rsp.length())) + "...");
    }

    static String extractToken(String json) {
        int start = json.indexOf("\"token\":\"") + 9;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
