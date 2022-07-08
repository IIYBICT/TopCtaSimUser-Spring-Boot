package top.ctasim.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ctasim.user.entity.UserRail;
import top.ctasim.user.mapper.UserRailMapper;
import top.ctasim.user.service.UserRailService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author iiybict
 * @description 针对表【user_rail】的数据库操作Service实现
 * @createDate 2022-07-08 09:34:29
 */
@Service
public class UserRailServiceImpl extends ServiceImpl<UserRailMapper, UserRail>
        implements UserRailService {

    @Resource
    private UserRailMapper userRailMapper;

    @Override
    public UserRail findOneByUsername(String username) {
        return userRailMapper.findOneByUsername(username);
    }

    @Override
    public List<UserRail> findAllByUsername(String username) {
        return userRailMapper.findAllByUsername(username);
    }

    @Override
    public List<UserRail> findAllByRailName(String railName) {
        return userRailMapper.findAllByRailName(railName);
    }

}




