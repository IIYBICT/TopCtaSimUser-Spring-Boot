package top.ctasim.user.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ctasim.user.entity.RailActivityUser;
import top.ctasim.user.mapper.RailActivityUserMapper;
import top.ctasim.user.service.RailActivityUserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author iiybict
 * @description 针对表【rail_activity_user】的数据库操作Service实现
 * @createDate 2022-07-08 09:34:51
 */
@Service
@DS("mysql1")
public class RailActivityUserServiceImpl extends ServiceImpl<RailActivityUserMapper, RailActivityUser>
        implements RailActivityUserService {

    @Resource
    private RailActivityUserMapper railActivityUserMapper;

    @Override
    public List<RailActivityUser> selectAllByUsername(String username) {
        return railActivityUserMapper.selectAllByUsername(username);
    }

    @Override
    public List<RailActivityUser> selectAllByActivityId(Integer activityId) {
        return railActivityUserMapper.selectAllByActivityId(activityId);
    }
}




