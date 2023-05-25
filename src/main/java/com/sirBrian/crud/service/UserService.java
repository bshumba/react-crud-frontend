package com.sirBrian.crud.service;

import com.sirBrian.crud.dto.UserDto;
import com.sirBrian.crud.entity.User;
import com.sirBrian.crud.exception.UserNotFoundException;
import com.sirBrian.crud.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Integer addUser(UserDto userDto) {
        User user = mapFromDto(userDto);
        return userRepository.save(user).getId();
    }

    public UserDto getUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + "Not Found"));
        return mapToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    public void updateUser(Integer userId, UserDto userDto) {
        User savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + "Not Found"));

        savedUser.setName(userDto.getName());
        savedUser.setUsername(userDto.getUsername());
        savedUser.setEmail(userDto.getEmail());

        userRepository.save(savedUser);
    }

    public void deleteUser(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + "Not Found"));

        userRepository.deleteById(userId);
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    private User mapFromDto(UserDto user) {
        return User.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
