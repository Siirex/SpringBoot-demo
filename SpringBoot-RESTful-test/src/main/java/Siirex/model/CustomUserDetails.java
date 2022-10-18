package Siirex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/** Tham chiếu User với UserDetails?
 *
 * - Tại sao cần tham chiếu? Vì khi Client login thì Spring Security sẽ cần lấy các thông tin trong
 * 'UserDetails' hiện có để authenticate username/password/authorities!
 *
 * - Mặc định Spring Security sử dụng một đối tượng UserDetails để chứa toàn bộ thông tin về người dùng
 * hợp lệ (Principal) được trích xuất từ DB.
 *
 * - Vì vậy, cần tạo ra class 'CustomUserDetails' này để chuyển các thông tin của UserEntity thành UserDetails.
 */

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private UserEntity userEntity;

    /* C O N S T R U C T O R
    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
    */

    /**
     * - Vì ở đây kích hoạt Constructor (ở @Service) để lấy đối tượng CustomUserDetails
     * - Nên việc khai triển sẽ khác với Class 'UserPrincipal' trong <Project 05-...-use-Nimbusds>
     * dù thế, cả 2 Class có chức năng tương tự như nhau!
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mặc định sẽ để tất cả là ROLE_USER (demo)
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
