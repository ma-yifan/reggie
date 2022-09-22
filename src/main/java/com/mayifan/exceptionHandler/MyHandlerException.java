package com.mayifan.exceptionHandler;

import com.mayifan.common.R;
import com.mayifan.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice(annotations = {RestController.class,Controller.class})
public class MyHandlerException {
    /*
    对错误进行捕获之后，在方法中我们还需要对错误信息进行捕获，原因是一个exception可能是不同的原因导致的，
    这样才能让我们更好的对不同的错误进行对应的处理
     */
    @ResponseBody
    @ExceptionHandler(DuplicateKeyException.class)
    public R<String> exceptionHandle(DuplicateKeyException e) {
        log.error(e.getMessage());
        if (e.getCause().getMessage().contains("Duplicate entry")) {
            String[] split = e.getCause().getMessage().split(" ");
            String msg = split[2] + "字段值已经存在，请重新输入";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandle(CustomException e) {
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }

}