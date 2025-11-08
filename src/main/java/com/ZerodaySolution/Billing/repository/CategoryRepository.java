package com.ZerodaySolution.Billing.repository;

import com.ZerodaySolution.Billing.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository< CategoryEntity,Long> {
    //select * from category where profileId = ?1
   List<CategoryEntity> findByProfileId(Long profileId);

    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);

    List<CategoryEntity> findByTypeAndProfileId(String type,Long id);

   Boolean existsByNameAndProfileId (String name,Long profileId);
}
