package top.ctasim.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.ctasim.user.entity.UserRail;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【user_rail】的数据库操作Mapper
 * @createDate 2022-07-08 09:34:29
 * @Entity top.ctasim.user.entity.UserRail
 */
public interface UserRailMapper extends BaseMapper<UserRail> {

    UserRail findOneByUsername(@Param("username") String username);

    List<UserRail> findAllByUsername(@Param("username") String username);

    List<UserRail> findAllByRailName(@Param("railName") String railName);

    int updateRailNameAndStateById(@Param("railName") String railName, @Param("state") Integer state, @Param("id") Integer id);

    @Override
    int update(UserRail entity, Wrapper<UserRail> updateWrapper);

    @Override
    int insert(UserRail entity);
}




