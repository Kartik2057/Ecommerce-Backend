package com.kartik.Ecommerce.services;

import com.kartik.Ecommerce.exception.UserException;
import com.kartik.Ecommerce.model.User;



public interface UserService {

    public User findUserById(Long userId)throws UserException;

    public User findUserProfileByJwt(String jwt)throws UserException;
}
