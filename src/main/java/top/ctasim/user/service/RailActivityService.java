package top.ctasim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ctasim.user.entity.RailActivity;
import top.ctasim.user.entity.RailActivityUser;

import java.util.List;

/**
 * @author iiybict
 * @description 针对表【rail_activity】的数据库操作Service
 * @createDate 2022-07-08 09:29:40
 */
public interface RailActivityService extends IService<RailActivity> {

    boolean addRailActivity(String name, String stage, String time, String line, String section, String start, String end, String iocoAsk, String bottomAsk, String goExplain, String dispatch, String ipPort, String otherExplain, String state);

    boolean updateRailActivity(int id, String name, String stage, String time, String line, String section, String start, String end, String iocoAsk, String bottomAsk, String goExplain, String dispatch, String ipPort, String otherExplain, String state);

    boolean deleteRailActivity(int id);

    boolean delByActivityId(int id);

    RailActivity selectOneById(int id);

    List<RailActivity> selectAllSignRailActivity(String username);

    List<RailActivityUser> selectAllSignRailActivityList(Integer activityId);

    boolean signIsRailActivity(Integer activityId, String username);

    boolean signRailActivity(Integer activityId, String railName, String username, String busType, String iocoType, String bottomType, String busLength, String busSum, String railExplain);

    boolean cancelSignRailActivity(Integer activityId, String railName, String username);
}
