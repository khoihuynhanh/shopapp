package com.sa.shopapp.controllers;

import com.sa.shopapp.dtos.CategoryDTO;
import com.sa.shopapp.models.Category;
import com.sa.shopapp.services.CategoryService;
import com.sa.shopapp.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody
            CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if (result .hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("create successfully " + categoryDTO);
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getCategory(
            @RequestParam("page")       int page,
            @RequestParam("limit")      int limit
    ) {
        List<Category> allCategories = categoryService.getAllCategories();
        return ResponseEntity.ok(allCategories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO
    ) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("update category " + categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("delete category with id = " + id);
    }
}
