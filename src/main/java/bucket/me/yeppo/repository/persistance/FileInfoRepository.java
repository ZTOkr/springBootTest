package bucket.me.yeppo.repository.persistance;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FileInfoRepository extends PagingAndSortingRepository<FileInfo, Long>{
	public FileInfo findByRealName(String realName);
}
