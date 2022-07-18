package top.ctasim.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Rating implements Serializable {
    private Integer id;
    private String name;
    private String nameEn;

}
