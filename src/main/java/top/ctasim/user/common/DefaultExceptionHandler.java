package top.ctasim.user.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import top.ctasim.user.utils.ResultJson;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class DefaultExceptionHandler {
    /**
     * 缺少必要的参数
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ResultJson> missingParameterHandler(HttpServletRequest request, MissingServletRequestParameterException e) {
        this.logError(request, e);
        return new ResponseEntity<ResultJson>(ResultJson.error().message("缺少必要的参数:" + e.getParameterName()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResultJson> methodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        this.logError(request, e);
        return new ResponseEntity<ResultJson>(ResultJson.error().message("参数类型不匹配"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 不支持的请求方法
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultJson> httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        this.logError(request, e);
        return new ResponseEntity<ResultJson>(ResultJson.error().message("不支持该请求方法"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResultJson> validateException(HttpServletRequest request, MethodArgumentNotValidException e) {
        this.logError(request, e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> list = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            if (error.getField().equals("value")) {
                list.add(error.getField() + error.getDefaultMessage());
            }
        }
//        return DataResult.custom(500, "参数有误！", list.get(0));
        return new ResponseEntity<ResultJson>(ResultJson.error().message("参数错误," + list.get(0)), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ResultJson> validateException(HttpServletRequest request, ConstraintViolationException e) {
        this.logError(request, e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> list = new ArrayList<>();
        for (ConstraintViolation<?> item : violations) {
            list.add(item.getMessage());
        }
//        return DataResult.custom(500, "参数有误！", list.get(0));
        return new ResponseEntity<ResultJson>(ResultJson.error().message("参数错误," + list.get(0)), HttpStatus.ACCEPTED);
    }

    /**
     * 其他异常统一处理
     */
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<ResultJson> exception(HttpServletRequest request, Exception e) {
//        this.logError(request, e);
//        return new ResponseEntity<ResultJson>(ResultJson.error().message("错误请求"), HttpStatus.BAD_REQUEST);
//    }

    /**
     * 记录错误日志
     */
    private void logError(HttpServletRequest request, Exception e) {
//        System.out.println("path:{}, queryParam:{}, errorMessage:{}", request.getRequestURI(), request.getQueryString(), e.getMessage(), e).error();
        System.out.println("path:" + request.getRequestURI() + ", queryParam:" + request.getQueryString() + ", errorMessage:" + e.getMessage());
    }
}
