package com.sky.mapper;

import com.sky.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     *
     * @param user
     */
    void save(User user);

    /**
     * 根据id查找用户
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId};")
    User getById(Long userId);
}
