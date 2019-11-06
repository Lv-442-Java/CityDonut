package com.softserve.ita.java442.cityDonut.security;

import com.softserve.ita.java442.cityDonut.model.User;
import com.softserve.ita.java442.cityDonut.service.UserService;
import com.softserve.ita.java442.cityDonut.exception.JwtAuthenticationExeption;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTTokenProvider {

    private String SECRET = "CityDonut";
    private long VALIDITY_TIME_IN_MILLISECONDS = 3600000;
    private Date CURRENT_DATE;
    private Date VALIDITY;
    private String HEADER = "Authorization";

    @Autowired
    private JWTUserDetailsService jwtUserDetailsService;

    @Autowired
    private CookieProvider cookieProvider;

    @Autowired
    private static UserService u;

    @PostConstruct
    protected void init() {
        SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("roles", user.getRole().getRole());
        claims.put("id", user.getId());

        CURRENT_DATE = new Date();
        VALIDITY = new Date(CURRENT_DATE.getTime() + VALIDITY_TIME_IN_MILLISECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(CURRENT_DATE)
                .setExpiration(VALIDITY)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return cookieProvider.readCookie(request);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date()))
                return false;
            return true;
        } catch (JwtException e) {
            throw new JwtAuthenticationExeption("JWT token is invalid");
        }
    }

    public String parseToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
