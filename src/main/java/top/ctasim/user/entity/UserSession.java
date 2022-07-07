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
 * @TableName user_session
 */
@TableName(value ="user_session")
@Data
public class UserSession implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String token;

    private Date loginTime;

    private Date expireTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}