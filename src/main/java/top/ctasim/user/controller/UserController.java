package top.ctasim.user.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.mail.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.ctasim.user.entity.*;
import top.ctasim.user.service.*;
import top.ctasim.user.utils.MapAndEntyUtil;
import top.ctasim.user.utils.ResultJson;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Resource
    TemplateEngine templateEngine;
    @Resource
    private UserService userService;
    @Resource
    private UserSessionService userSessionService;
    @Resource
    private UserGroupService userGroupService;
    @Resource
    private ActivateEmailService activateEmailService;

    @Resource
    private ChangeEmailService changeEmailService;
    /**
     * 获取全局配置的RootPath
     */
    @Value("${config.rootPath}")
    private String rootPath;

    @Value("${config.activateUrl}")
    private String activateUrl;

    @Value("${config.changeEmailUrl}")
    private String changeEmailUrl;

    /**
     * 注册接口
     *
     * @param call     呼号
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @param qq       QQ
     * @return ResultJson.ok() / ResultJson.error()
     */
    @PostMapping("/register")
    public ResultJson register(
            @RequestParam("call") String call,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("qq") String qq) {


        FileReader fileReader = new FileReader(FileUtil.file(rootPath + "tshh.txt"));
        String result = fileReader.readString();
        String[] split = result.split("\r\n");
        for (String item : split) {
            if (call.equals(item)) {
                return ResultJson.error().message("该呼号为特殊呼号，禁止使用");
            }
        }
        if (call.length() != 4) {
            return ResultJson.error().message("呼号长度限制4位");
        }
        List<User> callList = userService.selectAllByCall(call);
        if (callList.size() > 0) {
            return ResultJson.error().message("该呼号已被注册");
        }
        List<User> userNameList = userService.selectAllByUserName(username);
        if (userNameList.size() > 0) {
            return ResultJson.error().message("该用户名已被注册");
        }
        List<User> callUserNameList = userService.selectAllByCall(username);
        if (callUserNameList.size() > 0) {
            return ResultJson.error().message("该用户名已被其他用户注册为呼号，所以该用户名无法使用");
        }
        if (!Validator.isEmail(email)) {
            return ResultJson.error().message("邮箱格式不正确");
        }
        List<User> emailList = userService.selectAllByEmail(email);
        if (emailList.size() > 0) {
            return ResultJson.error().message("该邮箱地址已被注册");
        }
        boolean res = userService.AddUser(call, username, DigestUtil.md5Hex(password), email, qq);
        return ResultJson.ok().data("isRegister", res);
    }

    /**
     * 登录接口
     *
     * @param call     可邮箱 用户名 呼号登录
     * @param password 密码
     * @return ResultJson.ok() / ResultJson.error()
     */
    @GetMapping("/login")
    public ResultJson login(
            @RequestParam("call") String call,
            @RequestParam("password") String password
    ) {
        List<User> userList = userService.selectAllByCall(call);
        if (userList.size() == 0) {
            userList = userService.selectAllByUserName(call);
            if (userList.size() == 0) {
                userList = userService.selectAllByEmail(call);
                if (userList.size() == 0) {
                    return ResultJson.error().message("该账号不存在");
                }
            }
        }
        User user = userList.get(0);
        String passwordMd5Hex = DigestUtil.md5Hex(password);
        if (!passwordMd5Hex.equals(user.getPassword())) {
            return ResultJson.error().message("密码错误");
        }
        String slat = RandomUtil.randomString(32);
        Date date = new Date();
        String token = DigestUtils.md5DigestAsHex((user.getEmail() + user.getUserCall() + slat + user.getPassword() + date.getTime()).getBytes());
        DateTime newDate2 = DateUtil.offsetDay(date, 1);
        UserSession userSession = new UserSession();
        userSession.setUsername(user.getUsername());
        userSession.setToken(token);
        userSession.setLoginTime(date);
        userSession.setExpireTime(newDate2);
        userSessionService.delByUsername(user.getUsername());
        userSessionService.save(userSession);
        return ResultJson.ok().data("token", token).data("userId", user.getId());
    }

    @GetMapping("/cert")
    public ResultJson getCert() {
        FileReader fileReader1 = new FileReader(FileUtil.file(rootPath + "conf/cert.txt"));
        String result1 = fileReader1.readString();
        FileReader fileReader2 = new FileReader(FileUtil.file(rootPath + "tshh.txt"));
        String result2 = fileReader2.readString();
        String result = result1 + result2;
        String[] split = result.split("\r\n");
        while (true) {
            String call = RandomUtil.randomNumbers(4);
            for (String item : split) {
                String[] data = item.split(" ");
                if (!data[0].equals(call)) {
                    return ResultJson.ok().data("is_cert", true).data("call", call);
                }
            }
        }
    }

    @GetMapping("/info")
    public ResultJson getUserInfo(@RequestParam("token") String token) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        Map<String, Object> data = MapAndEntyUtil.entityToMap(userData);
        UserGroup userGroup1 = userGroupService.selectOneById(userData.getGroupId());
        ActivateEmail activateEmail = activateEmailService.selectOneByEmail(userData.getEmail());

        FileReader fileReader1 = new FileReader(FileUtil.file(rootPath + "conf/cert.txt"));
        String result = fileReader1.readString();
        String[] certList = result.split("\r\n");
        FileReader ratingFileReader = new FileReader(FileUtil.file(rootPath + "rating.txt"));
        String ratingData = ratingFileReader.readString();
        String[] ratingList = ratingData.split("\r\n");
        for (String item : certList) {
            String[] itemInfo = item.split(" ");
            if (itemInfo[0].equals(userData.getUserCall())) {
//                data.put("activateCall", new Integer(itemInfo[2].trim()));
                for (String ratingItem : ratingList) {
                    String[] ratingItemInfo = ratingItem.split(" ");

                    if (ratingItemInfo[0].equals(itemInfo[2].trim())) {
                        data.put("ratingId", new Integer(ratingItemInfo[0].trim()));
                        data.put("ratingName", ratingItemInfo[2].trim());
                        data.put("ratingNameEn", ratingItemInfo[1].trim());
                    }
                }
            }

        }
