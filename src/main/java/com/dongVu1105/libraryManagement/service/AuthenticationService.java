package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.IntrospectRequest;
import com.dongVu1105.libraryManagement.dto.response.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {
    public IntrospectResponse introspect (IntrospectRequest request){
        return IntrospectResponse.builder().build();
    }
}
