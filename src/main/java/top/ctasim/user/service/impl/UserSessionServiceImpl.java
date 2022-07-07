package top.ctasim.user.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.ctasim.user.entity.UserSession;
import top.ctasim.user.service.UserSessionService;
import top.ctasim.user.mapper.UserSessionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author iiybict
* @description 针对表【user_session】的数据库操作Service实现
* @createDate 2022-07-03 11:44:52
*/
@Service
@DS("mysql1")
public class UserSessionServiceImpl extends ServiceImpl<UserSessionMapper, UserSession>
    implements UserSessionService{

    @Resource
    private UserSessionMapper userSessionMapper;

    @Override
    public void delByUsername(String username) {
        userSessionMapper.delByUsername(username);
    }

    @Override
    public List<UserSession> selectAllByToken(String token) {
        return userSessionMapper.findAllByToken(token);
    }

    @Override
    public UserSession selectOneByToken(String token) {
        return userSessionMapper.selectOneByToken(token);
    }
}




