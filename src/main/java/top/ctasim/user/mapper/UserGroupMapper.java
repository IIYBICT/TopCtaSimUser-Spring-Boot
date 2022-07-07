package top.ctasim.user.mapper;
import org.apache.ibatis.annotations.Param;

import top.ctasim.user.entity.UserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author iiybict
* @description 针对表【user_group】的数据库操作Mapper
* @createDate 2022-07-03 16:19:29
* @Entity top.ctasim.user.entity.UserGroup
*/
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    UserGroup selectOneById(@Param("id") Integer id);

}




