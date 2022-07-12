package com.springboot.ipservice.enumaration;

public enum ErrorEnum {

//@formatter:off
  IPTYPE_IPVALUE    (100, "Ip type or value is not valid."), 
  IPTYPE            (101, "Ip type is not valid. Valid ip types:  IPV4   IPV6"), 
  FIRST_SEEN        (102, "First seen format is not valid. Valid formats are  dd-MM-yyyy HH:mm:ss    dd/MM/yyyy HH:mm:ss"
                        + "    dd.MM.yyyy HH:mm:ss    yyyy.MM.dd HH:mm:ss    yyyy-MM-dd HH:mm:ss    yyyy/MM/dd HH:mm:ss"), 
  TOTAL_COUNT       (103, "Total count should be between 0 and 100");

//@formatter:on 
  private final long id;
  private final String message;

  ErrorEnum(long id, String message) {
    this.id = id;
    this.message = message;
  }

  public long getId() {
    return id;
  }

  public String getMessage() {
    return message;
  }
}
