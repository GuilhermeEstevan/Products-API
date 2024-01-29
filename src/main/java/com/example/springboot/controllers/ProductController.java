package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ProductController {

    @Autowired
    ProductRepository repository;

    @PostMapping("/products")
    public ResponseEntity<ProductModel>
    saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        repository.save(productModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(productModel);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAll() {
        List<ProductModel> productModelList = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(productModelList);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = repository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id,
                                         @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> product = repository.findById(id);
        if (product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var productModel = product.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        repository.save(productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productModel);
    }

    @DeleteMapping(value = "products/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productModel = repository.findById(id);
        if (productModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        repository.delete(productModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product Deleted");
    }

}
