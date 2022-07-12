package com.springboot.ipservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.ipservice.model.GeneralResponse;
import com.springboot.ipservice.model.IpRecordModel;
import com.springboot.ipservice.service.IpRecordService;

@RestController
public class IpRecordController {


  @Autowired
  private IpRecordService ipRecordService;

  @GetMapping("iprecord/{value}")
  public ResponseEntity<GeneralResponse> getIpRecordByValue(@PathVariable String value) {

    return new ResponseEntity<>(ipRecordService.getIpRecordByValue(value), HttpStatus.OK);
  }

  @PostMapping("iprecord")
  public ResponseEntity<GeneralResponse> saveIpRecord(@RequestBody IpRecordModel model) {

    return new ResponseEntity<>(ipRecordService.saveIpRecord(model), HttpStatus.CREATED);
  }

}
