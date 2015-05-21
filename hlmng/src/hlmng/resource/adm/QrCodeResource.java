package hlmng.resource.adm;

import hlmng.dao.GenDao;
import hlmng.dao.GenDaoLoader;
import hlmng.model.QrCode;
import hlmng.resource.Resource;
import hlmng.resource.ResourceHelper;
import hlmng.resource.TimeHelper;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import log.Log;
import settings.HLMNGSettings;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;



@Path(HLMNGSettings.admURL+"/qrcode")
public class QrCodeResource extends Resource {
	
	private static GenDao qrCodeDao =GenDaoLoader.instance.getQrCodeDao();
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Object> getQrCode() throws IOException {
		return listResource(qrCodeDao, false);
	}
	
	@GET
	@Path("/lastupdate")
	@Produces(MediaType.TEXT_PLAIN)
	public long getLastUpdateTime() throws IOException {
		return qrCodeDao.getLastUpdateTime();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public QrCode getQrCode(@PathParam("id") int id) throws IOException{
		return (QrCode)getResource(qrCodeDao, id);
	}

	@GET
	@Path("{id}/render")
	@Produces("image/png")
	public Response getQrCodeRendered(@PathParam("id") int id) throws IOException{
		QrCode qrCode = ((QrCode)getResource(qrCodeDao, id));
		if(qrCode!=null){
			try {
				String filePath = generateQR(qrCode.getQrcodeID(),qrCode.getPayload());
				return ResourceHelper.mediaResponse(filePath, "png", request);			
			} catch (WriterException e) {
				response.sendError(500);
			} 			
		}
		return null;
	} 
	

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response putQrCode(QrCode element,@PathParam("id") int id) throws IOException {
		return putResource(qrCodeDao, element, id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object postQrCode(QrCode element) throws IOException {		
		element.setCreatedAt(TimeHelper.getCurrentDateTime());
		String payload= element.getRole()+"-"+element.getEventIDFK()+"-"+ResourceHelper.getSecret();
		element.setPayload(payload);
		return postResource(qrCodeDao, element);
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteQrCode(@PathParam("id") int id) throws IOException {
		
		QrCode qrcode = (QrCode)qrCodeDao.getElement(id);
		if(qrcode!=null){
			deleteQRFromFS(id);			
		}
		return deleteResource(qrCodeDao, id);
	}

	
	private void deleteQRFromFS(int id) {
		String qrPath= HLMNGSettings.qrFileRootDir+Integer.toString(id)+".png";
		File file = new File(qrPath);
		boolean filedelres = file.delete();
		Log.addEntry(Level.FINE, "Removed media from file system? "+filedelres);
	}
	
	// parts of the following method are from: http://stackoverflow.com/a/7756956
	public String generateQR(int id,String payload) throws WriterException, IOException{
	    Charset charset = Charset.forName("UTF-8");
	    CharsetEncoder encoder = charset.newEncoder();
        java.nio.file.Path filePath= Paths.get(HLMNGSettings.qrFileRootDir+Integer.toString(id)+".png");
        
        File f = new File(filePath.toString());
        if(f.exists() && !f.isDirectory()) { Log.addEntry(Level.INFO, "Already rendered QR Code with ID: "+id);  return filePath.toString(); }
         
	    byte[] b = null;
	   
	   // Convert a string to UTF-8 bytes in a ByteBuffer
	   ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(payload));
	   b = bbuf.array();

	   String data;
	   data = new String(b, "UTF-8");
	   BitMatrix matrix = null;

	   com.google.zxing.Writer writer = new MultiFormatWriter();
	   Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
	   hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	   matrix = writer.encode(data,
	   com.google.zxing.BarcodeFormat.QR_CODE, HLMNGSettings.qrCodeWidth, HLMNGSettings.qrCodeHeight, hints);
	   MatrixToImageWriter.writeToPath(matrix, "PNG", filePath);
	   
	   Log.addEntry(Level.INFO, "Rendered QR Code with ID: "+id);
	   return filePath.toString();
	}



}

