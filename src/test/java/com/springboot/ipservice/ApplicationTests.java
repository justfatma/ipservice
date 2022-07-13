package com.springboot.ipservice;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.ipservice.entity.IpRecord;
import com.springboot.ipservice.enumaration.ErrorEnum;
import com.springboot.ipservice.model.GeneralResponse;
import com.springboot.ipservice.model.IpRecordModel;
import com.springboot.ipservice.repository.IpRecordRepository;
import com.springboot.ipservice.service.DateValidationService;
import com.springboot.ipservice.service.IpRecordService;

@SpringBootTest
class ApplicationTests {

  @Autowired
  private DateValidationService dateValidationService;

  @Autowired
  private IpRecordService ipRecordService;

  @Autowired
  private IpRecordRepository ipRecordRepository;

  @Test
  void shouldConvertDateToIsoDate() {

  //@formatter:off
    assertAll(
        () -> assertEquals("2021-12-01 11:02:23.000", dateValidationService.convertStringToIsoFormat("01-12-2021 11:02:23")),
        () -> assertEquals("2021-12-01 11:02:23.000", dateValidationService.convertStringToIsoFormat("01.12.2021 11:02:23")),
        () -> assertEquals("2021-12-01 11:02:23.000", dateValidationService.convertStringToIsoFormat("01/12/2021 11:02:23")),
        () -> assertEquals("2021-12-01 15:14:10.000", dateValidationService.convertStringToIsoFormat("2021-12-01 15:14:10")),
        () -> assertEquals("2021-12-01 15:14:10.000", dateValidationService.convertStringToIsoFormat("2021.12.01 15:14:10")),
        () -> assertEquals("2021-12-01 15:14:10.000", dateValidationService.convertStringToIsoFormat("2021/12/01 15:14:10")));
  //@formatter:on 
  }

  @Test
  void shouldInsertIpV4Record() {

    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("17-05-2022 22:34:01");
    model.setIpType("IPV4");
    model.setIpValue("123.34.53.103");
    model.setTotalCount(20L);

    GeneralResponse response = ipRecordService.saveIpRecord(model);
    Long id = (Long) response.getData();
    IpRecord ipRecord = ipRecordRepository.findById(id).get();

    assertAll(() -> assertEquals("2022-05-17 22:34:01.000", ipRecord.getFirstSeen()),
        () -> assertEquals("IPV4", ipRecord.getIpType().name()),
        () -> assertEquals("123.34.53.103", ipRecord.getIpValue()),
        () -> assertEquals(20L, ipRecord.getTotalCount()));
  }


  @Test
  void shouldGetIpV4RecordByIpValue() {

    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("30/07/2022 08:34:01");
    model.setIpType("IPV4");
    model.setIpValue("222.33.55.111");
    model.setTotalCount(30L);

    ipRecordService.saveIpRecord(model);

    GeneralResponse response = ipRecordService.getIpRecordByValue("222.33.55.111");
    List<IpRecordModel> list = (List<IpRecordModel>) response.getData();

    assertAll(() -> assertEquals(0L, response.getErrorCode()), () -> assertTrue(list.size() > 0));
  }

  @Test
  void shouldInsertIpV6Record() {

    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("20.07.2022 09:34:01");
    model.setIpType("IPV6");
    model.setIpValue("2001:0db8:85a3:0000:0000:8a2e:0370:0370");
    model.setTotalCount(20L);

    GeneralResponse response = ipRecordService.saveIpRecord(model);
    Long id = (Long) response.getData();
    IpRecord ipRecord = ipRecordRepository.findById(id).get();

    assertAll(() -> assertEquals("2022-07-20 09:34:01.000", ipRecord.getFirstSeen()),
        () -> assertEquals("IPV6", ipRecord.getIpType().name()),
        () -> assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:0370", ipRecord.getIpValue()),
        () -> assertEquals(20L, ipRecord.getTotalCount()));
  }


  @Test
  void shouldGetIpV6RecordByIpValue() {

    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("2022/02/04 13:30:01");
    model.setIpType("IPV6");
    model.setIpValue("2001:0db8:85a3:0000:0000:8a2e:0370:9999");
    model.setTotalCount(30L);

    ipRecordService.saveIpRecord(model);

    GeneralResponse response =
        ipRecordService.getIpRecordByValue("2001:0db8:85a3:0000:0000:8a2e:0370:9999");
    List<IpRecordModel> list = (List<IpRecordModel>) response.getData();

    assertAll(() -> assertEquals(0L, response.getErrorCode()), () -> assertTrue(list.size() > 0));
  }

  @Test
  void shouldReturnIpTypeIpValueError() {
    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("20.07.2022 09:34:01");
    model.setIpType("IPV4");
    model.setIpValue("2001:0db8:85a3:0000:0000:8a2e:0370:0370");
    model.setTotalCount(20L);

    GeneralResponse response = ipRecordService.saveIpRecord(model);
    assertAll(() -> assertEquals(ErrorEnum.IPTYPE_IPVALUE.getId(), response.getErrorCode()),
        () -> assertEquals(ErrorEnum.IPTYPE_IPVALUE.getMessage(), response.getErrorMessage()));
  }

  @Test
  void shouldReturnIpTypeError() {
    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("20.07.2022 09:34:01");
    model.setIpType("IPV1");
    model.setIpValue("222.33.55.111");
    model.setTotalCount(20L);

    GeneralResponse response = ipRecordService.saveIpRecord(model);
    assertAll(() -> assertEquals(ErrorEnum.IPTYPE.getId(), response.getErrorCode()),
        () -> assertEquals(ErrorEnum.IPTYPE.getMessage(), response.getErrorMessage()));
  }


  @Test
  void shouldReturnFirstSeenError() {
    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("20-07.2022 09:34:01");
    model.setIpType("IPV4");
    model.setIpValue("222.33.55.111");
    model.setTotalCount(20L);

    GeneralResponse response = ipRecordService.saveIpRecord(model);
    assertAll(() -> assertEquals(ErrorEnum.FIRST_SEEN.getId(), response.getErrorCode()),
        () -> assertEquals(ErrorEnum.FIRST_SEEN.getMessage(), response.getErrorMessage()));
  }

  @Test
  void shouldReturnTotalCountError() {
    IpRecordModel model = new IpRecordModel();
    model.setFirstSeen("20.07.2022 09:34:01");
    model.setIpType("IPV4");
    model.setIpValue("222.33.55.111");
    model.setTotalCount(101L);

    GeneralResponse response = ipRecordService.saveIpRecord(model);
    assertAll(() -> assertEquals(ErrorEnum.TOTAL_COUNT.getId(), response.getErrorCode()),
        () -> assertEquals(ErrorEnum.TOTAL_COUNT.getMessage(), response.getErrorMessage()));
  }


}

