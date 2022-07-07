package top.ctasim.user.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.ctasim.user.entity.ActivateEmail;
import top.ctasim.user.entity.User;
import top.ctasim.user.entity.UserSession;
import top.ctasim.user.mapper.UserMapper;
import top.ctasim.user.mapper.UserSessionMapper;
import top.ctasim.user.service.ActivateEmailService;
import top.ctasim.user.service.UserService;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author iiybict
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2022-07-03 09:55:25
 */
@Service
@DS("mysql1")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    TemplateEngine templateEngine;
    @Resource
    private UserMapper userMapper;
    @Value("${config.rootPath}")
    private String rootPath;

    @Resource
    private ActivateEmailService activateEmailService;

    @Resource
    private UserSessionMapper userSessionMapper;

    @Value("${config.activateUrl}")
    private String activateUrl;

    @Override
    public List<User> selectAllByCall(String call) {
        return userMapper.selectAllByUserCall(call);
    }

    @Override
    public List<User> selectAllByUserName(String username) {
        return userMapper.selectAllByUsername(username);
    }

    @Override
    public List<User> selectAllByEmail(String email) {
        return userMapper.selectAllByEmail(email);
    }


    @Override
    public boolean selectUserLoginExpireByUserIdAndToken(String token) {
        UserSession userSession = userSessionMapper.selectOneByToken(token);
        if (userSession == null) {
            return false;
        }
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return userSession.getExpireTime().after(calendar.getTime());
    }

    @Override
    public boolean AddUser(String call, String username, String password, String email, String qq) {
        FileAppender appender = new FileAppender(FileUtil.file(rootPath + "conf/cert.txt"), 16, true);
        appender.append(call + " NotActive 0");
        appender.flush();
        int c = RandomUtil.randomInt(20, 50);
        String sjs = RandomUtil.randomString(c);
        ActivateEmail activateEmail = new ActivateEmail();
        Date date = new Date();
        DateTime newDate2 = DateUtil.offsetMinute(date, 15);
        activateEmail.setEmail(email);
        activateEmail.setSjs(sjs);
        activateEmail.setUsername(username);
        activateEmail.setIsActivate(0);
        activateEmail.setExpireTime(newDate2);
        activateEmailService.save(activateEmail);
        User user = new User();
        user.setUserCall(call);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGroupId(0);
        user.setLastLoginTime(null);
        user.setRegisterTime(date);
        user.setQq(qq);
        int res = userMapper.insert(user);
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                Context context = new Context();
                Map<String, Object> emailParam = new HashMap<>();
                emailParam.put("username", user.getUsername());
                emailParam.put("call", user.getUserCall());
                emailParam.put("email", user.getEmail());
                emailParam.put("url", activateUrl + sjs);
                context.setVariable("emailParam", emailParam);
                String emailTemplate = templateEngine.process("mail/ActivateEmail", context);
                MailUtil.send(email, "CTAsim用户中心邮箱验证", emailTemplate, true);
            }
        });
        return res > 0;
    }

    @Override
    public User selectOneByUsername(String username) {
        return userMapper.selectOneByUsername(username);
    }

    @Override
    public User selectOneByEmail(String email) {
        return userMapper.selectOneByEmail(email);
    }

    @Override
    public User selectOneByUserCall(String userCall) {
        return userMapper.selectOneByUserCall(userCall);
    }

    @Override
    public void updatePasswordByUsername(String username, String password) {
        userMapper.updatePasswordByUsername(username, password);
    }
}




