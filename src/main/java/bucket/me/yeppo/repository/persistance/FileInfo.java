package bucket.me.yeppo.repository.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author shkim
 *
 */
@Data
@Entity
public class FileInfo {
	@Id
	@GeneratedValue
	private Long id;
	private String oriName;
	private String realName;
	private String ext;
	private String contentType;
	private String path;
	private int groupId;
	private int groupType;
	private String size;
	@Column(insertable = false)
	private Date regDate;
	private int state;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriName() {
		return oriName;
	}

	public void setOriName(String oriName) {
		this.oriName = oriName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupType() {
		return groupType;
	}

	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public FileInfo() {
	}

	public FileInfo(String oriName, String realName, String ext, String path, int groupId, int groupType, String size,
			Date regDate, int state) {
		this.oriName = oriName;
		this.realName = realName;
		this.ext = ext;
		this.path = path;
		this.groupId = groupId;
		this.groupType = groupType;
		this.size = size;
		this.regDate = regDate;
		this.state = state;
	}
}
