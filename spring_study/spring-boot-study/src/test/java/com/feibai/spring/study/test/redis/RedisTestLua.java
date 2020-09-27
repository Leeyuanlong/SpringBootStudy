package com.feibai.spring.study.test.redis;

import com.feibai.spring.study.SpringbootStudyStartup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;

/**
 * Spring Hash测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootStudyStartup.class)
@Slf4j
public class RedisTestLua {

  @Resource(name = "redisScriptLock")
  private RedisScript lockScript;

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Test
  public void testLua() {
    Object lock = redisTemplate.execute(lockScript, Collections.singletonList("test.distribute.lock"), "0");
    if (Objects.isNull(lock)) {
      log.warn("");
    }
  }

}