package com.acrobtw.elei.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acrobtw.elei.dto.ActivityCompletionDto;
import com.acrobtw.elei.service.UserService;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final UserService userService;

    // TODO after AuthController /login logic
    // @PostMapping("/complete")
    // public void completeActivity(@RequestBody ActivityCompletionDto dto) {
    //     userService.addExperience(null, dto.activityId(), dto.multiplier());
    // }
}
