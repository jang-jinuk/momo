package com.momo.momopjt.userandschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAndScheduleRepository extends JpaRepository<UserAndSchedule, Long> {
}
