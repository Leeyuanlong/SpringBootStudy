package com.feibai.spring.study.test;

import com.feibai.spring.study.App;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring List测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class RedisTestList {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;


  public void test_push() {

  }

}
