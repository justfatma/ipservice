package com.springboot.ipservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springboot.ipservice.entity.IpRecord;


@Repository
public interface IpRecordRepository extends JpaRepository<IpRecord, Long> {

  List<IpRecord> findByIpValue(String value);
}
