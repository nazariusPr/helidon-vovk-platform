package org.nazarius.model;

import org.nazarius.VovkDataCore.annotations.Column;
import org.nazarius.VovkDataCore.annotations.Entity;
import org.nazarius.VovkDataCore.annotations.PrimaryKey;
import org.nazarius.VovkDataCore.annotations.Validate;
import org.nazarius.VovkDataCore.enums.GenerationType;

@Entity(name = "users")
public class User {

    @PrimaryKey(strategy = GenerationType.AUTO)
    @Column(name = UserFields.ID)
    private Integer id;

    @Column(name = UserFields.USERNAME, unique = true, length = 100)
    @Validate(notBlank = true, minLength = 3, maxLength = 100)
    private String username;

    @Column(name = UserFields.PASSWORD)
    @Validate(notBlank = true, minLength = 6)
    private String password;

    @Column(name = UserFields.ROLE, length = 50)
    @Validate(notBlank = true)
    private String role;

    public User() {
    }

    public User(Integer id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public static String ID() {
        return UserFields.ID;
    }

    public static String USERNAME() {
        return UserFields.USERNAME;
    }

    public static String PASSWORD() {
        return UserFields.PASSWORD;
    }

    public static String ROLE() {
        return UserFields.ROLE;
    }

    public static final class UserFields {
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String ROLE = "role";

        private UserFields() {
        }
    }
}