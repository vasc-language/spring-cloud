package spring.cloud.user.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.cloud.common.exception.BlogException;
import spring.cloud.common.pojo.Result;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler
    public Result handler(Exception e){
        log.error("发生异常, e: {}", e);
        return Result.fail(e.getMessage());
    }
    @ExceptionHandler
    public Result handler(BlogException e){
        log.error("发生异常, e: {}", e);
        return Result.fail(e.getMessage());
    }
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result verifyHandle(Exception e){
        log.error("参数校验失败: {}", e.getMessage());
        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
        //未做判空处理, 自行添加
        String errorMessage = exception.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail("参数校验失败:" + errorMessage);
    }
}