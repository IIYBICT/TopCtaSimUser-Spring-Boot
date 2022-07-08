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
 * @TableName rail_activity_user
 */
@TableName(value ="rail_activity_user")
@Data
public class RailActivityUser implements Serializable {
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
    private String type;

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
    private String length;

    /**
     * 
     */
    private String sum;

    /**
     * 
     */
    private String explain;

    /**
     * 
     */
    private Date signTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}