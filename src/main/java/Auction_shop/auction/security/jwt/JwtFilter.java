package Auction_shop.auction.security.jwt;

import Auction_shop.auction.security.CustomUserDetails;
import Auction_shop.auction.security.MemberSecurity;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken= request.getHeader("Authorization");
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = accessToken.split(" ")[1];

        try {
            jwtUtil.isExpired(token);
        }
        catch (ExpiredJwtException e){
            PrintWriter writer = response.getWriter();
            writer.write("Access Token Expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(token);
        if(!category.equals("access")){
            PrintWriter writer = response.getWriter();
            writer.write("Invalid Access Token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(token); //uuid
        String nickname = jwtUtil.getNickname(token);
        String role = jwtUtil.getRole(token);

        MemberSecurity memberSecurity = MemberSecurity.builder()
                        .username(username)
                        .nickname(nickname)
                        .role(role)
                        .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(memberSecurity);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
