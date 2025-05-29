package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.AuthenticateRequest;
import com.dongVu1105.libraryManagement.dto.request.IntrospectRequest;
import com.dongVu1105.libraryManagement.dto.response.AuthenticateResponse;
import com.dongVu1105.libraryManagement.dto.response.IntrospectResponse;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public AuthenticateResponse authenticate (AuthenticateRequest request) throws AppException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticateResponse.builder()
                .valid(true)
                .token("").build();
    }

    public IntrospectResponse introspect (IntrospectRequest request){
        return IntrospectResponse.builder().build();
    }


}
