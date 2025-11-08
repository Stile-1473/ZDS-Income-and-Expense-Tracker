package com.ZerodaySolution.Billing.repository;

import com.ZerodaySolution.Billing.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Long>{


        //Select from * from table_profile WHERE email = ?
    Optional<ProfileEntity> findByEmail(String email);


    //Select from * from table_profile WHERE token = ?

    //Optional<ProfileEntity> findByActivationToken(String activationToken);

}
