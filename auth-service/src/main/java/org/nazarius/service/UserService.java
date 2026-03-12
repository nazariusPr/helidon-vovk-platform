package org.nazarius.service;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.dto.AuthDto;
import org.nazarius.model.User;
import org.nazarius.dto.UserDto;
import org.nazarius.mapper.UserMapper;
import org.nazarius.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // Fetch raw User entity by ID
    public User getEntityById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Fetch raw User entity by username
    public User getEntityByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    // Fetch all users as DTOs
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch paged users as DTOs
    public Page<UserDto> readPage(Pageable pageable) {
        // Get Page<User> from repository
        Page<User> userPage = repository.readPage(pageable);

        // Convert entities to DTOs
        List<UserDto> dtoList = userPage.getElems().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

        // Create Page<UserDto> using your Page class
        return new Page<>(
                dtoList,                         // elems
                userPage.getCurrentPage(),       // currentPage
                userPage.getTotalElems(),        // totalElems
                userPage.getTotalPages()         // totalPages
        );
    }

    // Fetch user by ID
    public UserDto getUserById(Integer id) {
        User user = getEntityById(id);
        return user != null ? UserMapper.toDto(user) : null;
    }

    // Fetch user by username
    public UserDto getUserByUsername(String username) {
        User user = getEntityByUsername(username);
        return user != null ? UserMapper.toDto(user) : null;
    }

    // Fetch all users with a specific role
    public List<UserDto> getUsersByRole(String role) {
        List<User> users = repository.findAll();
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .filter(u -> role.equals(u.getRole()))
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    // Create a new user
    public UserDto create(AuthDto authDto) {
        // Map DTO -> Entity
        User user = new User();
        user.setUsername(authDto.getUsername());
        user.setPassword(authDto.getPassword());
        user.setRole(authDto.getRole());

        int saved = repository.save(user);

        if (saved == 0) {
            throw new IllegalStateException(
                    "Failed to create user. Affected rows: " + saved
            );
        }

        // If ID is generated, it must be set inside save()
        User savedUser = repository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalStateException("User was saved but not found"));

        return UserMapper.toDto(savedUser);
    }

    public void deleteById(Integer id) {
        boolean exists = repository.findById(id).isPresent();
        if (!exists) {
            throw new IllegalStateException("User with ID " + id + " does not exist");
        }

        // Perform deletion
        int deleted = repository.deleteById(id);
        if (deleted == 0) {
            throw new IllegalStateException("Failed to delete user with ID " + id);
        }
    }
}