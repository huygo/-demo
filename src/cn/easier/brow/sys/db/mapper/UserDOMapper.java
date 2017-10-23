package cn.easier.brow.sys.db.mapper;

import cn.easier.brow.sys.entity.User;

public interface UserDOMapper {

	int insertSelective(User record);

	User selectByPrimaryKey(String pkUserid);


}