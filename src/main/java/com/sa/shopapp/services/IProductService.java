package com.sa.shopapp.services;

import com.sa.shopapp.dtos.ProductDTO;
import com.sa.shopapp.dtos.ProductImageDTO;
import com.sa.shopapp.exceptions.DataNotFoundException;
import com.sa.shopapp.models.Product;
import com.sa.shopapp.models.ProductImage;
import com.sa.shopapp.response.ProductResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;
}
