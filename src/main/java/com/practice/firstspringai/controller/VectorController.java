package com.practice.firstspringai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.firstspringai.service.VectorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping ("/vector")
@RequiredArgsConstructor 
@Slf4j 
public class VectorController {
    private final VectorService vectorService;
}
