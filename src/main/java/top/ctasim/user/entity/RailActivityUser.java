package top.ctasim.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName rail_activity_user
 */
@TableName(value = "rail_activity_user")
@Data
public class RailActivityUser implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     *
     */
    private String railName;
    /**
     *
     */
    private String username;
    /**
     *
     */
    private Integer activityId;
    /**
     *
     */
    private String busType;
    /**
     *
     */
    private String iocoType;
    /**
     *
     */
    private String bottomType;
    /**
     *
     */
    private String busLength;
    /**
     *
     */
    private String busSum;
    /**
     *
     */
    private String railExplain;
    /**
     *
     */
    private Date signTime;
}