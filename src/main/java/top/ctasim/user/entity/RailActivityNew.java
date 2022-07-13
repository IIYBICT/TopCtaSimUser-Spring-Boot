package top.ctasim.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RailActivityNew extends RailActivity implements Serializable {
    private Integer signRailActivitySum;
    private Integer RailActivityDispatchSum;
}
