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
 * @TableName change_email
 */
@TableName(value ="change_email")
@Data
public class ChangeEmail implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String sjs;

    /**
     * 
     */
    private Date expireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}