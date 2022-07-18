package top.ctasim.user.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ctasim.user.entity.ActivateEmail;
import top.ctasim.user.mapper.ActivateEmailMapper;
import top.ctasim.user.service.ActivateEmailService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author iiybict
 * @description 针对表【activate_email】的数据库操作Service实现
 * @createDate 2022-07-03 10:53:31
 */
@Service
@DS("mysql1")
public class ActivateEmailServiceImpl extends ServiceImpl<ActivateEmailMapper, ActivateEmail>
        implements ActivateEmailService {

    @Resource
    private ActivateEmailMapper activateEmailMapper;

    @Override
    public ActivateEmail selectOneByEmail(String email) {
        return activateEmailMapper.selectOneByEmail(email);
    }

    @Override
    public List<ActivateEmail> selectAllBySjs(String sjs) {
        return activateEmailMapper.selectAllBySjs(sjs);
    }

    @Override
    public void ActivateEmail(String sjs) {
        activateEmailMapper.updateIsActivateBySjs(1, sjs);
    }

    @Override
    public int updateExpireTimeAndSjsByEmail(Date expireTime, String sjs, String email) {
        return activateEmailMapper.updateExpireTimeAndSjsByEmail(expireTime, sjs, email);
    }

    @Override
    public int delByEmail(String email) {
        return activateEmailMapper.delByEmail(email);
    }
}




