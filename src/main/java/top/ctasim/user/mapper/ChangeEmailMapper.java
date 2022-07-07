package top.ctasim.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.ctasim.user.entity.ChangeEmail;

/**
 * @author iiybict
 * @description 针对表【change_email】的数据库操作Mapper
 * @createDate 2022-07-04 10:14:46
 * @Entity top.ctasim.user.entity.ChangeEmail
 */
public interface ChangeEmailMapper extends BaseMapper<ChangeEmail> {

    @Override
    int insert(ChangeEmail entity);

    ChangeEmail selectOneByEmail(@Param("email") String email);

    ChangeEmail selectOneByUsername(@Param("username") String username);

    int delByEmail(@Param("email") String email);

    int delByUsername(@Param("username") String username);

    ChangeEmail selectOneBySjs(@Param("sjs") String sjs);
}




