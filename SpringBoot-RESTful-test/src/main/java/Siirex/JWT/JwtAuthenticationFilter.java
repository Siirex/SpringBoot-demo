package Siirex.JWT;

import Siirex.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * - JwtAuthenticationFilter: Có nhiệm vụ kiểm tra request của Client trước khi nó tới Servlet (Controller).
 * - Nó sẽ lấy Authorization header ra và kiểm tra xem JWT Token được Client gửi lên có hợp lệ không?!
 */

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService service;

    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        System.out.println("> Token from request: " + bearerToken);

        // Kiểm tra xem header Authorization có chứa thông tin jwt không?
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // Lấy token từ request
            String jwt = this.getTokenFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                // Lấy Id user từ chuỗi JWT.
                Long userId = tokenProvider.getUserIdFromToken(jwt);

                // Lấy thông tin người dùng từ Database dựa trên Id có được.
                UserDetails userDetails = service.loadUserById(userId);

                // Nếu lấy được User, nghĩa là thông tin trích xuất từ Token là hợp lệ, thì ...
                if (userDetails != null) {
                    // Set đối tượng Authentication (thông tin authentication của principal) cho Seturity Context
                    UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // set thêm trường Details
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    System.out.println("Thông tin extract từ Token không hợp lệ --> Token không hợp lệ!");
                }
            }

        } catch (Exception e) {
            log.error("Failed on set user authentication!", e);
        }

        filterChain.doFilter(request,response);
    }
}
