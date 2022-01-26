package com.shail.security.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class SecretKeyUtil {

    private static List<String> keyList = new ArrayList<String>();

    @PostConstruct
    private void generateKey(){
        int i = 0;
        while (i++ < 11)
            keyList.add(generateSecretKey());
    }

    public static List<String> getKeyList() {
        return keyList;
    }

    private String generateSecretKey(){
        return UUID.randomUUID().toString().replaceAll("-", "")+ UUID.randomUUID().toString().replaceAll("-", "")+UUID.randomUUID().toString().replaceAll("-", "");
    }
}
