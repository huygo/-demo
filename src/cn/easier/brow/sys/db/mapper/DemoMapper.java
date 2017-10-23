package cn.easier.brow.sys.db.mapper;

import cn.easier.brow.sys.entity.Demo;

public interface DemoMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(Demo record);

	int insertSelective(Demo record);

	Demo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Demo record);

	int updateByPrimaryKey(Demo record);
}