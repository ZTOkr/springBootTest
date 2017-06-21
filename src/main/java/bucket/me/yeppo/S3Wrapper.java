package bucket.me.yeppo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import bucket.me.yeppo.repository.persistance.FileInfo;
import bucket.me.yeppo.repository.persistance.FileInfoRepository;

@Service
public class S3Wrapper {

	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private FileInfoRepository fire;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	

	private PutObjectResult upload(String filePath, String uploadKey) throws FileNotFoundException {
		return upload(new FileInputStream(filePath), uploadKey);
	}
	
	private PutObjectResult upload(InputStream inputStream, String uploadKey) {
		return upload(inputStream, uploadKey, new ObjectMetadata());
	}

	private PutObjectResult upload(InputStream inputStream, String uploadKey, ObjectMetadata metaData) {
		
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, metaData);

		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

		PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

		IOUtils.closeQuietly(inputStream);

		return putObjectResult;
	}

	public List<PutObjectResult> upload(MultipartFile[] multipartFiles) {
		List<PutObjectResult> putObjectResults = new ArrayList<>();

		Arrays.stream(multipartFiles)
				.filter(multipartFile -> !StringUtils.isEmpty(multipartFile.getOriginalFilename()))
				.forEach(multipartFile -> {
					try {
						
						//DB 저장 설정
						long fileSize = multipartFile.getSize();
						String contentType = multipartFile.getContentType();
						String oriName = multipartFile.getOriginalFilename();
						String ext = oriName.substring(oriName.lastIndexOf(".") + 1).toLowerCase();
						// 업로드 날짜를 경로로 설정
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
						String datePath = sdf.format(new Date());
						String savePath = MediaUtils.checkType(contentType) + "/" + datePath;
						String realName = UUID.randomUUID().toString();
						FileInfo fileInfo = new FileInfo();
						ObjectMetadata metadata = new ObjectMetadata();
						metadata.setContentDisposition("attachment; filename=" + oriName);
						metadata.setContentLength(fileSize);
						metadata.setContentType(contentType);
						
						fileInfo.setOriName(oriName);
						fileInfo.setGroupId(0);
						fileInfo.setGroupType(0);
						fileInfo.setPath(savePath);
						fileInfo.setRealName(realName);
						fileInfo.setContentType(contentType);
						fileInfo.setExt(ext);
						fileInfo.setSize(String.valueOf(fileSize));
						fileInfo.setState(1);
						
						fire.save(fileInfo);
						
						putObjectResults.add(upload(multipartFile.getInputStream(), realName, metadata));
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

		return putObjectResults;
	}

	public ResponseEntity<byte[]> download(String key) throws IOException {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, key);

		S3Object s3Object = amazonS3.getObject(getObjectRequest);

		S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

		byte[] bytes = IOUtils.toByteArray(objectInputStream);

		FileInfo fileInfo = fire.findByRealName(key);
//		String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");

		HttpHeaders httpHeaders = new HttpHeaders();
		
		httpHeaders.setContentType(MediaType.valueOf(fileInfo.getContentType()));
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", fileInfo.getOriName());

		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}

	public List<S3ObjectSummary> list() {
		ObjectListing objectListing = amazonS3.listObjects(new ListObjectsRequest().withBucketName(bucket));

		List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

		return s3ObjectSummaries;
	}
}