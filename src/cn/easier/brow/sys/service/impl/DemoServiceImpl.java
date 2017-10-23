package cn.easier.brow.sys.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.comm.util.CommonParams;
import cn.easier.brow.sys.db.mapper.DemoMapper;
import cn.easier.brow.sys.entity.Demo;
import cn.easier.brow.sys.redis.util.RedisUtil;
import cn.easier.brow.sys.service.inte.DemoService;

@Service(value = "demoService")
public class DemoServiceImpl implements DemoService {
	private final Logger log = Logger.getLogger(CommonParams.LOG_SYS);

	@Resource(name = "redisCache")
	private RedisUtil redisCache;

	@Resource(name = "demoMapper")
	private DemoMapper demoMapper;

	//@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class, Exception.class })
	public Demo insertSelective() throws Exception {
		Demo demo = new Demo();
		//String id = StringUtil.getUUID();
		demo.setAge(20);
		demo.setGid(1);
		demo.setName("jack");
		demo.setSex(1);
		int i = demoMapper.insertSelective(demo);
		if (i > 0) {
			Integer id = demo.getId();
			log.debug("DemoServiceImpl().insertSelective() 新增成功  id=" + id);
			log.debug("setRedisString()");
			String demoJsonStr = JSONObject.toJSONString(demo);
			String str = redisCache.setString("demo_" + id, demoJsonStr, 30);
			log.debug("redisCache.setString: " + str);
		}
		return demo;
	}

}
