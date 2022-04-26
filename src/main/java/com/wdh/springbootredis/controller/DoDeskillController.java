package com.wdh.springbootredis.controller;

import com.wdh.springbootredis.service.DoDeskillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
@Slf4j
public class DoDeskillController {

    private final DoDeskillService doDeskillService;

    public DoDeskillController(DoDeskillService doDeskillService) {
        this.doDeskillService = doDeskillService;
    }

    @PostMapping("/deskill")
    @ResponseBody
    public String deskill(@RequestParam("inventoryId") String inventoryId){
        String userId = getUserId();
        boolean deskill = doDeskillService.isDeskill(userId, inventoryId);
        return "";
    }

    private String getUserId() {
        Random random = new Random();
        return String.valueOf(random.nextInt(99999));
    }
}
