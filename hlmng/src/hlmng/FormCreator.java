package hlmng;

import java.lang.reflect.Field;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/forms")
public class FormCreator {

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("{name}")
  public String sayHtmlHello(@PathParam("name") String name) throws ClassNotFoundException, SQLException {
	String httpOutput=" <!DOCTYPE html> <html> <head> <title>Form to create a new resource of type "+name+"</title> </head>";
	name=name.toLowerCase();
	
	Class<?> cls = Class.forName("model."+upperCaseFirstLetter(name));
	Field[] methods = cls.getDeclaredFields();
	
	httpOutput+="<body>Form to create a new resource of type "+name+" <form action=\"../"+name+"\" method=\"POST\">";
	for (Field field : methods) {
		httpOutput+=createField(field.getName());
	}
	httpOutput+="<input type=\"submit\" value=\"Submit\" />" + "</form> </body> </html>";
	
    return httpOutput;
  }
  
  public String upperCaseFirstLetter(String str){
	  return str.substring(0,1).toUpperCase() + str.substring(1);
  }
  public String createField(String name){
	  return  "<label for=\""+name+"\">"+name+"</label> <input name=\""+name+"\" /> <br />";
  }

} 