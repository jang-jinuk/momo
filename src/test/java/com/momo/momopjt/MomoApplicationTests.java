package com.momo.momopjt;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Log4j2
class MomoApplicationTests {

  boolean calc1(boolean x1, boolean x2){
    if (x1==x2) { return x1;}
    else {return true;}
  }

  boolean calc2(boolean x3, boolean x4){
    if (x3==x4) { return x3;}
    else {return false;}
  }

  boolean solution (boolean x1, boolean x2, boolean x3, boolean x4){
    return calc2(calc1(x1,x2),calc1(x3,x4));
  }


  @Test
  void tests(){
    boolean result = solution(true,false,false,false);

    System.out.println(result);


  }

  //불필요 삭제 0722 YY

}