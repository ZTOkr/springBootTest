package bucket.me.yeppo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import bucket.me.yeppo.S3Wrapper;
import bucket.me.yeppo.repository.persistance.FileInfo;
import bucket.me.yeppo.repository.persistance.FileInfoRepository;

@org.springframework.stereotype.Controller
public class Controller {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private S3Wrapper s3Wrapper;

	@Autowired
	private FileInfoRepository fire;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.link}")
	private String link;

	@RequestMapping("/")
	public String home(Model model, HttpServletRequest request) {
		Map<String, String> rq = new HashMap<>();
		String awsLink = link + bucket + "/";
		rq.put("name", "김시형");
		rq.put("age", "34");
		rq.put("job", "developer");
		rq.put("path", request.getContextPath());
		rq.put("awsLink", awsLink);
		List<S3ObjectSummary> summaryList = s3Wrapper.list();
		List<FileInfo> list = new ArrayList<>();
		try {
			for (S3ObjectSummary summary : summaryList) {
				FileInfo fileInfo = fire.findByRealName(summary.getKey());
				if (fileInfo == null) {
					continue;
				} else {
					logger.info(fileInfo.toString());
					list.add(fileInfo);
				}
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
		}
		
		model.addAttribute("rq", rq);
		model.addAttribute("list", list);
		return "home";
	}

	@RequestMapping("/file/upload")
	@ResponseBody
	public List<PutObjectResult> upload(@RequestParam("files") MultipartFile[] multipartFiles) {
		return s3Wrapper.upload(multipartFiles);
	}

	@RequestMapping(value = "/file/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@RequestParam String key) throws IOException {
		return s3Wrapper.download(key);
	}

	@RequestMapping(value = "/file/list", method = RequestMethod.GET)
	public List<S3ObjectSummary> list() throws IOException {
		return s3Wrapper.list();
	}

}
