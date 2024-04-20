package com.sa.shopapp.services;

import com.sa.shopapp.dtos.ProductDTO;
import com.sa.shopapp.dtos.ProductImageDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.exceptions.InvalidParamException;
import com.sa.shopapp.models.Category;
import com.sa.shopapp.models.Product;
import com.sa.shopapp.models.ProductImage;
import com.sa.shopapp.repositories.CategoryRepository;
import com.sa.shopapp.repositories.ProductImageRepository;
import com.sa.shopapp.repositories.ProductRepository;
import com.sa.shopapp.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "cannot find category with id " + productDTO.getCategoryId()
                        ));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "cannot find category with id " + id
                        ));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository
                .findAll(pageRequest)
                .map(product -> ProductResponse.fromProduct(product));
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);

        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "cannot find category with id " + productDTO.getCategoryId()
                        ));

        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isEmpty()) {
            productRepository.delete(optionalProduct.get());
        }
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO
    ) throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "cannot find product with id = " + productImageDTO.getId()
                        ));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= 5) {
            throw new InvalidParamException("number of images mus be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }
}
