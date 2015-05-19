package com.boxtoeat.services;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;


@Path("/uploadKitchenPics")
public class ChefKitchenUploads {

	
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadMultipart(FormDataMultiPart multiPart) throws IOException{        
	    List<FormDataBodyPart> fields = multiPart.getFields("kitchenPics");   
	    int i=1;
	    for(FormDataBodyPart field : fields){
	    	String fileName=field.getContentDisposition().getFileName();
	        handleInputStream(field.getValueAs(InputStream.class),i,fileName);
	        i++;
	    }
	    //prepare the response
	    return null;
	}

	private void handleInputStream(InputStream uploadedInputStream,int i,String fileName){
		
	String uploadedFileLocation="D:/uploadedfile"+i+fileName;
	System.out.println(uploadedFileLocation);

			try {
				OutputStream out = new FileOutputStream(new File(
						uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	    //read the stream any way you want
	
	
	
	

}
