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
 * @TableName user_rail
 */
@TableName(value ="user_rail")
@Data
public class UserRail implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String railName;

    /**
     * 
     */
    private Integer state;

    /**
     * 
     */
    private Integer activitySum;

    /**
     * 
     */
    private Integer connectSum;

    /**
     * 
     */
    private Date registerTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}