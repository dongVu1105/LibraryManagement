package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.UserCreationRequest;
import com.dongVu1105.libraryManagement.dto.request.UserUpdateRequest;
import com.dongVu1105.libraryManagement.dto.response.UserResponse;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.mapper.UserMapper;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserMapper userMapper;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) throws AppException {
        User user = userMapper.toUser(request);
        log.info(request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        try{
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUser (){
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUserById (String id) throws AppException {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser (String id ,UserUpdateRequest request) throws AppException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getUserByUsername (String username) throws AppException {
        return userMapper.toUserResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public void deleteUser (String id){
        userRepository.deleteById(id);
    }
}
