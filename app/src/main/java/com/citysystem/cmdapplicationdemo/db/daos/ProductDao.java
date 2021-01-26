package com.citysystem.cmdapplicationdemo.db.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.citysystem.cmdapplicationdemo.entities.Brand;
import com.citysystem.cmdapplicationdemo.entities.Category;
import com.citysystem.cmdapplicationdemo.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {

    //BRANDS

    @Insert
    void insertBrand(Brand brand);

    @Update
    void updateBrand(Brand brand);

    @Delete
    void deleteBrand(Brand brand);


    @Query("SELECT * FROM  brand")
    LiveData<List<Brand>> getAllBrands();

    //CATERORIES

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);


    @Query("SELECT * FROM  category")
    LiveData<List<Category>> getAllCategorie();

    //PRODUCTS

    @Insert
    void insertProduct(Product product);

    @Update
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT * FROM  product")
    List<Product> getAllProducts();

    @Query("SELECT * FROM  product WHERE promo=:promo")
    List<Product> getProductsPromo(boolean promo);

    @Query("SELECT * FROM  product WHERE favorite=:favorite")
    List<Product> getProductsFavorites(boolean favorite);

    @Query("SELECT * FROM  product WHERE categoryId=:categoryId")
    LiveData<List<Product>> getProductsByCategory(long categoryId);

    @Query("SELECT * FROM  product WHERE brandId=:brandId")
    LiveData<List<Product>> getProductsByBrand(long brandId);

}
