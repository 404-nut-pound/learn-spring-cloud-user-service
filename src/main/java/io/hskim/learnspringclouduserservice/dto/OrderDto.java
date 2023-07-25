package io.hskim.learnspringclouduserservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class OrderDto {

  private String orderId;

  private int qty;

  private int unitPrice;

  private int totalPrice;

  private String userId;

  private String catalogId;

  private String updatedAt;
}
