package top.ctasim.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName rail_activity
 */
@TableName(value ="rail_activity")
@Data
public class RailActivity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 活动名称
     */
    private String railName;

    /**
     * 活动期号
     */
    private String stage;

    /**
     * 活动时间
     */
    private Date activityTime;

    /**
     * 使用线路
     */
    private String line;

    /**
     * 活动区间
     */
    private String section;

    /**
     * 始发站
     */
    private String activityStart;

    /**
     * 终到站
     */
    private String activityEnd;

    /**
     * 机车要求
     */
    private String iocoAsk;

    /**
     * 车底要求
     */
    private String bottomAsk;

    /**
     * 进入说明
     */
    private String goExplain;

    /**
     * 调度
     */
    private String dispatch;

    /**
     * IP端口
     */
    private String ipPort;

    /**
     * 其他说明
     */
    private String otherExplain;

    /**
     * 发布时间
     */
    private Date addTime;

    /**
     * 活动状态
     */
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}