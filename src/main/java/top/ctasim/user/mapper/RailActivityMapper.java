package top.ctasim.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.ctasim.user.entity.RailActivity;

/**
 * @author iiybict
 * @description 针对表【rail_activity】的数据库操作Mapper
 * @createDate 2022-07-08 09:29:40
 * @Entity top.ctasim.user.entity.RailActivity
 */
public interface RailActivityMapper extends BaseMapper<RailActivity> {

    @Override
    int insert(RailActivity entity);

    int delById(@Param("id") Integer id);

    RailActivity selectOneById(@Param("id") Integer id);
    
}




