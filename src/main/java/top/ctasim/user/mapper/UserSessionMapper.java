package top.ctasim.user.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import top.ctasim.user.entity.UserSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author iiybict
* @description 针对表【user_session】的数据库操作Mapper
* @createDate 2022-07-03 11:44:52
* @Entity top.ctasim.user.entity.UserSession
*/
public interface UserSessionMapper extends BaseMapper<UserSession> {

    int delByUsername(@Param("username") String username);

    List<UserSession> findAllByToken(@Param("token") String token);

    UserSession selectOneByToken(@Param("token") String token);

}




