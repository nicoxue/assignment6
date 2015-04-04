/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.ProductList;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author c0640916
 */
@Path("/product")
public class ProductREST {
    
    @Inject
    ProductList productList;
    
    @GET
    @Produces("application/json")
    public Response getAll(){
        return Response.ok(productList.toJSON()).build();
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") int id){
        return null;
    }    
    
    @POST
    @Consumes("application/json")
    public Response add(JsonObject Json){
        return null;
    }    
    
    @PUT
    @Path("{id}")
    @Produces("application/json")
    public Response set(@PathParam("id") int id, JsonObject Json){
        return null;
    }   
    
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") int id){
        return null;
    } 
}
