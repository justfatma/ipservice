package com.springboot.ipservice.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import com.springboot.ipservice.enumaration.IpType;
import lombok.Data;

@Entity
@Data
public class IpRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private IpType ipType;

  private String ipValue;

  private String firstSeen;

  @Min(value = 0, message = "Total count should not be less than 0")
  @Max(value = 100, message = "Total count should not be greater than 100")
  private Long totalCount;

}
