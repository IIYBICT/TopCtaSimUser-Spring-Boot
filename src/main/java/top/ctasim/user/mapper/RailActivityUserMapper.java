package top.ctasim.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.ctasim.user.entity.RailActivityUser;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【rail_activity_user】的数据库操作Mapper
 * @createDate 2022-07-08 09:34:51
 * @Entity top.ctasim.user.entity.RailActivityUser
 */
public interface RailActivityUserMapper extends BaseMapper<RailActivityUser> {
    List<RailActivityUser> selectAllByUsername(@Param("username") String username);

    List<RailActivityUser> selectAllByActivityId(@Param("activityId") Integer activityId);

    List<RailActivityUser> selectAllByActivityIdAndRailName(@Param("activityId") Integer activityId, @Param("railName") String railName);

    int delByUsernameAndActivityId(@Param("username") String username, @Param("activityId") Integer activityId);

    int delByActivityId(@Param("activityId") Integer activityId);
}




