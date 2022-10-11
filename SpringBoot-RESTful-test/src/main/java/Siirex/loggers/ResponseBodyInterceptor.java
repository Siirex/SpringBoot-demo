package Siirex.loggers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Logging API responses?
 * - ResponseBodyAdvice cho phép tùy chỉnh Response sau khi thực thi @ResponseBody hoặc
 * ResponseEntity controller method, nhưng trước đó phần Body phải được ghi bằng HttpMessageConverter.
 * - Vì vậy, có thể implement 'ResponseBodyAdvice' này để bắt Response của từng API như bên dưới:
 */

@ControllerAdvice // sẽ được tự động phát hiện bởi RequestMappingHandlerAdapter & ExceptionHandlerExceptionResolver
public class ResponseBodyInterceptor implements ResponseBodyAdvice<Object> {

    @Autowired
    LoggingService loggingService;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        loggingService.displayResp(
                ((ServletServerHttpRequest) request).getServletRequest(),
                ((ServletServerHttpResponse) response).getServletResponse(),
                body
        );
        return body;
    }
}
