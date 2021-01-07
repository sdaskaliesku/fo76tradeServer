package com.manson.fo76.domain.fed76;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manson.AbstractObject;
import com.manson.domain.fed76.pricing.AuthorResponse;
import com.manson.domain.fed76.pricing.ReviewRatingResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PriceCheckReviewResponse extends AbstractObject {

  @JsonProperty("@context")
  private String context;
  @JsonProperty("@type")
  private String type;

  private AuthorResponse authorResponse;
  private String description;
  private String dateCreated;
  private String url;
  private ReviewRatingResponse reviewRating;
  private PlanReviewResponse review;

}
