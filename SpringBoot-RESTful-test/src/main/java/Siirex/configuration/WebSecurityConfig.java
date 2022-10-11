package Siirex.configuration;

import Siirex.JWT.JwtAuthenticationFilter;
import Siirex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // Bean use authenticate when REST Client sent Login POST request
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager Bean
        return super.authenticationManagerBean();
    }

    /** ------------------------------------------------------------------------- **/

    @Autowired
    private UserService service;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Không cần thiết lập này, việc authenticate vẫn thực thi bình thường???
     */

    /** ------------------------------------------------------------------------- **/

    @Bean
    public JwtAuthenticationFilter authenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .cors() // Ngăn chặn request từ một domain khác.
            .and()
            .csrf().disable(); // Disable CSRF protection

        http
            .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .anyRequest().authenticated();

        // Filter kiểm tra JWT token
        http.addFilterBefore(this.authenticationFilter(), UsernamePasswordAuthenticationFilter.class).csrf().disable();
        /**
         * - Chỉ định Default filter <UsernamePasswordAuthenticationFilter> được call trước,
         * rồi mới đến Custom filter <JwtAuthenticationFilter>
         * - Filter <JwtAuthenticationFilter> này sẽ chỉ lọc các request truy cập đến URL/PATH
         * yêu cầu phải xác thực (trừ "/api/login")
         */

        /** Cách hoạt động của Filter trong Spring Security?
         * - Khi làm việc với Spring Security thì mặc định sẽ có hàng loạt các Filter liên tiếp nhau
         * có nhiệm vụ Authentication và Authorization các request.
         * - Vậy khi muốn sử dụng Custom Filter thì phải config các addFilter(), addFilterBefore(),
         * addFilterAfter().
         */

        /** UsernamePasswordAuthenticationFilter?
         * - Trong Spring Security có 1 setting được gọi là FORM_LOGIN_FILTER, mặc định nó sẽ sử
         * dụng class 'UsernamePasswordAuthenticationFilter' để Filter.
         * - Khi Client post thông tin authenticate (username/password) thông qua request "/login",
         * sẽ phải đi qua Filter này đầu tiên (được config ở SecurityConfig).
         * - Trong Filter 'UsernamePasswordAuthenticationFilter' nó sẽ thực hiện call AuthenticationManager
         * để authenticate thông tin user vừa gửi lên.
         */

        /** N O T E
         * - Phạm vi ở đây là REST API, nên không dùng đến Login Form mặc định của Spring Security,
         * vì thế không dùng đến Filter này, mà sẽ làm việc trực tiếp với AuthenticationManager
         * để thực hiện quá trình authentication với Username/Password của Client.
         * - Dù thế, ta vẫn phải set Filter này trong phương thức addFilterBefore() - để chỉ định
         * rõ ràng rằng: filter "authenticationFilter()" sẽ phải được thực thi sau nó!
         */
    }

    /** ------------------------------------------------------------------------- **/

    /* Logging for API request (no response)?
    https://viblo.asia/p/hien-thi-log-cua-request-va-response-trong-spring-boot-Ljy5V7BMKra

    @Bean
    public CommonsRequestLoggingFilter loggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
    */
}
