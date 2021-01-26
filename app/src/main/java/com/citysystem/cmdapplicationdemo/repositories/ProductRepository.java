package com.citysystem.cmdapplicationdemo.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.citysystem.cmdapplicationdemo.db.daos.ProductDao;
import com.citysystem.cmdapplicationdemo.db.room.CmdDataBase;
import com.citysystem.cmdapplicationdemo.entities.Brand;
import com.citysystem.cmdapplicationdemo.entities.Category;
import com.citysystem.cmdapplicationdemo.entities.Product;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.citysystem.cmdapplicationdemo.utils.Constants.IS_FAVORITE;
import static com.citysystem.cmdapplicationdemo.utils.Constants.IS_PROMO;

public class ProductRepository {


    private ProductDao productDao;
    private LiveData<List<Brand>> allBrands;
    private LiveData<List<Category>> allCategories;

    public ProductRepository(Application application) {
        CmdDataBase database = CmdDataBase.getInstance(application);
        productDao = database.productDao();
        allBrands = productDao.getAllBrands();
        allCategories = productDao.getAllCategorie();
    }

    // BRAND GETS

    public LiveData<List<Brand>> getAllBrands() {
        return allBrands;
    }

    // CATEGORY GETS

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    // PRODUCT GETS

    public List<Product> getAllProducts() {
        try {
            return new getAllProductsAsyncTask(productDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsPromo() {
        try {
            return new getProductsPromoAsyncTask(productDao).execute(IS_PROMO).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsFavorites() {
        try {
            return new getProductsFavoritesAsyncTask(productDao).execute(IS_FAVORITE).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Product>> getProductsByBrand(long brandId) {
        return productDao.getProductsByBrand(brandId);
    }

    public LiveData<List<Product>> getProductsByCategory(long categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public void insertProduct(Product product) {
        new InsertProductAsyncTask(productDao).execute(product);
    }


    private static class getProductsFavoritesAsyncTask extends AsyncTask<Boolean, Void, List<Product>> {
        private ProductDao productDao;

        private getProductsFavoritesAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected List<Product> doInBackground(Boolean... booleans) {
            return productDao.getProductsFavorites(booleans[0]);
        }
    }

    private static class getProductsPromoAsyncTask extends AsyncTask<Boolean, Void, List<Product>> {
        private ProductDao productDao;

        private getProductsPromoAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected List<Product> doInBackground(Boolean... booleans) {
            return productDao.getProductsPromo(booleans[0]);
        }
    }

    private static class getAllProductsAsyncTask extends AsyncTask<Void, Void, List<Product>> {
        private ProductDao productDao;

        private getAllProductsAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {

            return productDao.getAllProducts();
        }
    }

    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insertProduct(products[0]);
            return null;
        }
    }

}
