package org.nbc.account.trollo.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nbc.account.trollo.global.dto.ApiResponse;
import org.nbc.account.trollo.global.security.UserDetailsImpl;
import org.nbc.account.trollo.global.security.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        String token = jwtUtil.getTokenFromRequest(request);

        if (Objects.nonNull(token)) {
            if (jwtUtil.validateToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                // email -> search user
                String email = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // -> put this in userDetails
                UserDetailsImpl userDetails = userDetailsService.getUserDetails(email);
                // ->  put this in authentication principal
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
                // -> put this in securityContent
                context.setAuthentication(authentication);
                // -> put this in SecurityContextHolder
                SecurityContextHolder.setContext(context);
                // -> now you can search with @AuthenticationPrincipal

            } else {
                // when authorization doesn't exist
                ApiResponse<Void> responseDto = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "this token is not valid");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(responseDto));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
