package Siirex.payload;

import lombok.Data;

@Data
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    // Constructor với đối số "accessToken"
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
