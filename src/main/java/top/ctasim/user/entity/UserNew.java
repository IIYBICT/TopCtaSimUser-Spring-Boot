package top.ctasim.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserNew extends User implements Serializable {

    private boolean IsActivate;
    private String groupName;
    private String ratingName;
    private String ratingNameEn;
    private Integer ratingId;
    private boolean IsCallActivate;
}
