package com.lord.local.gptjavaapi.service;

import com.lord.local.gptjavaapi.dao.entity.User;
import com.lord.local.gptjavaapi.dao.repository.UserTokenRepository;
import com.lord.local.gptjavaapi.exception.ResponseStatusEnum;
import com.lord.local.gptjavaapi.exception.ServiceException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {


    @Value("${jwt.token.secret.key}")
    private String SECRET_KEY;
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days in milliseconds

    @Autowired
    private UserServer _userServer;

    @Autowired
    private UserTokenRepository _userTokenRepository;


    public String generateToken(User login) {
        String userAccount = login.getAccount();
        Long version = login.getVersion();
        User authorization = _userServer.authorization(login.getAccount(), login.getVersion());
        if (authorization == null) {
            //授权信息过期
            _userTokenRepository.clearToken(login.getUid());
            return null;
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(login.getAccount())
                .setId(login.getUid().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY);
        HashMap<String, Object> claimMap = new HashMap<>();
        claimMap.put("userId", login.getUid());
        claimMap.put("userAccount", userAccount);
        claimMap.put("version", version);
        //添加token令牌到服务缓存 表示当前用户初次访问过服务
        String token = jwtBuilder.addClaims(claimMap).compact();
        _userTokenRepository.putToken(login.getUid(), token);

        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            Long userId = Long.valueOf(claimsJws.getBody().get("userId").toString());
            String userAccount = (String) claimsJws.getBody().get("userAccount");
            Long version = Long.valueOf(claimsJws.getBody().get("version").toString());
            Boolean tokenIsExist = _userTokenRepository.validToken(userId, token);
            if (!tokenIsExist) {
                //校验db是否存在
                User authorization = _userServer.authorization(userAccount, version);
                if (authorization == null) {
                    return false;
                }
                //token补偿
                _userTokenRepository.putToken(userId, token);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public User getTokenClaims(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        if (StringUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
            throw new ServiceException(ResponseStatusEnum.CLIENT_NOT_AUTH, "用户未登录");
        }
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(header.substring(7))
                    .getBody();
        } catch (Exception e) {
            throw new ServiceException(ResponseStatusEnum.CLIENT_ILLEGAL_AUTH, "授权验证异常");
        }
        User user = new User();
        user.setUid(Long.valueOf(body.getId()));
        user.setAccount(body.getSubject());
        user.setVersion(Long.valueOf(body.get("version").toString()));
        return user;

    }
}
