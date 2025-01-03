package org.example.psrauth.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.psrauth.dto.user.RequestUserDTO;
import org.example.psrauth.exception.AlreadyExistsException;
import org.example.psrauth.mapper.UserMapper;
import org.example.psrauth.model.RoleType;
import org.example.psrauth.model.entity.Role;
import org.example.psrauth.model.entity.User;
import org.example.psrauth.repository.RoleRepository;
import org.example.psrauth.repository.UserRepository;
import org.example.psrauth.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public User register(RequestUserDTO userDTO) {
    
        if (userRepository.existsByUsername(userDTO.username())) {
            throw new AlreadyExistsException("Username " + userDTO.username() + " already exists");
        }

        Role role = roleRepository.findByRoleType(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Role not found"));

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    public User changeRole(String username, String newRole) {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser == null || !currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can change user roles");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role role = roleRepository.findByRoleType(RoleType.valueOf(newRole))
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

}