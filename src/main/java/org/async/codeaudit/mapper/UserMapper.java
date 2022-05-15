package org.async.codeaudit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.async.codeaudit.entiy.User;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    @Select("SELECT * FROM user WHERE uid=#{uid}")
    User queryUserByUid(String uid);
    @Select("SELECT * FROM user WHERE uid=#{uid} AND password=#{password}")
    User userLogin(String uid, String password);
    @Insert("INSERT INTO user(uid, userName, password, ceratTime, socialAdress) " +
            "VALUES (#{uid},#{userName},#{password}, #{ceratTime}, #{socialAdress})")
    Integer insertUser(User user);
    @Update("UPDATE user SET userName=#{userName},password=#{password},ceratTime=#{ceratTime},socialAdress=#{socialAdress} WHERE " +
            "uid=#{uid}")
    Integer updateUser(User user);
}
