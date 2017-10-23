package cn.easier.brow.sys.db.mapper;

import cn.easier.brow.sys.entity.Dlt;

public interface DltMapper {

	Dlt selectByPrimaryKey(String openid);

	int updateIntegral(Dlt dlt);
	
	int insertSelective(Dlt dlt);

}
