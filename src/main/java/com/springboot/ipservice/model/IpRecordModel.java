package com.springboot.ipservice.model;

import lombok.Data;

@Data
public class IpRecordModel {

  private Long id;
  private String ipType;
  private String ipValue;
  private String firstSeen;
  private Long totalCount;

}
