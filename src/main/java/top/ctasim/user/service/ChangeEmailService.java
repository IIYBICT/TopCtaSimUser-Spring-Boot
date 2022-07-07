package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.ChangeEmail;

/**
 * @author iiybict
 * @description 针对表【change_email】的数据库操作Service
 * @createDate 2022-07-04 10:14:46
 */
public interface ChangeEmailService extends IService<ChangeEmail> {

    public ChangeEmail selectOneByEmail(String email);

    public ChangeEmail selectOneByUsername(String username);

    public int delByEmail(String email);

    public int delByUsername(String username);

    ChangeEmail selectOneBySjs(String sjs);
}
