package top.ctasim.user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ctasim.user.entity.RailActivity;
import top.ctasim.user.entity.RailActivityUser;
import top.ctasim.user.mapper.RailActivityMapper;
import top.ctasim.user.service.RailActivityService;
import top.ctasim.user.service.RailActivityUserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author iiybict
 * @description 针对表【rail_activity】的数据库操作Service实现
 * @createDate 2022-07-08 09:29:40
 */
@Service
@DS("mysql1")
public class RailActivityServiceImpl extends ServiceImpl<RailActivityMapper, RailActivity>
        implements RailActivityService {

    @Resource
    private RailActivityMapper railActivityMapper;

    @Resource
    private RailActivityUserService railActivityUserService;

    @Override
    public boolean addRailActivity(String name, String stage, String time, String line, String section, String start, String end, String iocoAsk, String bottomAsk, String goExplain, String dispatch, String ipPort, String otherExplain, String state) {
        RailActivity railActivity = new RailActivity();
        railActivity.setRailName(name);
        railActivity.setStage(stage);
        railActivity.setActivityTime(DateUtil.parse(time, "yyyy-MM-dd HH:mm:ss"));
        railActivity.setLine(line);
        railActivity.setSection(section);
        railActivity.setActivityStart(start);
        railActivity.setActivityEnd(end);
        railActivity.setIocoAsk(iocoAsk);
        railActivity.setBottomAsk(bottomAsk);
        railActivity.setGoExplain(goExplain);
        railActivity.setDispatch(dispatch);
        railActivity.setIpPort(ipPort);
        railActivity.setOtherExplain(otherExplain);
        railActivity.setAddTime(new Date());
        railActivity.setState(Integer.valueOf(state));
        return railActivityMapper.insert(railActivity) > 0;
    }

    @Override
    public boolean updateRailActivity(int id, String name, String stage, String time, String line, String section, String start, String end, String iocoAsk, String bottomAsk, String goExplain, String dispatch, String ipPort, String otherExplain, String state) {
        RailActivity railActivity = new RailActivity();
        railActivity.setRailName(name);
        railActivity.setStage(stage);
        railActivity.setActivityTime(DateUtil.parse(time, "yyyy-MM-dd HH:mm:ss"));
        railActivity.setLine(line);
        railActivity.setSection(section);
        railActivity.setActivityStart(start);
        railActivity.setActivityEnd(end);
        railActivity.setIocoAsk(iocoAsk);
        railActivity.setBottomAsk(bottomAsk);
        railActivity.setGoExplain(goExplain);
        railActivity.setDispatch(dispatch);
        railActivity.setIpPort(ipPort);
        railActivity.setOtherExplain(otherExplain);
//        railActivity.setAddTime(new Date());
        railActivity.setState(Integer.valueOf(state));
        System.out.println(railActivity);
        UpdateWrapper<RailActivity> updateWrapper = new UpdateWrapper<RailActivity>();
        updateWrapper.eq("id", id);
        return railActivityMapper.update(railActivity, updateWrapper) > 0;
    }

    @Override
    public boolean deleteRailActivity(int id) {

        return railActivityMapper.delById(id) > 0;
    }

    @Override
    public boolean delByActivityId(int id) {
        return railActivityUserService.delByActivityId(id) > 0;
    }

    @Override
    public RailActivity selectOneById(int id) {
        return railActivityMapper.selectOneById(id);
    }

    @Override
    public List<RailActivity> selectAllSignRailActivity(String username) {
        List<RailActivityUser> railActivityUsers = railActivityUserService.selectAllByUsername(username);
        List<RailActivity> railActivities = new ArrayList<>();
        for (RailActivityUser railActivityUser : railActivityUsers) {
            RailActivity railActivity = railActivityMapper.selectOneById(railActivityUser.getActivityId());
            railActivities.add(railActivity);
        }
        return railActivities;
    }

    @Override
    public List<RailActivityUser> selectAllSignRailActivityList(Integer activityId) {
        return railActivityUserService.selectAllByActivityId(activityId);
    }

    @Override
    public boolean signIsRailActivity(Integer activityId, String username) {
        List<RailActivityUser> railActivityUsers = railActivityUserService.selectAllByActivityIdAndRailName(activityId, username);
        return railActivityUsers.size() > 0;
    }

    @Override
    public boolean signRailActivity(Integer activityId, String railName, String username, String busType, String iocoType, String bottomType, String busLength, String busSum, String railExplain) {
        RailActivityUser railActivityUser = new RailActivityUser();
        railActivityUser.setActivityId(activityId);
        railActivityUser.setRailName(railName);
        railActivityUser.setUsername(username);
        railActivityUser.setBusType(busType);
        railActivityUser.setIocoType(iocoType);
        railActivityUser.setBottomType(bottomType);
        railActivityUser.setBusLength(busLength);
        railActivityUser.setBusSum(busSum);
        railActivityUser.setRailExplain(railExplain);
        railActivityUser.setSignTime(new Date());
        return railActivityUserService.save(railActivityUser);
    }

    @Override
    public boolean cancelSignRailActivity(Integer activityId, String railName, String username) {
        int res = railActivityUserService.delByUsernameAndActivityId(username, activityId);
        return res > 0;
    }
}




