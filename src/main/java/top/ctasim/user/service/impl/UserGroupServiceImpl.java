package top.ctasim.user.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ctasim.user.entity.UserGroup;
import top.ctasim.user.mapper.UserGroupMapper;
import top.ctasim.user.service.UserGroupService;

import javax.annotation.Resource;

/**
 * @author iiybict
 * @description 针对表【user_group】的数据库操作Service实现
 * @createDate 2022-07-03 16:19:29
 */
@Service
@DS("mysql1")
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup>
        implements UserGroupService {

    @Resource
    private UserGroupMapper userGroupMapper;

    @Override
    public UserGroup selectOneById(Integer id) {
        return userGroupMapper.selectOneById(id);
    }
}




