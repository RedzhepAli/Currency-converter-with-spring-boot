package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void validInput(int from, int to){

		assert from < 3;
		assert  from >1;
		assert to < 3;
		assert  to >1;

	}

	@Test
	void contextLoads() {
	}

}
