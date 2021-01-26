package com.citysystem.cmdapplicationdemo.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.citysystem.cmdapplicationdemo.entities.Brand;
import com.citysystem.cmdapplicationdemo.entities.Category;
import com.citysystem.cmdapplicationdemo.entities.InvoiceLine;
import com.citysystem.cmdapplicationdemo.entities.Partner;
import com.citysystem.cmdapplicationdemo.entities.Product;
import com.citysystem.cmdapplicationdemo.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    public boolean sameInvoice = false;
    public long invoiceId;
    public List<InvoiceLine> invoiceLineList = new ArrayList<>();
    public Partner partner;
    public long partner_id = -1;
    public long partner_tariff = -1;
    private ProductRepository productRepository;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }
    // BRANDS

    public LiveData<List<Brand>> getAllBrands() {
        return productRepository.getAllBrands();
    }

    // CATEGORIES

    public LiveData<List<Category>> getAllCategories() {

        return productRepository.getAllCategories();
    }

    // Products

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public LiveData<List<Product>> getProductsByBrand(long brandId) {
        return productRepository.getProductsByBrand(brandId);
    }

    public LiveData<List<Product>> getProductsByCategory(long categoryId) {
        return productRepository.getProductsByCategory(categoryId);
    }

    public List<Product> getProductsFavorites() {
        return productRepository.getProductsFavorites();
    }

    public List<Product> getProductsPromo() {
        return productRepository.getProductsPromo();
    }

    //INSERT PRODUCTS

    public void insertProducts(Product product) {

        productRepository.insertProduct(product);

    }

}
