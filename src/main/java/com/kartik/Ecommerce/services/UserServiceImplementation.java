package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.config.JwtProvider;
import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.User;
import com.kartik.Ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException("user not found with -id"+userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findUserByEmail(email);
        if(user==null)
            throw new UserException("User not found with email "+email);
        return user;
    }
}
