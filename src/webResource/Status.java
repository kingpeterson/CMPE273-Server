package webResource;

import java.sql.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import data.*;

@Path("/Status")
public class Status {
	
	@Path("/returnTitle")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(){
		System.out.println("Hello");
		return "<h1>Java Web Service</h1>";
	}

}