//        data.putIfAbsent("activateCall", null);
        data.putIfAbsent("ratingId", null);
        data.putIfAbsent("ratingName", null);
        data.putIfAbsent("ratingNameEn", null);
        data.remove("password");
        data.put("groupName", userGroup1.getGroupName());
        data.put("IsActivate", activateEmail.getIsActivate() == 1);
        return ResultJson.ok().data(data);
    }

    /**
     * 获取所有用户信息
     *
     * @param token Token
     * @return ResultJson.ok() / ResultJson.error()
     */
    @GetMapping("/get/user/list")
    public ResultJson getUserList(
            @RequestParam("token") String token
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserGroup userGroup = userGroupService.selectOneById(userData.getGroupId());
        if (userGroup.getIsAdmin() != 1) {
            return ResultJson.error().message("权限不足");
        }
        FileReader fileReader1 = new FileReader(FileUtil.file(rootPath + "conf/cert.txt"));
        String result = fileReader1.readString();
        String[] certList = result.split("\r\n");
        List<User> userList = userService.list();
        List<UserNew> userNewList = new ArrayList<>();
        for (User user : userList) {
            UserNew userNew = new UserNew();
            userNew.setId(user.getId());
            userNew.setUserCall(user.getUserCall());
            userNew.setPassword(null);
            userNew.setUsername(user.getUsername());
            userNew.setEmail(user.getEmail());
            userNew.setQq(user.getQq());
            userNew.setGroupId(user.getGroupId());
            UserGroup userGroup1 = userGroupService.selectOneById(userData.getGroupId());
            userNew.setGroupName(userGroup1.getGroupName());
            userNew.setLastLoginTime(user.getLastLoginTime());
            userNew.setRegisterTime(user.getRegisterTime());
            ActivateEmail activateEmail = activateEmailService.selectOneByEmail(user.getEmail());
            userNew.setIsActivate(activateEmail.getIsActivate() == 1);
            for (String item : certList) {
                String[] itemInfo = item.split(" ");
                if (itemInfo[0].equals(userData.getUserCall())) {
                    userNew.setActivateCall(new Integer(itemInfo[2].trim()));
                }
            }
            userNewList.add(userNew);
        }
        return ResultJson.ok().data("size", userNewList.size()).data("data", userNewList);
    }

    /**
     * 验证邮箱接口
     *
     * @param sjs 随机数（唯一数）
     * @return ResultJson.ok() / ResultJson.error()
     */
    @PostMapping("/activate/email")
    public ResultJson ActivateEmail(
            @RequestParam(value = "sjs", required = true) String sjs
    ) {
        List<ActivateEmail> activateEmailList = activateEmailService.selectAllBySjs(sjs);
        if (activateEmailList.size() == 0) {
            return ResultJson.error().message("无效验证链接");
        }
        ActivateEmail activateEmail = activateEmailList.get(0);
        if (activateEmail.getIsActivate() == 1) {
            return ResultJson.error().message("已经验证了，无需重复验证");
        }
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (!activateEmail.getExpireTime().after(calendar.getTime())) {
            return ResultJson.error().message("验证地址已过期");
        }
        activateEmailService.ActivateEmail(sjs);
        return ResultJson.ok().data("isActivate", true);
    }


    /**
     * 发送验证邮箱邮件
     *
     * @param token Token
     * @return ResultJson.ok() / ResultJson.error()
     */
    @PostMapping("/sand/activate/email")
    public ResultJson sandActivateEmail(
            @RequestParam(value = "token", required = true) String token
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        ActivateEmail activateEmail = activateEmailService.selectOneByEmail(userData.getEmail());
//        int c = RandomUtil.randomInt(20, 50);
//        String sjs = RandomUtil.randomString(c);
        Date date = new Date();
        int slat = RandomUtil.randomInt(20, 50);
        String sjs = DigestUtils.md5DigestAsHex((userData.getEmail() + userData.getUserCall() + slat + userData.getPassword() + date.getTime()).getBytes());
        DateTime newDate2 = DateUtil.offsetMinute(date, 15);
        if (activateEmail == null) {
            ActivateEmail activateEmail1 = new ActivateEmail();
            activateEmail1.setEmail(userData.getEmail());
            activateEmail1.setSjs(sjs);
            activateEmail1.setUsername(userSession.getUsername());
            activateEmail1.setIsActivate(0);
            activateEmail1.setExpireTime(newDate2);
            activateEmailService.save(activateEmail1);
        } else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (activateEmail.getExpireTime().after(calendar.getTime())) {
                return ResultJson.error().message("已发送到您的邮箱且还在有效期内（15分钟有效期）");
            }
            activateEmailService.updateExpireTimeAndSjsByEmail(newDate2, sjs, userData.getEmail());
        }
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                Context context = new Context();
                Map<String, Object> emailParam = new HashMap<>();
                emailParam.put("username", userData.getUsername());
                emailParam.put("call", userData.getUserCall());
                emailParam.put("email", userData.getEmail());
                emailParam.put("url", activateUrl + sjs);
                context.setVariable("emailParam", emailParam);
                String emailTemplate = templateEngine.process("mail/ActivateEmail", context);
                MailUtil.send(userData.getEmail(), "CTAsim用户中心邮箱验证", emailTemplate, true);
            }
        });
        return ResultJson.ok().data("isSend", true);
    }

    /**
     * 发送找回密码邮件
     *
     * @param callOrEmail 呼号或者邮箱
     * @return ResultJson.ok() / ResultJson.error()
     */
    @PostMapping("/send/change/password/email")
    public ResultJson SendEmailPasswordEmail(
            @RequestParam(required = true) String callOrEmail
    ) {
        Date date = DateUtil.date();
        int slat = RandomUtil.randomInt(20, 50);
        User user = null;
        if (Validator.isEmail(callOrEmail)) {
            user = userService.selectOneByEmail(callOrEmail);
        } else {
            user = userService.selectOneByUserCall(callOrEmail);
        }

        if (user == null) {
            return ResultJson.error().message("用户不存在").data("isSend", false);
        }
        String sjs = DigestUtils.md5DigestAsHex((user.getEmail() + user.getUserCall() + slat + user.getPassword() + date.getTime()).getBytes());
        DateTime newDate2 = DateUtil.offset(date, DateField.MINUTE, 15);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        ChangeEmail changeEmailData = changeEmailService.selectOneByEmail(user.getEmail());
        if (changeEmailData != null && changeEmailData.getExpireTime().after(calendar.getTime())) {
            return ResultJson.error().message("已发送到您的邮箱且还在有效期内（15分钟有效期）").data("isSend", false);
        }
        changeEmailService.delByEmail(user.getEmail());
        ChangeEmail changeEmail = new ChangeEmail();
        changeEmail.setEmail(user.getEmail());
        changeEmail.setSjs(sjs);
        changeEmail.setUsername(user.getUsername());
        changeEmail.setExpireTime(newDate2);
        changeEmailService.save(changeEmail);
        User finalUser = user;
        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                Context context = new Context();
                Map<String, Object> emailParam = new HashMap<>();
                emailParam.put("username", finalUser.getUsername());
                emailParam.put("call", finalUser.getUserCall());
                emailParam.put("email", finalUser.getEmail());
                emailParam.put("url", changeEmailUrl + sjs);
                context.setVariable("emailParam", emailParam);
                String emailTemplate = templateEngine.process("mail/ChangePassWordEmail", context);
                MailUtil.send(finalUser.getEmail(), "CTAsim用户中心密码找回", emailTemplate, true);
            }
        });
        return ResultJson.ok().data("isSend", true);
    }

    /**
     * 校验邮箱并修改密码
     *
     * @param sjs      验证码
     * @param password 密码
     * @return ResultJson.ok() / ResultJson.error()
     */
    @PostMapping("/check/password/email")
    public ResultJson CheckEmailPasswordEmail(
            @RequestParam(required = true) String sjs,
            @RequestParam(required = true) String password
    ) {
        ChangeEmail changeEmail = changeEmailService.selectOneBySjs(sjs);
        if (changeEmail == null) {
            return ResultJson.error().message("无效链接").data("isUpdate", false);
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        if (changeEmail.getExpireTime().before(calendar.getTime())) {
            return ResultJson.error().message("链接已过期").data("isUpdate", false);
        }
        User user = userService.selectOneByUsername(changeEmail.getUsername());
        user.setPassword(password);
        userService.updatePasswordByUsername(user.getUsername(), user.getPassword());
        changeEmailService.delByEmail(user.getEmail());
        return ResultJson.ok().data("isUpdate", true);
    }


}
