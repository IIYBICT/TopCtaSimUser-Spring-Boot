package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.UserRail;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【user_rail】的数据库操作Service
 * @createDate 2022-07-08 09:34:29
 */
public interface UserRailService extends IService<UserRail> {

    UserRail findOneByUsername(String username);

    List<UserRail> findAllByUsername(String username);

    List<UserRail> findAllByRailName(String railName);
}
