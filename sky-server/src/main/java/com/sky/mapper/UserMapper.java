package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 判断是否为新用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
     User selectByOpenid(String openid);

    /**
     * 新增用户
     * @param user
     */
    void insert(User user);

    @Select("select * from user where id=#{id}")
    User getById(Long id);

    /**
     * 统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
