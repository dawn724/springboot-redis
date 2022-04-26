package com.wdh.springbootredis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@Slf4j
public class DoDeskillService {

    private final RedisTemplate<String,String> redisTemplate;

    public DoDeskillService(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDeskill(String userId,String goodId){
        // 检验参数的合法性
        if (!StringUtils.hasLength(userId) && !StringUtils.hasLength(goodId)) {
            log.info("参数不合法！");
            return false;
        }
        // 拼接需要的key
        String inventoryKey = "sk:" + goodId + ":inventory";
        String usrSetKey = "sk:" + goodId + ":usr";
        // 查看是否已经秒杀过了
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(usrSetKey, userId))) {
            log.info("你已经抢过了！");
            return false;
        }
        // 查看有没有库存
        if (Integer.parseInt((redisTemplate.opsForValue().get(inventoryKey))) < 1) {
            log.info("没有货了！");
            return false;
        }
        // 抢到了
        redisTemplate.execute(new SessionCallback<String>() {
            @Override
            public <K, V> String execute(RedisOperations<K, V> operations) throws DataAccessException {
                redisTemplate.watch(inventoryKey);
                redisTemplate.opsForValue().decrement(inventoryKey);
                redisTemplate.opsForSet().add(usrSetKey,userId);
                return "ok";
            };
        });
        log.info("抢到了！");
        return true;
    }
}
