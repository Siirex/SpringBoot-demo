package Siirex.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * - Trong hầu hết các Rest API đều sử dụng các DTO như là một Class dùng để giao tiếp giữa Client và
 * Server, không nên sử dụng trực tiếp một Entity để truyền vào nhận giữa client-server vì đôi lúc có
 * những thông tin trong Entity không nên trả về cho Server.
 * - Như vậy sử dụng DTO cho phép chúng ta toàn quyền quyết định những dữ liệu dùng trong giao tiếp
 * giữa client và server.
 * - https://shareprogramming.net/validate-du-lieu-rest-api-trong-spring-boot/
 */

@Data
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

/** @NotBlank annotation?
 * - Được sử dụng với một trường dữ liệu ngầm hiểu rằng dữ liệu từ Client truyền lên không được để
 * trống trường này.
 */

/** Min & Max annotation?
 * - @Min được sử dụng để chỉ định một giá trị không thể nhỏ một giá trị được chỉ định.
 * - @Max với một giá trị và giá trị gửi lên phải bé hơn hoặc bằng.

 @Min(value = 18, message = "Age can not be less than 18")
 @Max(value = 50, message = "Age can not be greater than 50")
 private Integer age;

 */
