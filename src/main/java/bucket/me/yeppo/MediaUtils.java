package bucket.me.yeppo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

public class MediaUtils {

	private static Map<String, String> imageMap;
	private static Map<String, String> documentMap;

	static {

		imageMap = new HashMap<String, String>();
		imageMap.put("JPG", MediaType.IMAGE_JPEG_VALUE);
		imageMap.put("GIF", MediaType.IMAGE_GIF_VALUE);
		imageMap.put("PNG", MediaType.IMAGE_PNG_VALUE);

		documentMap = new HashMap<String, String>();
		documentMap.put("TXT", MediaType.TEXT_PLAIN_VALUE);
		documentMap.put("HTML", MediaType.TEXT_HTML_VALUE);
		documentMap.put("TEXT", MediaType.TEXT_PLAIN_VALUE);
		documentMap.put("DOC", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("DOCX", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("XLSX", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("XLS", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("PPTX", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("PPT", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("HWP", MediaType.MULTIPART_FORM_DATA_VALUE);
		documentMap.put("PDF", MediaType.APPLICATION_PDF_VALUE);
	}

	public static String getImageType(String type) {
		return imageMap.get(type.toUpperCase());
	}

	public static String getDocumentType(String type) {
		return documentMap.get(type.toUpperCase());
	}
	
	public static boolean isImage(String type){
		boolean flag = false;
		if(getImageType(type) != null){
			flag = true;
		}else{
			if(imageMap.containsValue(type)){
				flag = true;
			}
		}
		return flag;
	}
	
	public static boolean isDocument(String type){
		boolean flag = false;
		if(getDocumentType(type) != null){
			flag = true;
		}else{
			if(documentMap.containsValue(type)){
				flag = true;
			}
		}
		return flag;
	}
	

	public static String checkType(String type){
		String returnType = null;
		if (isImage(type)){
			returnType = "image";
		}else if(isDocument(type)){
			returnType = "document";
		}else{
			returnType = "etc";
		};
		return returnType;
	}
}
