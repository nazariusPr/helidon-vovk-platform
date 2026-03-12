package org.nazarius.dto;

public class AuthDto {
    private String username;
    private String password;
    private String role;

    public AuthDto(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AuthDto() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
