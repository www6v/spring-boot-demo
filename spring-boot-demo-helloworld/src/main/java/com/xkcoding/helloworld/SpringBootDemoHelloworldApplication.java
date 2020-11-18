package com.xkcoding.helloworld;

import com.tuhu.renault.autoconfigure.provider.CacheManager;
import com.tuhu.renault.common.exception.CacheParamException;
import com.tuhu.renault.common.exception.TooLongKeyOrValueException;
import com.tuhu.renault.chassis.tikv.ChassisClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * <p>
 * SpringBoot启动类
 * </p>
 *
 * @package: com.xkcoding.helloworld
 * @description: SpringBoot启动类
 * @author: yangkai.shen
 * @date: Created in 2018/9/28 2:49 PM
 * @copyright: Copyright (c)
 * @version: V1.0
 * @modified: yangkai.shen
 */
@SpringBootApplication
@RestController
public class SpringBootDemoHelloworldApplication {

    private static ChassisClient<String, Object> cache;

	public static void main(String[] args) {
//        System.setProperty("java._appid_", "int-website-arch-renault-portal-api");
//        System.setProperty("java._environment_", "work");
//        System.setProperty("apollo.meta", "http://apollo.tuhu.work:8090");
//        System.setProperty("env", "DEV");

        System.setProperty("java._appid_", "int-website-arch-renault-portal-api");
        System.setProperty("java._environment_", "production");
        System.setProperty("apollo.meta", "http://apollo.ad.tuhu.cn:8080");
        System.setProperty("env", "PRO");

	    SpringApplication.run(SpringBootDemoHelloworldApplication.class, args);

        cache = (ChassisClient) CacheManager.getCache("testtikv");
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(required = false, name = "who") String who) {
//		if (StrUtil.isBlank(who)) {
//			who = "World";
//		}
//		return StrUtil.format("Hello, {}!", who);

        tikvTest();
        return "";
	}

    public static void tikvTest() {
        try {

            Random r = new Random();
            int i = r.nextInt();
            String key = "key" + i;
            String value = "value" + i;
            cache.set(key, value);

            Object value2 = cache.get(key);
            if (value2 != null && value2.toString().equals(value)) {
                System.out.println("SET and GET success!");
            } else {
                System.out.println("SET or GET failed!");
            }

//            cache.delete(key);
//
//            value2 = cache.get(key);
//            Assert.isTrue((value2 == null || value2.toString().isEmpty()), () -> {
//                System.out.println("Delete failed!!");
//                return "Delete failed!";
//            });

        } catch (TooLongKeyOrValueException e) {
            e.printStackTrace();
        } catch (CacheParamException e) {
            e.printStackTrace();
        }

        // For non spring environment, please call closeCache to release connection
        CacheManager.closeCache(cache);
    }

}
