/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author c0640916
 */
@ApplicationScoped
public class ProductList {

    private List<Product> productList;

    public ProductList() {
        productList = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM product";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("ProductId"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("Quantity"));
                productList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JsonArray toJSON() {
        JsonArrayBuilder json = Json.createArrayBuilder();
        for (Product p : productList) {
            json.add(p.toJSON());
        }
        return json.build();
    }

    public Product get(int productId) {
        Product result = null;
        for (int i = 0; i < productList.size() && result == null; i++) {
            Product p = productList.get(i);
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return result;
    }

    public void set(int productId, Product product) throws Exception {
        int result = doUpdate("UPDATE Product SET NAME = ?,DESCRITION=?,QUANTITY=? WHERE ProductID = ?",
                product.getName(),
                product.getDescription(),
                String.valueOf(product.getQuantity()),
                String.valueOf(productId)
        );
        if (result == 1) {
            Product o = get(productId);
            o.setName(product.getName());
            o.setDescription(product.getDescription());
            o.setQuantity(product.getQuantity());
        } else {
            throw new Exception("Error with Update");
        }
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbc = "jdbc:mysql://localhost/sample";
            String user = "root";
            String pass = "";
            conn = DriverManager.getConnection(jdbc, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    private String getResults(String query, String... params) {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sb.append(String.format("%s\t%s\t%s\n", rs.getInt("id"), rs.getString("name"), rs.getInt("age")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }
}
