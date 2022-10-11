package Siirex.controller;

import Siirex.JWT.JwtTokenProvider;
import Siirex.payload.LoginRequest;
import Siirex.payload.LoginResponse;
import Siirex.payload.RandomStuff;
import Siirex.model.CustomUserDetails;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class MainRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /** POST http://localhost:8080/api/login
     {
       "username": "hoangminh",
       "password": "304304"
     }
     * Response
     {
       "accessToken": "xx.yy.zz",
       "tokenType": "Bearer"
     }
     */
    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        /** A U T H E N T I C A T I O N
         * - Ở đây thực hiện call AuthenticationManager để authenticate thông tin user vừa gửi lên.
         *
         * - Trong AuthenticationManager lại sử dụng AuthencationProvider (mặc định là DaoAuthenticationProvider)
         * để thực hiện validate thông tin người dùng. DaoAuthenticationProvider sẽ call UserDetailService
         * để xác thực thông tin người dùng.
         *
         * - Cụ thể, AuthenticationManager sẽ sử dụng phương thức authenticate() mà AuthencationProvider
         * cung cấp để xác thực thông tin người dùng gửi lên.
         * - Phương thức authenticate() sẽ trả về đối tượng Authentication, cụ thể là một implementation
         * của interface Authentication, chính là UsernamePasswordAuthenticationToken!!!
         * - Ta sẽ truyền thông tin username/password vào Constructor UsernamePasswordAuthenticationToken
         * đó để AuthencationProvider tiến hành authenticate!
         *
         * - Đọc phần "Không có quyền truy cập vào password của user" trong MD file "Authentication & Authorization"!!!
         *
         * - Check log ta sẽ có một đối tượng Authentication được biểu diễn như sau:

             Authentication = UsernamePasswordAuthenticationToken [
                Principal = CustomUserDetails (
                    userEntity = UserEntity (
                        id=1,
                        username=hoangminh,
                        password=$2a$10$jp1WFCZbLPB5gBbe6HYbGeIq5TKKWeB9hSbYBsRRV.58MssgThVCS
                    )
                ),
                Credentials=[PROTECTED],
                Authenticated=true,
                Details = WebAuthenticationDetails [
                    RemoteIpAddress=0:0:0:0:0:0:0:1,
                    SessionId=null
                ],
                Granted Authorities=[ROLE_USER]]
             ]

         */

        /**
         * - Quy trình authentication ở dưới cũng được Filter UsernamePasswordAuthenticationFilter
         * sử dụng để authenticate với username/password.
         * - Tuy nhiên phạm vi ở đây là REST API, nên không dùng đến Login Form mặc định của
         * Spring Security, vì thế không dùng đến Filter này, mà sẽ làm việc trực tiếp với
         * AuthenticationManager!!!
         */

        // Quá trình authenticate với username/password của Client gửi lên
        Authentication authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        /**
         * - Đến đây, nếu quá trình Authentication trên không thành công (sai thông tin username/pass),
         * Spring Security sẽ ném ra BadCredentialsException.
         * - Khi đó, phương thức exceptionAuthenticate() ở dưới sẽ bắt được và xử lý!!!
         */

        // Nếu quá trình trên không xảy ra exception, tức là thông tin hợp lệ!
        // Tiến hành set thông tin authentication vào Security Context
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        /** [WARNING] Tuy nhiên, nếu làm thế, quá trình authenticate bằng JWT token sau đó sẽ không có hiệu lực?
         *
         * - Vì bản thân thông tin Authentication (với username/password) đã được stored trong
         * Security Context, do đó Spring Security nhận diện User đó là đã hợp lệ (Principal)!
         * - Từ đó, Spring Security cấp cho Principal đó 1 Session, có thể request đến mọi Resource -
         * mà không cần thiết phải khai báo JWT Token trong Header của các request đó!
         *
         * - Vì vậy, để có thể authenticate bằng JWT token, tại đây, ta KHÔNG store thông tin
         * Authentication với username/password ở Security Context.
         * - Mà đến quá trình authenticate bằng JWT token (thực thi ở Filter class), nếu Token chính
         * xác, thì mới store thông tin Authentication với JWT Token (chính là đối tượng
         * UsernamePasswordAuthenticationToken(principal,credentials,authorities) ) vào Security Context.
         */

        // Trả về jwt cho người dùng (với đầu vào là thông tin serDetails của authenticated user)
        String jwt = this.tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());

        // Call constructor với tham số đầu vào là JWT token
        return new LoginResponse(jwt);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exceptionAuthenticate() {
        return ResponseEntity.ok("Xác thực không thành công!");
    }

    /** Truy cập Resource với Token có được?
     * GET http://localhost:8080/api/contents
        KEY: Authorization
        VALUE: xx.yy.zz
     * Response:
     {
        "message": "JWT Hợp lệ mới có thể thấy được message này!"
     }
     */
    @GetMapping("/contents")
    public RandomStuff randomStuff(){
        return new RandomStuff("JWT Token nợp lệ mới có thể thấy được message này!");
    }

    /** Cung cấp Cookie sau khi xác thực Token thành công???
     * - Khi REST Client truy cập Resource với JWT Token được Security Filter Chain đánh giá là hợp lệ,
     * họ được quyền truy cập vào Resource đó miễn là Role cho phép.
     * - Từ đây, Spring Security sẽ cung cấp cho User này 1 Session (chỉ định bằng Cookie) - để có
     * thể truy cập lần sau mà không cần gửi kèm Token trong Header của request!!!
     *
     * - Check log Response của REST Service ngay sau khi Client truy cập Resource với Token hợp lệ,
     * ta có được nội dung trong Body, và cả thông tin Cookie trong Header cụ thể như sau:

     ResponseHeaders = [{Vary=Origin, Set-Cookie=JSESSIONID=E0A7FDC8B5CE6533EA52E5F05546D953; Path=/; HttpOnly}]
     ResponseBody = [RandomStuff(message=JWT Token nợp lệ mới có thể thấy được message này!)]

     */

    /** Nếu REST Client gửi lên JWT Token không hợp lệ, nó có thể sẽ ném ra các Exceptions sau:
     * - SignatureException: Invalid JWT token (io.jsonwebtoken)
     * - MalformedJwtException: Invalid JWT token
     * - ExpiredJwtException: Expired JWT token
     * - UnsupportedJwtException: Unsupported JWT token
     * - IllegalArgumentException: JWT claims string is empty
     *
     * Khi đó, ta sẽ cần các hàm Handler để bắt các Exception này và xử lý!!!
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SignatureException.class) // exception này đã được try-catch trong validateToken() bắt, nên ở đây ở không bắt được nữa!
    public ResponseEntity<String> exceptionInvalidToken() {
        return ResponseEntity.ok("Token authentation false!");
    }
}



/** Validate annotation?
 * - @Valid annotation được sử dụng với các tham số đầu vào trong Rest method.
 * - Spring Boot sẽ tìm kiếm những tham số được chú thích với @Valid để thực hiện các kiểm tra như
 * đã định nghĩa trước đó.
 *
 * - Ví dụ chỉ định trường 'password' trong đối tượng DTO lấy ra (là tham số đầu vào @RequestBody
 * được chú thích bởi @Valid) - phải có ký tự đặc biệt, độ dài tổi thiểu, ...
 *
 * - Khi các tham số không đáp ứng đủ điều kiện mà chúng ta đã đặt ra trước đó, Spring Boot
 * sẽ ném <MethodArgumentNotValidException>.
 *
 * - Do vậy, cần sử dụng @ExceptionHandler annotation để bắt <MethodArgumentNotValidException> ném
 * ra từ Spring Boot khi có lỗi validate để xử lý và trả về kết quả lỗi cho client.
 *
 * - Để kiểm thử, chúng ta sẽ khởi chạy ứng dụng và request với những thông tin không hợp lệ?
        <curl -d "{\"age\":10}" -H "Content-Type: application/json" -X POST http://localhost:8080/users
 * - Ta sẽ được response như sau:
        {
            "name": "Name is mandatory",
            "age": "Age can not be less than 18",
            "email": "Email is mandatory"
        }
 */

/*

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @Min(value = 18, message = "Age can not be less than 18")
    @Max(value = 50, message = "Age can not be greater than 50")
    private Integer age;

}

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @PostMapping("/users")
    ResponseEntity<String> addUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok("User is valid");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
 */

