package com.idc789;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DefultController {

    @Resource
    private Xufei xufei;

    @GetMapping("/test")
    public void test() {
        xufei.operate();
    }

    @GetMapping
    public String operate() {
        return "success";
    }
}
