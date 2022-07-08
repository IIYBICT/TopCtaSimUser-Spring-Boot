package top.ctasim.user.controller;

import org.springframework.web.bind.annotation.*;
import top.ctasim.user.entity.User;
import top.ctasim.user.entity.UserRail;
import top.ctasim.user.entity.UserSession;
import top.ctasim.user.service.UserRailService;
import top.ctasim.user.service.UserService;
import top.ctasim.user.service.UserSessionService;
import top.ctasim.user.utils.MapAndEntyUtil;
import top.ctasim.user.utils.ResultJson;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/rail")
@ResponseBody
public class RailController {

    @Resource
    private UserService userService;

    @Resource
    private UserSessionService userSessionService;

    @Resource
    private UserRailService userRailService;


    /**
     * 注册连线账号
     *
     * @param token    用户token
     * @param railName 连线账号名称
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResultJson register(
            @RequestParam("token") String token,
            @RequestParam("railName") String railName
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserRail userRailInfo = userRailService.findOneByUsername(userData.getUsername());
        if (userRailInfo != null) {
            return ResultJson.error().message("已经注册过了").code(202);
        }
        List<UserRail> userRails = userRailService.findAllByRailName(railName);
        if (userRails.size() > 0) {
            return ResultJson.error().message("该连线账号已被使用或已记录为不可使用账号，请重新填写").code(202);
        }
        UserRail userRail = new UserRail();
        userRail.setUsername(userData.getUsername());
        userRail.setRailName(railName);
        userRail.setActivitySum(0);
        userRail.setConnectSum(0);
        userRail.setState(0);
        userRail.setRegisterTime(new Date());
        userRailService.save(userRail);
        return ResultJson.ok().data("isRegister", true);
    }

    /**
     * 获取连线账号信息
     *
     * @param token 用户token
     * @return 连线账号信息
     */
    @GetMapping("/info")
    public ResultJson getInfo(
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
        UserRail userRailInfo = userRailService.findOneByUsername(userData.getUsername());
        if (userRailInfo == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        return ResultJson.ok().data(MapAndEntyUtil.entityToMap(userRailInfo));
    }

}
