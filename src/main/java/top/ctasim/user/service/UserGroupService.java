package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.UserGroup;

/**
 * @author iiybict
 * @description 针对表【user_group】的数据库操作Service
 * @createDate 2022-07-03 16:19:29
 */
public interface UserGroupService extends IService<UserGroup> {

    UserGroup selectOneById(Integer id);

}
