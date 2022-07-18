package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.ActivateEmail;

import java.util.Date;
import java.util.List;

/**
 * @author iiybict
 * @description 针对表【activate_email】的数据库操作Service
 * @createDate 2022-07-03 10:53:31
 */
public interface ActivateEmailService extends IService<ActivateEmail> {

    public ActivateEmail selectOneByEmail(String email);

    List<ActivateEmail> selectAllBySjs(String sjs);

    void ActivateEmail(String sjs);

    int updateExpireTimeAndSjsByEmail(Date expireTime, String sjs, String email);

    int delByEmail(String email);
}
