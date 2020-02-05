package com.wallet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HelloWorldTests {

    @Test
    public void testHelloWorld(){
        assertEquals(1,1);
    }
}
