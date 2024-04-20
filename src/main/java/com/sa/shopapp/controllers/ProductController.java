package com.sa.shopapp.controllers;

import com.github.javafaker.Faker;
import com.sa.shopapp.dtos.ProductDTO;
import com.sa.shopapp.dtos.ProductImageDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.Product;
import com.sa.shopapp.models.ProductImage;
import com.sa.shopapp.response.ProductListResponse;
import com.sa.shopapp.response.ProductResponse;
import com.sa.shopapp.services.IProductService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(
            @Valid
            // @ModelAttribute("files") List<MultipartFile> files,
            @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result .hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok().body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // tách upload hình ảnh riêng
    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > 5) {
                return ResponseEntity.badRequest().body("you can upload 5 images");
            }
            List<ProductImage> productImageList = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file != null) {
                    if (file.getSize() == 0) {
                        continue;
                    }
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.badRequest().body("file too large");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.badRequest().body("file must be image");
                    }
                    String filename = storeFile(file);
                    // sau khi xong product service
                    // lưu vào bảng product img
                    ProductImage productImage = productService.createProductImage(
                            existingProduct.getId(),
                            ProductImageDTO.builder()
                                    .imageUrl(filename)
                                    .build()
                    );
                    productImageList.add(productImage);
                }
            }
            return ResponseEntity.ok().body(productImageList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // kiểm tra định dạng ảnh
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page")       int page,
            @RequestParam("limit")      int limit
    ) {
        PageRequest pageRequest = PageRequest.of (
                page, limit,
                Sort.by("createdAt").descending()
        );
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        // all page
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok("product with id " + ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDTO productDTO
    ) {
        try {
            Product updateProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok("update product " + updateProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok("product with id " + productId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/generate_fake_products")
    public ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 1000000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existsByName(productName)) {
                continue;
            }
            Float productPrice = (float)faker.number().numberBetween(10, 1000000);
            String productDescription = faker.lorem().sentence();
            Long productCategoryId = (long)faker.number().numberBetween(3, 6);

            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price(productPrice)
                    .thumbnail("")
                    .description(productDescription)
                    .categoryId(productCategoryId)
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("fake products created successfully");
    }
}
