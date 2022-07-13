package com.springboot.ipservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.ipservice.entity.IpRecord;
import com.springboot.ipservice.enumaration.ErrorEnum;
import com.springboot.ipservice.enumaration.IpType;
import com.springboot.ipservice.model.GeneralResponse;
import com.springboot.ipservice.model.IpRecordModel;
import com.springboot.ipservice.repository.IpRecordRepository;


@Service
public class IpRecordService {

  Logger logger = LoggerFactory.getLogger(IpRecordService.class);

  @Autowired
  private IpRecordRepository ipRecordRepository;

  @Autowired
  private DateValidationService dateValidationService;

  public GeneralResponse saveIpRecord(IpRecordModel model) {

    GeneralResponse response = validateModel(model);
    if (response.getErrorCode() != 0) {
      return response;
    }

    response = new GeneralResponse();

    model.setFirstSeen(dateValidationService.convertStringToIsoFormat(model.getFirstSeen()));

    IpRecord ipRecord = fromModelToEntity(model);

    try {
      IpRecord savedIpRecord = ipRecordRepository.save(ipRecord);

      logger.info("saveIpRecord " + ipRecord.getIpValue() + " is saved successfully.");

      response.setData(savedIpRecord.getId());
      response.setErrorCode(0L);
      response.setErrorMessage("");
    } catch (ConstraintViolationException e) {
      response.setData("");
      response.setErrorCode(102L);
      response.setErrorMessage(e.getConstraintViolations().toString());
      logger.debug("saveIpRecord  constraint violation");
    }
    return response;
  }


  public GeneralResponse getIpRecordByValue(String ipValue) {
    List<IpRecord> ipRecordList = ipRecordRepository.findByIpValue(ipValue);

    GeneralResponse response = new GeneralResponse();
    response.setErrorCode(0L);
    response.setErrorMessage("");

    if (ipRecordList.isEmpty()) {
      response.setData("No data found");
    } else {

      List<IpRecordModel> modelList = new ArrayList<IpRecordModel>();

      ipRecordList.forEach(f -> {
        modelList.add(fromEntityToModel(f));
      });
      response.setData(modelList);
    }

    return response;
  }

  private GeneralResponse validateModel(IpRecordModel model) {

    GeneralResponse response = new GeneralResponse();
    response.setErrorCode(0L);
    response.setErrorMessage("");
    response.setData("");

    if (Arrays.stream(IpType.values()).noneMatch(t -> t.name().equals(model.getIpType()))) {
      response.setErrorCode(ErrorEnum.IPTYPE.getId());
      response.setErrorMessage(ErrorEnum.IPTYPE.getMessage());
      response.setData("");
      logger.info("validateModel " + model.getIpType() + " is not valid.");
      return response;
    }

    if (!validateIp(model.getIpType(), model.getIpValue())) {
      response.setErrorCode(ErrorEnum.IPTYPE_IPVALUE.getId());
      response.setErrorMessage(ErrorEnum.IPTYPE_IPVALUE.getMessage());
      response.setData("");
      logger.info(
          "validateModel " + model.getIpType() + " " + model.getIpValue() + " are not valid.");
      return response;
    }

    String isoDate = dateValidationService.convertStringToIsoFormat(model.getFirstSeen());
    if (isoDate == null) {
      response.setErrorCode(ErrorEnum.FIRST_SEEN.getId());
      response.setErrorMessage(ErrorEnum.FIRST_SEEN.getMessage());
      response.setData("");
      logger.info("validateModel " + model.getFirstSeen() + " is not valid.");
      return response;
    }

    if (!(model.getTotalCount() <= 100 && model.getTotalCount() >= 0)) {
      response.setErrorCode(ErrorEnum.TOTAL_COUNT.getId());
      response.setErrorMessage(ErrorEnum.TOTAL_COUNT.getMessage());
      response.setData("");
      logger.info("validateModel " + model.getTotalCount() + " is not valid.");
      return response;
    }

    return response;
  }

  private boolean validateIp(String ipType, String ipValue) {

    InetAddressValidator validator = InetAddressValidator.getInstance();

    if (ipType.equals(IpType.IPV4.name())) {
      return validator.isValidInet4Address(ipValue);
    } else if (ipType.equals(IpType.IPV6.name())) {
      return validator.isValidInet6Address(ipValue);
    }

    return false;
  }



  private IpRecord fromModelToEntity(IpRecordModel model) {

    IpRecord ipRecord = new IpRecord();
    ipRecord.setId(model.getId());
    ipRecord.setFirstSeen(model.getFirstSeen());
    ipRecord.setIpType(IpType.valueOf(model.getIpType()));
    ipRecord.setTotalCount(model.getTotalCount());
    ipRecord.setIpValue(model.getIpValue());

    return ipRecord;
  }

  private IpRecordModel fromEntityToModel(IpRecord ipRecord) {

    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen(ipRecord.getFirstSeen());
    model.setId(ipRecord.getId());
    model.setIpType(ipRecord.getIpType().name());
    model.setTotalCount(ipRecord.getTotalCount());
    model.setIpValue(ipRecord.getIpValue());

    return model;
  }
}
