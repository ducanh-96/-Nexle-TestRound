package com.example.test.controller;

import com.example.test.dto.requests.SignInRequestDto;
import com.example.test.dto.requests.SignUpRequestDto;
import com.example.test.entity.Token;
import com.example.test.entity.User;
import com.example.test.repository.TokenRepository;
import com.example.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestApiController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //a. Sign up
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDto signUpRequestDto){

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpRequestDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST); //400 Bad Request
        }

        // create user object
        User user = new User();
        user.setFirstName(signUpRequestDto.getFirstName());
        user.setLastName(signUpRequestDto.getLastName());
        //validate if the email in in correct email format or not
        if(!user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}")){
            return new ResponseEntity<>("Email is not in correct format!", HttpStatus.BAD_REQUEST); //400 Bad Request
        }
        user.setEmail(signUpRequestDto.getEmail());
        //Password must be between 8-20 characters
        if(signUpRequestDto.getPassword().length() < 8 || signUpRequestDto.getPassword().length() > 20){
            return new ResponseEntity<>("Password must be between 8-20 characters!", HttpStatus.BAD_REQUEST); //400 Bad Request
        }
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK); // 201 Success
    }

    //b. Sign in
    @PostMapping("/sign-in")
    public ResponseEntity<String> authenticateUser(@RequestBody SignInRequestDto signInRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDto.getEmail(), signInRequestDto.getPassword()));

        //The api validate if the email in in correct email format or not
        if(!authentication.isAuthenticated()){
            return new ResponseEntity<>("Email is not in correct format!", HttpStatus.BAD_REQUEST); //400 Bad Request
        }
        //Password must be between 8-20 characters
        if(signInRequestDto.getPassword().length() < 8 || signInRequestDto.getPassword().length() > 20){
            return new ResponseEntity<>("Password must be between 8-20 characters!", HttpStatus.BAD_REQUEST); //400 Bad Request
        }
        //The token expires in one hour
        Token token = new Token();
        token.setToken("token");
        token.setExpiresIn(String.valueOf(3600));
        tokenRepository.save(token);
        //The refreshToken expires in 30 days
        Token refreshToken = new Token();
        refreshToken.setToken("refreshToken");
        refreshToken.setExpiresIn(String.valueOf(2592000));
        tokenRepository.save(refreshToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(authentication.isAuthenticated()){
            return new ResponseEntity<>("User Sign in successfully!.", HttpStatus.OK); //200 Success
        }
        return new ResponseEntity<>("User Sign in failed!.", HttpStatus.BAD_REQUEST); //400 Bad Request
    }

    //c. Sign out
    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut() {
        // Remove all the refresh tokens belong to the user account in refreshToken table
        tokenRepository.getReferenceById(1L).setRefreshToken(null);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  //204 No Content
    }

    //d. Refresh Token
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() {
        // The token expires in one hour
        Token token = new Token();
        token.setToken("token");
        token.setExpiresIn(String.valueOf(3600));
        tokenRepository.save(token);
        //404 if the supplied refreshToken in the inbound does not exist
        if(tokenRepository.getReferenceById(1L).getRefreshToken() == null){
            return new ResponseEntity<>("Refresh token not found!", HttpStatus.NOT_FOUND); //404 Not Found
        }
        // The refreshToken expires in 30 days
        Token refreshToken = new Token();
        refreshToken.setToken("refreshToken");
        refreshToken.setExpiresIn(String.valueOf(2592000));
        tokenRepository.save(refreshToken);

        return new ResponseEntity<>("Token refreshed successfully!", HttpStatus.OK); //200 Success
    }

    // In case of an internal error, return 500 http code
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


}
