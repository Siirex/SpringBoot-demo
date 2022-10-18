package Siirex.service;

import Siirex.model.CustomUserDetails;
import Siirex.model.UserEntity;
import Siirex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Khi Client đăng nhập thì Spring Security sẽ cần lấy các thông tin UserDetails hiện có để authenticate!
 *
 * - Vì vậy, có thể tạo ra một Class implements từ 'UserDetailsService' mà Spring Security cung cấp
 * để làm nhiệm vụ này. Trong đó nó cung cấp sẵn loadUserByUsername() để tìm Principal trong DB.
 *
 * - Ngoài ra, có thể tạo ra một Class implements từ Custom Interface khác (như trong Project 05-...-use-Nimbusds)
 * nhưng ta sẽ phải tạo thủ công phương thức tương tự như loadUserByUsername() để tìm Principal trong DB
 * giúp thực hiện quá tình authenticate!
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = repository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        // Trả về đối tượng CustomUserDetails và ...
        // Kích hoạt Contructor CustomUserDetails() với đối số là đối tượng UserEntity
        return new CustomUserDetails(user);
    }

    @Transactional
    public UserDetails loadUserById(Long userId) {
        UserEntity user = repository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + userId)
        );
        return new CustomUserDetails(user);
    }
}
