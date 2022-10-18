package Siirex.loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class LoggingServiceImpl implements LoggingService {

    Logger logger = LoggerFactory.getLogger("LoggingServiceImpl");

    @Override
    public void displayReq(HttpServletRequest request, Object body) {

        StringBuilder requestMessage = new StringBuilder();
        Map<String,String> parameters = getParameters(request);

        requestMessage.append("REQUEST ");
        requestMessage.append("Method = [").append(request.getMethod()).append("]");
        requestMessage.append(" Path = [").append(request.getRequestURI()).append("] ");

        if(!parameters.isEmpty()) {
            requestMessage.append(" Parameters = [").append(parameters).append("] ");
        }

        if(!Objects.isNull(body)) {
            requestMessage.append(" Body = [").append(body).append("]");
        }

        logger.info("log Request: {}", requestMessage);
    }

    @Override
    public void displayResp(HttpServletRequest request, HttpServletResponse response, Object body) {

        StringBuilder responseMessage = new StringBuilder();
        Map<String,String> headers = getHeaders(response);

        responseMessage.append("RESPONSE ");
        responseMessage.append(" Method = [").append(request.getMethod()).append("]");

        if(!headers.isEmpty()) {
            responseMessage.append(" ResponseHeaders = [").append(headers).append("]");
        }

        responseMessage.append(" ResponseBody = [").append(body).append("]");

        logger.info("log Response: {}", responseMessage);
    }

    // Get request parameters
    private Map<String,String> getParameters(HttpServletRequest request) {
        Map<String,String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.put(paramName,paramValue);
        }
        return parameters;
    }

    // Get request headers
    private Map<String,String> getHeaders(HttpServletResponse response) {
        Map<String,String> headers = new HashMap<>();
        Collection<String> headerMap = response.getHeaderNames();

        for (String str : headerMap) {
            headers.put(str, response.getHeader(str));
        }
        return headers;
    }

}
