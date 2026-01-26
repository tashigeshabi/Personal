package cn.ljrexclusive.modules.user.controller;


import cn.ljrexclusive.modules.user.entity.Test;
import cn.ljrexclusive.modules.user.service.ITestService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Junery
 * @since 2026-01-15
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @Resource
    private ITestService testService;


    @GetMapping("/hello")
    public List<Test> hello(){
        return testService.list();
    }

}
