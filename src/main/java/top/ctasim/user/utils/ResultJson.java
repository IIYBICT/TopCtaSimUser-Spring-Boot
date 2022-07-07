package top.ctasim.user.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultJson {

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    private ResultJson() {
    }

    public static ResultJson ok() {
        ResultJson r = new ResultJson();
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("");
        return r;
    }

    public static ResultJson error() {
        ResultJson r = new ResultJson();
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public static ResultJson unauthorized() {
        ResultJson r = new ResultJson();
        r.setCode(401);
        r.setMessage("账号未登录或Token已过期");
        return r;
    }

    public ResultJson message(String message) {
        this.setMessage(message);
        return this;
    }

    public ResultJson code(Integer code) {
        this.setCode(code);
        return this;
    }

    public ResultJson data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public ResultJson data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }


}
