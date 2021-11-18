//package br.com.ota.udemy.userserviceapi.config;
//
//import br.com.ota.udemy.userserviceapi.model.dto.request.LoginRequest;
//import br.com.ota.udemy.userserviceapi.model.dto.response.UserResponseDto;
//import br.com.ota.udemy.userserviceapi.service.UserService;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final Environment environment;
//    private final UserService userService;
//
//    @Autowired
//    public AuthenticationFilter(Environment environment, UserService userService, AuthenticationManager authenticationManager) {
//        this.environment = environment;
//        this.userService = userService;
//        super.setAuthenticationManager(authenticationManager);
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            LoginRequest loginRequest = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                    .readValue(request.getInputStream(), LoginRequest.class);
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        String username = ((User) authResult.getPrincipal()).getUsername();
//        UserResponseDto user = userService.findByEmail(username);
//        String token = Jwts.builder()
//                .setSubject(user.getId())
//                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time", "7000000"))))
//                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
//                .compact();
//        response.setHeader("token", token);
//    }
//}
