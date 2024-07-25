package com.momo.momopjt.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleImageDTO {
  //632p
  private String uuid;
  private String extension;
  private int ord;

}
