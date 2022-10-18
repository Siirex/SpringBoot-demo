package Siirex.loggers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * - InterceptLog, RequestBodyInterceptor, ResponseBodyInterceptor - will be getting the request or responses of APIs.
 * - To log them in the required format, we could have a Service taking requests, responses and body as inputs:
 */

public interface LoggingService {

    void displayReq(HttpServletRequest request, Object body);
    void displayResp(HttpServletRequest request, HttpServletResponse response, Object body);
}
