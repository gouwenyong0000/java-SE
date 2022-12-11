package com.atguigu.distributedlock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class DistributedLockApplicationTests {

    @Test
    void contextLoads() {
        LocalDate of = LocalDate.of(2022, 6, 10);
        LocalDate now = LocalDate.now();
        Period between = Period.between(of, now);
        System.out.println("between.toDays()/7 = " + between.get(ChronoUnit.DAYS) / 7);

    }

}
