package Siirex.JWT;

import Siirex.model.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Sau khi có các thông tin về người dùng, cần mã hóa thông tin người dùng thành chuỗi JWT.
 */

@Component
@Slf4j
public class JwtTokenProvider {

    // Private Key?
    private final String JWT_SECRET = "siirex";

    // Thời gian có hiệu lực của JWT token?
    private final long JWT_EXPIRATION = 604800000L;

    // Tạo ra JWT token?
    public String generateToken(CustomUserDetails principal) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                    .setSubject(Long.toString(principal.getUserEntity().getId())) // Claim 'Subject': thông tin của Principal (Id/Username)
                    .setIssuedAt(now) // Claim 'Issuer': thời gian tạo Token
                    .setExpiration(expiryDate) // Claim 'Expiration': expiration date of Token
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // Cung cấp thuật toán mã hóa + PrivateKey / Signer (chữ ký)
                    .compact();
    }

    // Xác minh (authenticate) JWT Token được gửi lên?
    public boolean validateToken(String authToken) {
        try {
            // Decrypt JWT token (use Private Key "JWT_SECRET")
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true; // Token hợp lệ, xác thực token thành công!!!

        } catch (SignatureException ex) {
            log.error("Invalid Signature!");

        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token!");

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token!");

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token!");

        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty!");
        }
        return false;
    }

    // Trích xuất thông tin user từ JWT token thu được?
    public Long getUserIdFromToken(String token) {

        // Decrypt JWT token (use Private Key "JWT_SECRET")
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET) // cung cấp PrivateKey / Signer để decrypt
                .parseClaimsJws(token) // parse Claims từ token
                .getBody();

        // Lấy claim 'Subject' sau khi decrypt
        return Long.parseLong(claims.getSubject());
    }

}
