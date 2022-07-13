package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.User;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【users】的数据库操作Service
 * @createDate 2022-07-03 09:55:25
 */
public interface UserService extends IService<User> {

    List<User> selectAllByCall(String call);

    List<User> selectAllByUserName(String username);

    List<User> selectAllByEmail(String email);

    boolean selectUserLoginExpireByUserIdAndToken(String token);

    boolean AddUser(String call, String username, String password, String email, String qq);

    User selectOneByUsername(String username);

    User selectOneByEmail(String email);

    User selectOneByUserCall(String userCall);

    void updatePasswordByUsername(String username, String password);

    void updateLastLoginTimeByUserName(String username);
}
