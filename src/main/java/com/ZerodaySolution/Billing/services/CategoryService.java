package com.ZerodaySolution.Billing.services;

import com.ZerodaySolution.Billing.dto.CategoryDTO;
import com.ZerodaySolution.Billing.entity.CategoryEntity;
import com.ZerodaySolution.Billing.entity.ProfileEntity;
import com.ZerodaySolution.Billing.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;



    //Save category

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        ProfileEntity profile =profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profile.getId())){
          throw  new  RuntimeException("Category with this name already exists");


        }


        CategoryEntity newCategoryEntity = toEntity(categoryDTO,profile);
        newCategoryEntity = categoryRepository.save(newCategoryEntity);
        return toDTO(newCategoryEntity);
    }


    //Get categories for current profile

    public List<CategoryDTO> getCategoriesForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }


    //Get categories by type for current user
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type,profile.getId());
        return categories.stream().map(this::toDTO).toList();
    }


    //Updated cayegory

    public CategoryDTO updateCategory(Long categoryId,CategoryDTO dto){
        ProfileEntity profile = profileService.getCurrentProfile();
           CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId,profile.getId())
                    .orElseThrow(() -> new RuntimeException("Category Not Accessible Or Not Found"));

           existingCategory.setName(dto.getName());
           existingCategory.setIcon(dto.getIcon());
        existingCategory.setType(dto.getType());

           existingCategory =categoryRepository.save(existingCategory);

           return toDTO(existingCategory);

    }



    //helper methods

   private CategoryEntity toEntity( CategoryDTO categoryDTO, ProfileEntity profileEntity){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .type(categoryDTO.getType())
                .profile(profileEntity)
                .build();
    }


    private CategoryDTO toDTO(CategoryEntity categoryEntity){
      return CategoryDTO.builder()
              .id(categoryEntity.getId())
              .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId(): null)
              .name(categoryEntity.getName())
              .icon(categoryEntity.getIcon())
              .type(categoryEntity.getType())
              .createdAt(categoryEntity.getCreatedAt())
              .updatedAt(categoryEntity.getUpdatedAt())


              .build();
    }




}
