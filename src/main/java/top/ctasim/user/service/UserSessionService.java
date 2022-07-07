package top.ctasim.user.service;

import top.ctasim.user.entity.UserSession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author iiybict
* @description 针对表【user_session】的数据库操作Service
* @createDate 2022-07-03 11:44:52
*/
public interface UserSessionService extends IService<UserSession> {

    void delByUsername(String username);

    List<UserSession> selectAllByToken(String token);

    UserSession selectOneByToken(String token);
}
