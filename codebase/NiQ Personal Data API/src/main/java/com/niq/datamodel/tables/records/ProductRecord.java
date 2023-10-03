/*
 * This file is generated by jOOQ.
 */
package com.niq.datamodel.tables.records;


import com.niq.datamodel.tables.Product;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ProductRecord extends UpdatableRecordImpl<ProductRecord> implements Record3<String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>NIQ.PRODUCT.PRODUCT_ID</code>.
     */
    public void setProductId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>NIQ.PRODUCT.PRODUCT_ID</code>.
     */
    public String getProductId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>NIQ.PRODUCT.CATEGORY</code>.
     */
    public void setCategory(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>NIQ.PRODUCT.CATEGORY</code>.
     */
    public String getCategory() {
        return (String) get(1);
    }

    /**
     * Setter for <code>NIQ.PRODUCT.BRAND</code>.
     */
    public void setBrand(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>NIQ.PRODUCT.BRAND</code>.
     */
    public String getBrand() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Product.PRODUCT.PRODUCT_ID;
    }

    @Override
    public Field<String> field2() {
        return Product.PRODUCT.CATEGORY;
    }

    @Override
    public Field<String> field3() {
        return Product.PRODUCT.BRAND;
    }

    @Override
    public String component1() {
        return getProductId();
    }

    @Override
    public String component2() {
        return getCategory();
    }

    @Override
    public String component3() {
        return getBrand();
    }

    @Override
    public String value1() {
        return getProductId();
    }

    @Override
    public String value2() {
        return getCategory();
    }

    @Override
    public String value3() {
        return getBrand();
    }

    @Override
    public ProductRecord value1(String value) {
        setProductId(value);
        return this;
    }

    @Override
    public ProductRecord value2(String value) {
        setCategory(value);
        return this;
    }

    @Override
    public ProductRecord value3(String value) {
        setBrand(value);
        return this;
    }

    @Override
    public ProductRecord values(String value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ProductRecord
     */
    public ProductRecord() {
        super(Product.PRODUCT);
    }

    /**
     * Create a detached, initialised ProductRecord
     */
    public ProductRecord(String productId, String category, String brand) {
        super(Product.PRODUCT);

        setProductId(productId);
        setCategory(category);
        setBrand(brand);
        resetChangedOnNotNull();
    }
}
