package top.ctasim.user.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.*;
import top.ctasim.user.entity.*;
import top.ctasim.user.service.*;
import top.ctasim.user.utils.ResultJson;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rail/activity")
@ResponseBody
public class RailActivityController {
    @Resource
    private RailActivityService railActivityService;

    @Resource
    private UserRailService userRailService;

    @Resource
    private UserService userService;

    @Resource
    private UserSessionService userSessionService;

    @Resource
    private UserGroupService userGroupService;

    @Resource
    private ActivateEmailService activateEmailService;


    @PostMapping("/add")
    public ResultJson AddRailActivity(
            @RequestParam("token") String token, // 用户token
            @RequestParam("railName") String railName,//活动名称
            @RequestParam("stage") String stage,//活动期号
            @RequestParam("activityTime") String activityTime, //活动时间
            @RequestParam("line") String line,//使用线路
            @RequestParam("section") String section,//活动区间
            @RequestParam("activityStart") String activityStart,//始发站
            @RequestParam("activityEnd") String activityEnd,//终到站
            @RequestParam("iocoAsk") String iocoAsk,//机车要求
            @RequestParam("bottomAsk") String bottomAsk,//车底要求
            @RequestParam("goExplain") String goExplain,//进入说明
            @RequestParam("dispatch") String dispatch,//调度
            @RequestParam("ipPort") String ipPort,//IP端口
            @RequestParam("otherExplain") String otherExplain,//其他说明
            @RequestParam("state") String state //活动状态
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
        boolean res = railActivityService.addRailActivity(railName, stage, activityTime, line, section, activityStart, activityEnd, iocoAsk, bottomAsk, goExplain, dispatch, ipPort, otherExplain, state);
        return ResultJson.ok().data("isAdd", res);
    }

    @PostMapping("/update")
    public ResultJson UpdateRailActivity(
            @RequestParam("token") String token, // 用户token
            @RequestParam("id") String id,//活动id
            @RequestParam("railName") String railName,//活动名称
            @RequestParam("stage") String stage,//活动期号
            @RequestParam("activityTime") String activityTime, //活动时间
            @RequestParam("line") String line,//使用线路
            @RequestParam("section") String section,//活动区间
            @RequestParam("activityStart") String activityStart,//始发站
            @RequestParam("activityEnd") String activityEnd,//终到站
            @RequestParam("iocoAsk") String iocoAsk,//机车要求
            @RequestParam("bottomAsk") String bottomAsk,//车底要求
            @RequestParam("goExplain") String goExplain,//进入说明
            @RequestParam("dispatch") String dispatch,//调度
            @RequestParam("ipPort") String ipPort,//IP端口
            @RequestParam("otherExplain") String otherExplain,//其他说明
            @RequestParam("state") String state //活动状态
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
        boolean res = railActivityService.updateRailActivity(Integer.parseInt(id), railName, stage, activityTime, line, section, activityStart, activityEnd, iocoAsk, bottomAsk, goExplain, dispatch, ipPort, otherExplain, state);
        return ResultJson.ok().data("isUpdate", res);
    }

    @PostMapping("/delete")
    public ResultJson DeleteRailActivity(
            String token, // 用户token
            String id //活动id
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
        boolean res = railActivityService.deleteRailActivity(Integer.parseInt(id));
        railActivityService.delByActivityId(Integer.parseInt(id));
        return ResultJson.ok().data("isDelete", res);
    }

    /**
     * 查询活动详情
     *
     * @param token
     * @param id
     * @return
     */
    @GetMapping("/info")
    public ResultJson GetRailActivityInfo(
            String token, // 用户token
            Integer id //活动id
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        RailActivity railActivity = railActivityService.selectOneById(id);
        if (railActivity == null) {
            return ResultJson.error().message("活动不存在");
        }
        RailActivityNew railActivityNew = new RailActivityNew();
        railActivityNew.setId(railActivity.getId());
        railActivityNew.setDispatch(railActivity.getDispatch());
        railActivityNew.setAddTime(railActivity.getAddTime());
        railActivityNew.setLine(railActivity.getLine());
        railActivityNew.setBottomAsk(railActivity.getBottomAsk());
        railActivityNew.setActivityStart(railActivity.getActivityStart());
        railActivityNew.setActivityTime(railActivity.getActivityTime());
        railActivityNew.setActivityEnd(railActivity.getActivityEnd());
        railActivityNew.setIocoAsk(railActivity.getIocoAsk());
        railActivityNew.setGoExplain(railActivity.getGoExplain());
        railActivityNew.setOtherExplain(railActivity.getOtherExplain());
        railActivityNew.setState(railActivity.getState());
        railActivityNew.setSection(railActivity.getSection());
        railActivityNew.setStage(railActivity.getStage());
        railActivityNew.setIpPort(railActivity.getIpPort());
        railActivityNew.setRailName(railActivity.getRailName());
        railActivityNew.setRailActivityDispatchSum(JSONUtil.parseArray(railActivity.getDispatch()).size());
        railActivityNew.setSignRailActivitySum(railActivityService.selectAllSignRailActivityList(railActivity.getId()).size());
        return ResultJson.ok().data("data", railActivityNew);
    }

