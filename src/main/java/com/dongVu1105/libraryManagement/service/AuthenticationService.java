package com.dongVu1105.libraryManagement.service;

import com.dongVu1105.libraryManagement.dto.request.AuthenticateRequest;
import com.dongVu1105.libraryManagement.dto.request.IntrospectRequest;
import com.dongVu1105.libraryManagement.dto.request.LogoutRequest;
import com.dongVu1105.libraryManagement.dto.request.RefreshTokenRequest;
import com.dongVu1105.libraryManagement.dto.response.AuthenticateResponse;
import com.dongVu1105.libraryManagement.dto.response.IntrospectResponse;
import com.dongVu1105.libraryManagement.entity.InvalidatedToken;
import com.dongVu1105.libraryManagement.entity.User;
import com.dongVu1105.libraryManagement.exception.AppException;
import com.dongVu1105.libraryManagement.exception.ErrorCode;
import com.dongVu1105.libraryManagement.repository.InvalidatedTokenRepository;
import com.dongVu1105.libraryManagement.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;


    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    public AuthenticateResponse authenticate (AuthenticateRequest request) throws AppException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = generateToken(user);
        return AuthenticateResponse.builder()
                .valid(true)
                .token(token).build();
    }

    public IntrospectResponse introspect (IntrospectRequest request) throws AppException, ParseException, JOSEException {
        // Thay vì ném thẳng ngoại lệ lên client, exception được gói thành isValid
        //  Bỏ try catch sẽ ném thẳng ngoại lệ lên client => bỏ dòng if "thừa thãi", đảm bảo hoạt động như cũ nhưng hình như sai nguyên tắc
        // cách 1: Có thể dùng introspect() ở nơi khác (gọi riêng từ client), Muốn báo lỗi chi tiết hơn (ví dụ “token hết hạn”, “token bị thu hồi”, “token giả mạo”), dễ mở rông
        // cách 2: Chỉ dùng introspect trong CustomJwtDecoder, Ưu tiên hiệu năng và mã gọn gàng hơn,Nếu có nhiều nơi gọi introspect() mà mỗi nơi muốn xử lý lỗi khác nhau => khó mở rộng.
        boolean isValid = true;
        try{
            verifyToken(request.getToken(), false);
        } catch (AppException e){
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public void logout (LogoutRequest request) throws AppException, ParseException, JOSEException {
        // Cho dù token thế nào, user cũng được đăng xuất
        // Khi có try/catch, appException bị "nuốt" trong catch, nên ứng dụng ko báo lỗi => logout thành công
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true); // Nếu là false => trả lỗi => ko thể logout => ko ghi blacklist => hacker dùng vân đc
            // Việc cộng thêm refreshableTime giúp logout thành công, neu cả refreshableTime cũng quá hạn =>ko thể logout => ko ảnh hưởng do token đó đã không refresh đc nữa
            // Logout mục tiêu là chặn token trước khi nó hết hạn, hoặc trong khoảng cho phép refresh, để tránh bị dùng lại trong thời gian còn hiệu lực
            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            invalidatedTokenRepository.save(InvalidatedToken.builder().id(jit).expiredTime(expireTime).build());
        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    // Chỉ token hợp lệ mới được refresh
    // sẽ trả về lỗi cho client -> refresh ko thành công
    public AuthenticateResponse refreshToken (RefreshTokenRequest request) throws AppException, ParseException, JOSEException{
        SignedJWT signedJWT = verifyToken(request.getToken(), true);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(InvalidatedToken.builder().id(jit).expiredTime(expiredTime).build());
        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        String token = generateToken(user);
        return AuthenticateResponse.builder().valid(true).token(token).build();
    }

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("dong.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException, AppException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
//        if (!CollectionUtils.isEmpty(user.getRoles()))
//            user.getRoles().forEach(role -> {
//                stringJoiner.add("ROLE_" + role.getName());
//                if (!CollectionUtils.isEmpty(role.getPermissions()))
//                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
//            });
        return stringJoiner.toString();
    }

}
