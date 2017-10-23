package cn.easier.brow.sys.service.impl;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.easier.brow.comm.util.CommonParams;
import cn.easier.brow.sys.db.mapper.DltMapper;
import cn.easier.brow.sys.db.mapper.UserDOMapper;
import cn.easier.brow.sys.entity.Dlt;
import cn.easier.brow.sys.entity.User;
import cn.easier.brow.sys.service.inte.UserServece;

@Service(value = "userServece")
public class UserServiceImpl implements UserServece {
	private final Logger log = Logger.getLogger(CommonParams.LOG_SYS);
	@Autowired
	private UserDOMapper userDo;

	@Autowired
	private DltMapper dltMapper;

	@Override
	public int insert(User user) {
		String openid = user.getPkUserid();
		log.info("这是openid："+openid);
		User newuser = userDo.selectByPrimaryKey(openid);
		if (newuser == null) {
			userDo.insertSelective(user);
		}
		Dlt dlt = dltMapper.selectByPrimaryKey(openid);
		if (dlt == null) {
			Dlt newdlt = new Dlt();
			newdlt.setOpenid(openid);
			newdlt.setUsername(user.getIdxNick());
			newdlt.setIntegral(0);
			log.info("这是newdlt："+newdlt);
			dltMapper.insertSelective(newdlt);
		}
		return 1;
	}

	@Override
	public int casePrice() {
		Random  re = new Random();
		int count = re.nextInt(25);  
        switch (count) {  
        case 0:  
        case 1:   
        case 2:  
        case 3:  
        case 4:   
        case 5:
        case 6:
        case 7:
        	return 1;
        case 8:
        case 9:
        case 10:
        	return 2;
        case 11:      	
        case 12:
        	return 3;
        case 13:
        	return 4;
        case 14: 
        	return 5;
        case 15:
        	return 6;
        case 16:
        	return 7;
        default:
        	return 1;
        }  
	}

}
