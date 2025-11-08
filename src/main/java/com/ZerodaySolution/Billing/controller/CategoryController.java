package com.ZerodaySolution.Billing.controller;

import com.ZerodaySolution.Billing.dto.CategoryDTO;
import com.ZerodaySolution.Billing.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

@PostMapping("/save")
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){
    CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);

}


@GetMapping("/get")
public ResponseEntity<List<CategoryDTO>> getCategories(){
List<CategoryDTO> categories = categoryService.getCategoriesForCurrentUser();
return ResponseEntity.ok(categories);
}

@GetMapping("/{type}")
public ResponseEntity<List<CategoryDTO>> getCategoryByTypeForCurrentUser(@PathVariable String type){
    List<CategoryDTO> categories =categoryService.getCategoriesByTypeForCurrentUser(type);
    return ResponseEntity.ok(categories);

}

@PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDTO dto){

    CategoryDTO updatedCategory = categoryService.updateCategory(categoryId,dto);
    return ResponseEntity.ok(updatedCategory);
}
}
