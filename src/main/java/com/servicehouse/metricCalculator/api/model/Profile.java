package com.servicehouse.metricCalculator.api.model;

import lombok.*;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Profile  extends BaseEntity{
    String name;
    Month month;
}
