package com.kariuki.ecommerce.product;

import com.kariuki.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    public Integer createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        return repository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
       var productIds = request.stream()
               .map(ProductPurchaseRequest::productId)
               .toList();
       var storedProducts = repository.findAllById(productIds);
       if (productIds.size() != storedProducts.size()) {
           throw new ProductPurchaseException("One or more products not found!");
       }
       var storedRequest = request.stream()
               .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
               .toList();
       var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
       for(int i = 0; i < storedProducts.size(); i++) {
           var product = storedProducts.get(i);
           var requestedProduct = storedRequest.get(i);
           if (product.getAvailableQuantity() < requestedProduct.quantity()){
               throw new ProductPurchaseException("Insufficient quantity for product with id " + requestedProduct.productId());

           }
           product.setAvailableQuantity(product.getAvailableQuantity() - requestedProduct.quantity());
           repository.save(product);
           purchasedProducts.add(mapper.toProductPurchaseResponse(product, requestedProduct.quantity()));
       }
        return purchasedProducts;
    }

    public ProductResponse getProductById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID:: " + productId + " not found"));
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
}
