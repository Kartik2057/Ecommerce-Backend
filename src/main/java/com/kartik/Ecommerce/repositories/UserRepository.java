package com.kartik.Ecommerce.repositories;

import com.kartik.Ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    public User findUserByEmail(String email);

}