    @GetMapping("/list")
    public ResultJson GetRailActivityList(
            String token // 用户token
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        List<RailActivity> railActivityList = railActivityService.list();
        List<RailActivityNew> railActivityNewList = new ArrayList<>();
        for (RailActivity railActivity : railActivityList) {
            RailActivityNew railActivityNew = new RailActivityNew();
            railActivityNew.setId(railActivity.getId());
            railActivityNew.setDispatch(railActivity.getDispatch());
            railActivityNew.setAddTime(railActivity.getAddTime());
            railActivityNew.setLine(railActivity.getLine());
            railActivityNew.setBottomAsk(railActivity.getBottomAsk());
            railActivityNew.setActivityStart(railActivity.getActivityStart());
            railActivityNew.setActivityTime(railActivity.getActivityTime());
            railActivityNew.setActivityEnd(railActivity.getActivityEnd());
            railActivityNew.setIocoAsk(railActivity.getIocoAsk());
            railActivityNew.setGoExplain(railActivity.getGoExplain());
            railActivityNew.setOtherExplain(railActivity.getOtherExplain());
            railActivityNew.setState(railActivity.getState());
            railActivityNew.setSection(railActivity.getSection());
            railActivityNew.setStage(railActivity.getStage());
            railActivityNew.setIpPort(railActivity.getIpPort());
            railActivityNew.setRailName(railActivity.getRailName());
            railActivityNew.setRailActivityDispatchSum(JSONUtil.parseArray(railActivity.getDispatch()).size());
            railActivityNew.setSignRailActivitySum(railActivityService.selectAllSignRailActivityList(railActivity.getId()).size());
            railActivityNewList.add(railActivityNew);
        }
        return ResultJson.ok().data("size", railActivityList.size()).data("data", railActivityNewList);
    }

    @GetMapping("/sign/list")
    public ResultJson GetSignRailActivityList(
            String token // 用户token
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        List<RailActivity> railActivityList = railActivityService.selectAllSignRailActivity(userData.getUsername());
        return ResultJson.ok().data("size", railActivityList.size()).data("data", railActivityList);
    }

    /**
     * 根据活动id获取已报名的人员名单
     *
     * @param token
     * @param activityId
     * @return
     */
    @GetMapping("/sign/all/list")
    public ResultJson GetActivityAllSignList(
            String token, // 用户token
            Integer activityId // 用户token
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        List<RailActivityUser> railActivityList = railActivityService.selectAllSignRailActivityList(activityId);
        return ResultJson.ok().data("size", railActivityList.size()).data("data", railActivityList);
    }

    @PostMapping("/sign/rail/activity")
    public ResultJson SignRailActivity(
            String token, // 用户token
            Integer activityId, // 活动Id
            String busType, // 车型
            String iocoType, // Ioco类型
            String bottomType, // 底板类型
            String busLength, // 车长
            String busSum, // 车数
            String railExplain // 线路说明
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        ActivateEmail activateEmail = activateEmailService.selectOneByEmail(userData.getEmail());
        if (activateEmail.getIsActivate() != 1) {
            return ResultJson.error().message("邮箱未验证，请先验证邮箱");
        }
        UserRail userRailInfo = userRailService.findOneByUsername(userData.getUsername());
        System.out.println(userRailInfo);
        if (railActivityService.signIsRailActivity(activityId, userData.getUsername())) {
            return ResultJson.error().message("已经报名了");
        }
        boolean sign = railActivityService.signRailActivity(activityId, userRailInfo.getRailName(), userData.getUsername(), busType, iocoType, bottomType, busLength, busSum, railExplain);
        return ResultJson.ok().data("isSign", sign);
    }

    @PostMapping("/cancel/sign")
    public ResultJson SignRailActivity(
            String token, // 用户token
            Integer activityId // 活动Id
    ) {
        if (!userService.selectUserLoginExpireByUserIdAndToken(token)) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        UserSession userSession = userSessionService.selectOneByToken(token);
        User userData = userService.selectOneByUsername(userSession.getUsername());
        if (userData == null) {
            return ResultJson.error().message("未登录或已token失效，请重新登录").code(202);
        }
        ActivateEmail activateEmail = activateEmailService.selectOneByEmail(userData.getEmail());
        if (activateEmail.getIsActivate() != 1) {
            return ResultJson.error().message("邮箱未验证，请先验证邮箱");
        }
        UserRail userRailInfo = userRailService.findOneByUsername(userData.getUsername());
        boolean res = railActivityService.cancelSignRailActivity(activityId, userRailInfo.getRailName(), userData.getUsername());
        return ResultJson.ok().data("isCancelSign", true);
    }
}
