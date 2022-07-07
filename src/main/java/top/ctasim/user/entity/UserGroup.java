package top.ctasim.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user_group
 */
@TableName(value ="user_group")
@Data
public class UserGroup implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String groupName;

    /**
     * 
     */
    private Integer isAdmin;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}