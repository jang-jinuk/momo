package com.momo.momopjt;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@Log4j2
class MomoApplicationTests {


  @BeforeEach
  public void setUp() {
    log.info("----------------- [테스트 시작]-----------------");
  }

  @AfterEach
  public void tearDown() {
    log.info("----------------- [테스트 끝]-----------------");
  }

  @AfterAll
  public static void tearDownAll() {
    log.info("----------------- [전체 테스트 종료]-----------------");


  }

  @Test
  @DisplayName("테스트의 이름 지정")
  void testTRRemplate() {
    int result = 1;


    try {
      log.info("----------------- [try test]-----------------");

      result += 1;

    } catch (Exception e) {
      log.info("----------------- [error catch]-----------------");
      e.printStackTrace();
    }

    System.out.println(result);
    log.info(result);
  }


  @Test
  void classcheck(){
    log.info("--------------- [classcheck]---------------");
    Collection<Character> socialTypes = Arrays.asList('K', 'N', 'G', 'M');
    log.info(socialTypes.getClass().toString());
  }



}