package cn.easier.brow.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.easier.brow.sys.db.mapper.DltMapper;
import cn.easier.brow.sys.entity.Dlt;
import cn.easier.brow.sys.service.inte.DltService;

@Service(value="dltService")
public class DltServiceImpl implements DltService {

	@Autowired
	private DltMapper dltMapper;

	@Override
	public Dlt selectByKey(String key) {

		Dlt dlt = dltMapper.selectByPrimaryKey(key);
		return dlt;
	}

	@Override
	public int updateDlt(String key,String reason,Integer data) {

		Dlt dlt = dltMapper.selectByPrimaryKey(key);
		int integral = dlt.getIntegral();
		if("add".equals(reason)){
			integral=integral+data;
			dlt.setIntegral(integral);
			dlt.setOpenid(key);
		}else{
			integral=integral-data;
			dlt.setIntegral(integral);
			dlt.setOpenid(key);
		}
		
		return dltMapper.updateIntegral(dlt);
	}

}
