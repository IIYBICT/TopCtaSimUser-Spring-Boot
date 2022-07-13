package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.RailActivityUser;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【rail_activity_user】的数据库操作Service
 * @createDate 2022-07-08 09:34:51
 */
public interface RailActivityUserService extends IService<RailActivityUser> {

    List<RailActivityUser> selectAllByUsername(String username);

    List<RailActivityUser> selectAllByActivityId(Integer activityId);
}
