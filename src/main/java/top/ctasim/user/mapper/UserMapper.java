package top.ctasim.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.ctasim.user.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @author iiybict
 * @description 针对表【users】的数据库操作Mapper
 * @createDate 2022-07-03 09:55:25
 * @Entity top.ctasim.user.entity.User
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> selectAllByUserCall(@Param("userCall") String userCall);

    List<User> selectAllByEmail(@Param("email") String email);

    List<User> selectAllByUsername(@Param("username") String username);

    User selectOneByUsername(@Param("username") String username);

    User selectOneByEmail(@Param("email") String email);

    User selectOneByUserCall(@Param("userCall") String userCall);

    int updatePasswordByUsername(@Param("password") String password, @Param("username") String username);

    int updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

    int updateLastLoginTimeByUsername(@Param("lastLoginTime") Date lastLoginTime, @Param("username") String username);

    int updateGroupIdByUsername(@Param("groupId") Integer groupId, @Param("username") String username);

    User selectOneById(@Param("id") Integer id);

    @Override
    int insert(User entity);
}




