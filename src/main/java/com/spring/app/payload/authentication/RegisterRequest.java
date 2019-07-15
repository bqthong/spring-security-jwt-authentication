package com.spring.app.payload.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {
    
    @Size(max = 100)
    @NotBlank
    private String name;
    
    @NotBlank
    @Size(max = 50)
    private String username;
    
    @NotBlank
    @Size(max = 100)
    private String password;
    
    @Size(max = 50)
    @Email
    @NotBlank
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
