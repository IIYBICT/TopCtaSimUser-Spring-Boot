package top.ctasim.user.mapper;
import java.util.Date;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.ctasim.user.entity.ActivateEmail;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【activate_email】的数据库操作Mapper
 * @createDate 2022-07-03 10:53:31
 * @Entity top.ctasim.user.entity.ActivateEmail
 */
public interface ActivateEmailMapper extends BaseMapper<ActivateEmail> {

    @Override
    int insert(ActivateEmail entity);

    ActivateEmail selectOneByEmail(@Param("email") String email);

    List<ActivateEmail> selectAllBySjs(@Param("sjs") String sjs);

    int updateIsActivateBySjs(@Param("isActivate") Integer isActivate, @Param("sjs") String sjs);

    int updateExpireTimeAndSjsByEmail(@Param("expireTime") Date expireTime, @Param("sjs") String sjs, @Param("email") String email);
}




