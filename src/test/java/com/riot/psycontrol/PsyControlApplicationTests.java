package com.riot.psycontrol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PsyControlApplicationTests {

	@Test
	void contextLoads() {
		assertEquals(42, Integer.sum(19, 23));
	}

}
