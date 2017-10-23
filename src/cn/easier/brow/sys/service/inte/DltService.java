package cn.easier.brow.sys.service.inte;

import cn.easier.brow.sys.entity.Dlt;

public interface DltService {

	Dlt selectByKey(String key);

	int updateDlt(String key,String reason,Integer data);
}
