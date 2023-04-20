package com.lord.local.gptjavaapi.dao.repository;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserTokenRepository {
    public static final HashMap<Long, Set<String>> userTokenMap = new HashMap<>();

    public Boolean validToken(Long uid, String token) {
        if (uid == null || StringUtils.isEmpty(token)) {
            return false;
        }
        Set<String> tokenList = UserTokenRepository.userTokenMap.get(uid);
        if (tokenList == null) {
            return false;
        }
        return tokenList.contains(token);
    }

    public Boolean clearToken(Long uid) {
        if (uid == null) {
            return false;
        }
        UserTokenRepository.userTokenMap.remove(uid);
        return true;
    }

    public Boolean clearToken(Long uid, String token) {
        if (uid == null || StringUtils.isEmpty(token)) {
            return false;
        }
        Set<String> tokenList = UserTokenRepository.userTokenMap.get(uid);
        if (StringUtils.isEmpty(tokenList)) {
            return true;
        }
        boolean remove = tokenList.remove(token);
        return remove;

    }

    public Boolean putToken(Long uid, String token) {
        Set<String> tokenList = UserTokenRepository.userTokenMap.get(uid);
        if (StringUtils.isEmpty(tokenList)) {
            tokenList = new HashSet<>();
            tokenList.add(token);
            UserTokenRepository.userTokenMap.put(uid, tokenList);
        }
        return true;
    }
}
