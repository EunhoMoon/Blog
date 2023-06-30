package com.blog.service;

import com.blog.exception.InvalidSignInInformation;
import com.blog.repository.UserRepository;
import com.blog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signIn(Login request) {
        var findedUser = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
            .orElseThrow(InvalidSignInInformation::new);
        var session = findedUser.addSession();
        return session.getAccessToken();
    }

}
