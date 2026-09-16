package com.practice.firstspringai.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.firstspringai.dummyData.Helper;
import com.practice.firstspringai.service.VectorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/springai")
@RequiredArgsConstructor
@Slf4j
public class UploadDBController {

	@Autowired 
	private VectorService vectorService;
    
    @GetMapping("/upload")
    public String upload() {
        vectorService.saveData(Helper.getData());
        return "Saved data inside vector store";
    }
}
