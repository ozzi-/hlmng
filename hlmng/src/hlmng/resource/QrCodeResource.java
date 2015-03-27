package hlmng.resource;

import hlmng.FileSettings;
import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.QrCode;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;



@Path("/qrcode")
public class QrCodeResource extends Resource {
	
	private GenDao qrCodeDao =GenDaoLoader.instance.getQrCodeDao();


	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getQrCode() {
		return GenDaoLoader.instance.getQrCodeDao().listElements();
	}
	
	@GET
	@Path("{id}/render")
	@Produces("image/png")
	public Response getQrCodeRendered(@PathParam("id") String id) throws IOException{
		QrCode qrCode = ((QrCode)getResource(qrCodeDao, id));
		if(!ResourceHelper.sendErrorIfNull(qrCode,response)){
			String filePath = generateQR(qrCode.getQrCodeID(),qrCode.getPayload());
			return MediaResource.mediaResponse(filePath, "png", request);			
		}
		return null;
	} 
	
	@GET
	@Path("{id}")
	public QrCode getQrCode(@PathParam("id") String id) throws IOException{
		return (QrCode)getResource(qrCodeDao, id);
	}



	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putQrCode(QrCode element,@PathParam("id") String id) throws IOException {
		return putResource(qrCodeDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postQrCode(QrCode element) throws IOException {
		return postResource(qrCodeDao, element, true);
	}
	
	// parts of the following method from http://stackoverflow.com/a/7756956
	public String generateQR(int id,String payload){
	    Charset charset = Charset.forName("UTF-8");
	    CharsetEncoder encoder = charset.newEncoder();
        java.nio.file.Path filePath= Paths.get(FileSettings.qrFileRootDir+Integer.toString(id)+".png");
        
        File f = new File(filePath.toString());
        if(f.exists() && !f.isDirectory()) { Log.addEntry(Level.INFO, "Already rendered QR Code with ID: "+id);  return filePath.toString(); }
         
	    byte[] b = null;
	    try {
	        // Convert a string to UTF-8 bytes in a ByteBuffer
	        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap("HLMNG["+payload+"]HLMNG"));
	        b = bbuf.array();
	    } catch (CharacterCodingException e) {
	        System.out.println(e.getMessage());
	    }
	    String data;
	    try {
	        data = new String(b, "UTF-8");
	        BitMatrix matrix = null;
	        int h = 500;
	        int w = 500;
	        com.google.zxing.Writer writer = new MultiFormatWriter();
	        try {
	            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
	            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	            matrix = writer.encode(data,
	            com.google.zxing.BarcodeFormat.QR_CODE, w, h, hints);
	        } catch (com.google.zxing.WriterException e) {
	            System.out.println(e.getMessage());
	        }
	        try {
	        	MatrixToImageWriter.writeToPath(matrix, "PNG", filePath);
	        	Log.addEntry(Level.INFO, "Rendered QR Code with ID: "+id);
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	    } catch (UnsupportedEncodingException e) {
	        System.out.println(e.getMessage());
	    }
	    return filePath.toString();
	}



}

