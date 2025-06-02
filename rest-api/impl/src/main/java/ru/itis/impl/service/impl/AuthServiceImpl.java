package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.user.UserLoginRequest;
import ru.itis.dto.request.user.UserRegisterRequest;
import ru.itis.dto.response.security.JwtResponse;
import ru.itis.impl.exception.AuthServiceException;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.security.JwtTokenUtil;
import ru.itis.impl.security.UserDetailsImpl;
import ru.itis.impl.service.AuthService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public ResponseEntity<?> login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new AuthServiceException("Пользователь не аутентифицирован", HttpStatus.UNAUTHORIZED);
        }

        if (auth.getPrincipal() instanceof UserDetailsImpl details) {
            return details.getUser().getId();
        } else if (auth.getPrincipal() instanceof String username) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return ((UserDetailsImpl) userDetails).getUser().getId();
        }

        throw new AuthServiceException("Неподдерживаемый тип аутентификации", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}