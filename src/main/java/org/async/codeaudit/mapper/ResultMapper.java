package org.async.codeaudit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.async.codeaudit.entiy.Result;

import java.util.List;

@Mapper
public interface ResultMapper extends BaseMapper<Result> {
    @Select("SELECT * FROM result WHERE uid=#{uid}")
    public List<Result> getResultResultByUid(String uid,int start,int end);
    @Insert("INSERT INTO result(uid, result, sendTime, doTime, remarks, color) VALUES (#{uid}, #{result}, #{sendTime}, #{doTime},#{remarks}, #{color})")
    public Integer insertResult(Result result);
    @Update("UPDATE result SET result=#{result},sendTime=#{sendTime},doTime=#{doTime},remarks=#{remarks},color=#{color}")
    public Integer updateResultInteger(Result result);


}
