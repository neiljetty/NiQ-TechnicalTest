package com.niq.api.personaldata;

import com.niq.datamodel.tables.records.ProductRecord;
import com.niq.datamodel.tables.records.ShopperShelfRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.*;

import static com.niq.datamodel.Tables.PRODUCT;
import static com.niq.datamodel.Tables.SHOPPER_SHELF;
import static org.jooq.impl.DSL.trueCondition;

@SpringBootApplication
@RestController
public class PersonalDataApplication {

	private static Connection connection = null;
	private static DSLContext dslContext = null;

	public static void main(String[] args) {

		try {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/NIQDB", "NIQ", "temp1");
			dslContext = DSL.using(connection, SQLDialect.ORACLE18C);

			SpringApplication.run(PersonalDataApplication.class, args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//For the internal data team...
	@PostMapping(path ="/addProducts",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addProducts(@RequestBody Product[] products) {

		for (int i = 0; i < products.length; i++) {
			Product product = products[i];

			//Add product to the database...
			ProductRecord productRecord = dslContext.newRecord(PRODUCT);
			productRecord.setProductId(product.productId());
			productRecord.setCategory(product.category());
			productRecord.setBrand(product.brand());
			productRecord.store();
		}

		return new ResponseEntity(HttpStatus.CREATED);
	}


	//For the internal data team...
	@PostMapping(path ="/addShopperShelves",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity addShopperShelves(@RequestBody ShopperShelf[] shopperShelves) {

		for (int i = 0; i < shopperShelves.length; i++) {
			ShopperShelf shopperShelf = shopperShelves[i];

			for (int n = 0; n < shopperShelf.shelf().length; n++) {
				//Add shopper shelf record to the database...
				ShopperShelfRecord shopperShelfRecord = dslContext.newRecord(SHOPPER_SHELF);
				shopperShelfRecord.setShopperId(shopperShelf.shopperId());
				shopperShelfRecord.setProductId(shopperShelf.shelf()[n].productId());
				shopperShelfRecord.setRelevancyScore(shopperShelf.shelf()[n].relevancyScore());
				shopperShelfRecord.store();
			}
		}

		return new ResponseEntity(HttpStatus.CREATED);
	}

	//For the external eCommerce server(s)...
 	@GetMapping(path ="/getShoppers",
			produces = MediaType.APPLICATION_JSON_VALUE)
    public Shopper[] getShoppers(@RequestParam(name = "productId", required = true) String productId,
											   @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
		if (limit < 1)
			return new Shopper[0];

		if (limit > 1000)
			limit = 1000;

		Result<Record1<String>> results = dslContext.select(SHOPPER_SHELF.SHOPPER_ID)
				.from(SHOPPER_SHELF)
				.where(SHOPPER_SHELF.PRODUCT_ID.eq(productId))
				.fetch();

		Shopper[] shoppers = new Shopper[results.size() > limit ? limit : results.size()];

		for (int i = 0; i < shoppers.length; i++)
			shoppers[i] = new Shopper(results.get(i).value1());

		return shoppers;
    }

	//For the external eCommerce server(s)...
	@GetMapping(path ="/getProducts",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Product[] getProducts(@RequestParam(name = "shopperId", required = true) String shopperId,
								 @RequestParam(name = "category", required = false) String category,
								 @RequestParam(name = "brand", required = false) String brand,
								 @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
		if (limit < 1)
			return new Product[0];

		if (limit > 100)
			limit = 100;

		//Build up the where clause...
		Condition condition = trueCondition();
		condition = condition.and(SHOPPER_SHELF.SHOPPER_ID.eq(shopperId));
		if (category != null)
			condition = condition.and(PRODUCT.CATEGORY.eq(category));
		if (brand != null)
			condition = condition.and(PRODUCT.BRAND.eq(brand));

		Result<Record3<String, String, String>> results = dslContext.select(PRODUCT.PRODUCT_ID, PRODUCT.CATEGORY, PRODUCT.BRAND)
				.from(PRODUCT)
				.join(SHOPPER_SHELF)
				.on(PRODUCT.PRODUCT_ID.eq(SHOPPER_SHELF.PRODUCT_ID))
				.where(condition)
				.fetch();

		Product[] products = new Product[results.size() > limit ? limit : results.size()];

		for (int i = 0; i < products.length; i++)
			products[i] = new Product(results.get(i).value1(), results.get(i).value2(), results.get(i).value3());

		return products;
	}
}
