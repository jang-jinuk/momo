package com.momo.momopjt;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Log4j2
class MomoApplicationTests {


    @Test
    void testTemplate() {
      int result=1;
      result+=1;
      System.out.println(result);
      log.info(result);


    }


}