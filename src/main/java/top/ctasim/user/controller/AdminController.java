package top.ctasim.user.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.mail.MailUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import top.ctasim.user.entity.*;
import top.ctasim.user.service.ActivateEmailService;
import top.ctasim.user.service.UserGroupService;
import top.ctasim.user.service.UserService;
import top.ctasim.user.service.UserSessionService;
import top.ctasim.user.utils.MapAndEntyUtil;
import top.ctasim.user.utils.ResultJson;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author IIYBICT
 */
@RestController
@RequestMapping("/admin")
@ResponseBody
public class AdminController {

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

    @Value("${config.rootPath}")
    private String rootPath;

    @Value("${config.confPath}")
    private String confPath;

    @Value("${config.activateUrl}")
    private String activateUrl;

    @GetMapping("/get/user/list")
    public ResultJson getUserList(
            String token
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
        FileReader fileReader1 = new FileReader(FileUtil.file(confPath + "cert.txt"));
        String result = fileReader1.readString();
        String[] certList = result.split("\r\n");

        FileReader ratingFileReader = new FileReader(FileUtil.file(rootPath + "rating.txt"));
        String ratingData = ratingFileReader.readString();
        String[] ratingList = ratingData.split("\r\n");

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
            UserGroup userGroup1 = userGroupService.selectOneById(user.getGroupId());
            if (userGroup1 != null) {
                userNew.setGroupName(userGroup1.getGroupName());
            } else {
                userNew.setGroupName("");
            }
            userNew.setLastLoginTime(user.getLastLoginTime());
            userNew.setRegisterTime(user.getRegisterTime());
            ActivateEmail activateEmail = activateEmailService.selectOneByEmail(user.getEmail());
            userNew.setIsActivate(activateEmail.getIsActivate() == 1);
            for (String item : certList) {
                String[] itemInfo = item.split(" ");
                if (itemInfo[0].equals(user.getUserCall())) {
                    for (String ratingItem : ratingList) {
                        String[] ratingItemInfo = ratingItem.split(" ");
                        if (ratingItemInfo[0].equals(itemInfo[2].trim())) {
                            userNew.setRatingId(new Integer(ratingItemInfo[0].trim()));
                            userNew.setRatingName(ratingItemInfo[2].trim());
                            userNew.setRatingNameEn(ratingItemInfo[1].trim());
                        }
                    }
                }
            }
            userNewList.add(userNew);
        }
        return ResultJson.ok().data("size", userNewList.size()).data("data", userNewList);
    }

    @GetMapping("/get/user/info")
    public ResultJson getUserInfo(String token, Integer userId) {
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
        User user = userService.selectOneById(userId);
        if (user == null) {
            return ResultJson.error().message("用户不存在");
        }
        Map<String, Object> data = MapAndEntyUtil.entityToMap(user);
        UserGroup userGroup1 = userGroupService.selectOneById(user.getGroupId());
        ActivateEmail activateEmail = activateEmailService.selectOneByEmail(user.getEmail());

        FileReader fileReader1 = new FileReader(FileUtil.file(confPath + "cert.txt"));
        String result = fileReader1.readString();
        String[] certList = result.split("\r\n");
        FileReader ratingFileReader = new FileReader(FileUtil.file(rootPath + "rating.txt"));
        String ratingData = ratingFileReader.readString();
        String[] ratingList = ratingData.split("\r\n");
        for (String item : certList) {
            String[] itemInfo = item.split(" ");
            if (itemInfo[0].equals(user.getUserCall())) {
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
        data.putIfAbsent("ratingId", null);
        data.putIfAbsent("ratingName", null);
        data.putIfAbsent("ratingNameEn", null);
        data.remove("password");
        data.put("groupName", userGroup1.getGroupName());
        data.put("IsActivate", activateEmail.getIsActivate() == 1);
        return ResultJson.ok().data(data);
    }

    @GetMapping("/get/group/list")
    public ResultJson getGroupList(String token) {
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
        List<UserGroup> userGroupList = userGroupService.list();
        return ResultJson.ok().data("size", userGroupList.size()).data("data", userGroupList);
    }

    @PostMapping("/update/user/info")
    public ResultJson updateUserInfo(String token,Integer userId,String userCall,String password,String username,String email,String qq,Integer groupId,Integer ratingId) {
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
        User user = userService.selectOneById(userId);
        if (user == null) {
            return ResultJson.error().message("用户不存在");
        }
        if (!Objects.equals(email, user.getEmail())) {
            List<User> userList = userService.selectAllByEmail(email);
            if (userList.size() != 0) {
                return ResultJson.error().message("该邮箱已被使用，无法修改");
            }
            activateEmailService.delByEmail(user.getEmail());
            ActivateEmail activateEmail = new ActivateEmail();
            int c = RandomUtil.randomInt(20, 50);
            String sjs = RandomUtil.randomString(c);
            Date date = new Date();
            DateTime newDate2 = DateUtil.offsetMinute(date, 15);
            activateEmail.setEmail(email);
            activateEmail.setSjs(sjs);
            activateEmail.setUsername(username);
            activateEmail.setIsActivate(0);
            activateEmail.setExpireTime(newDate2);
            activateEmailService.save(activateEmail);
            ThreadUtil.execute(() -> {
                Context context = new Context();
                Map<String, Object> emailParam = new HashMap<>();
                emailParam.put("username", user.getUsername());
                emailParam.put("call", user.getUserCall());
                emailParam.put("email", user.getEmail());
                emailParam.put("url", activateUrl + sjs);
                context.setVariable("emailParam", emailParam);
                String emailTemplate = templateEngine.process("mail/ActivateEmail", context);
                MailUtil.send(email, "CTAsim用户中心邮箱验证", emailTemplate, true);
            });
        }

        FileReader fileReader = new FileReader(FileUtil.file(confPath + "cert.txt"));
        String result = fileReader.readString();
        StringBuilder certData = new StringBuilder();
        String[] split = result.split("\r\n");
        for (String item : split) {
            String itemData;
            String[] data = item.split(" ");
            if (data[0].equals(userCall)) {
                itemData = data[0] + " " + data[1] + " " + ratingId + "\r\n";
            } else {
                itemData = data[0] + " " + data[1] + " " + data[2] + "\r\n";
            }
            certData.append(itemData);
        }
        FileWriter writer = new FileWriter(FileUtil.file(confPath + "cert.txt"));
        writer.write(certData.toString());

        User userInfo = new User();
        userInfo.setUserCall(userCall);
        if (password != null) {
            userInfo.setPassword(DigestUtil.md5Hex(password));
        } else {
            userInfo.setPassword(user.getPassword());
        }
        userInfo.setUsername(username);
        userInfo.setEmail(email);
        userInfo.setQq(qq);
        userInfo.setGroupId(groupId);
        boolean res = userService.UpdateUserInfo(userInfo, userId);
        return ResultJson.ok().data("isUpdate", res);
    }

    @GetMapping("/get/rating/list")
    public ResultJson getRatingList(String token) {
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
        FileReader fileReader = new FileReader(FileUtil.file(rootPath + "rating.txt"));
        String result = fileReader.readString();
        List<Rating> ratingData = new ArrayList<>();
        String[] split = result.split("\r\n");
        for (String item : split) {
            String[] data = item.split(" ");
            Rating rating = new Rating();
            rating.setId(Integer.valueOf(data[0]));
            rating.setName(data[2]);
            rating.setNameEn(data[1]);
            ratingData.add(rating);
        }
        return ResultJson.ok().data("data", ratingData);
    }

}
