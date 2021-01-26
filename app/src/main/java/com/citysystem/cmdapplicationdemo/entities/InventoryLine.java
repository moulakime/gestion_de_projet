package com.citysystem.cmdapplicationdemo.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "inventory_line",
        foreignKeys = {
                @ForeignKey(entity = Inventory.class,
                        parentColumns = "id", childColumns = "inventoryId", onDelete = CASCADE),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId")
        }, indices = {@Index("inventoryId"), @Index("productId")})
public class InventoryLine {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long inventoryId;
    private long productId;
    private int stockQuantity;
    private int defectQuantity;

    public InventoryLine(long inventoryId, long productId, int stockQuantity, int defectQuantity) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.stockQuantity = stockQuantity;
        this.defectQuantity = defectQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInventoryId() {
        return inventoryId;
    }

    public long getProductId() {
        return productId;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getDefectQuantity() {
        return defectQuantity;
    }
}
