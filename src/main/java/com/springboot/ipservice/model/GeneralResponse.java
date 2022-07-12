package com.springboot.ipservice.model;

import lombok.Data;

@Data
public class GeneralResponse {

  private Long errorCode;
  private String errorMessage;
  private Object data;

}
