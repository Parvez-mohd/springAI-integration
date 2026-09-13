package com.practice.firstspringai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.practice.firstspringai.dummyData.Helper;
import com.practice.firstspringai.service.VectorService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j 
class FirstspringaiApplicationTests {

	@Autowired 
	private VectorService vectorService;


	@Autowired
	private org.springframework.ai.embedding.EmbeddingModel embeddingModel;

	@Test
	void contextLoads() {
	}


	@Test
	void checkDimensions() {
		float[] vector = embeddingModel.embed("Hello world");
		System.out.println(">>> ACTUAL EMBEDDING DIMENSION: " + vector.length);
	}

	@Test 
	void saveDataToVectorDB() {
		log.info("Saving data to db");
		vectorService.saveData(Helper.getData());
		log.info("data saved");
	}

}
