package top.ctasim.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ctasim.user.entity.ChangeEmail;
import top.ctasim.user.mapper.ChangeEmailMapper;
import top.ctasim.user.service.ChangeEmailService;

import javax.annotation.Resource;

/**
 * @author iiybict
 * @description 针对表【change_email】的数据库操作Service实现
 * @createDate 2022-07-04 10:14:46
 */
@Service
public class ChangeEmailServiceImpl extends ServiceImpl<ChangeEmailMapper, ChangeEmail>
        implements ChangeEmailService {

    @Resource
    private ChangeEmailMapper changeEmailMapper;

    @Override
    public ChangeEmail selectOneByEmail(String email) {
        return changeEmailMapper.selectOneByEmail(email);
    }

    @Override
    public ChangeEmail selectOneByUsername(String username) {
        return changeEmailMapper.selectOneByUsername(username);
    }

    @Override
    public int delByEmail(String email) {
        return changeEmailMapper.delByEmail(email);
    }

    @Override
    public int delByUsername(String username) {
        return changeEmailMapper.delByUsername(username);
    }

    @Override
    public ChangeEmail selectOneBySjs(String sjs) {
        return changeEmailMapper.selectOneBySjs(sjs);
    }
}




