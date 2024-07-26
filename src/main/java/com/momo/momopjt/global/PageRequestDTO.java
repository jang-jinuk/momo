package com.momo.momopjt.global;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

  @Builder.Default
  private int page = 1;

  @Builder.Default
  private int size = 10;

  private String type; // 검색 종류

  private String keyword;

  public String[] getTypes() {
    return (this.type == null || type.isEmpty()) ?
        new String[]{} : this.type.split("");
//        null : this.type.split("");        //TODO new String 대신 null 리턴
  }

  public Pageable getPageable(String...props) {

    return PageRequest.of(
        this.page -1,
        this.size,
        Sort.by(props).descending());

  }


  private String link;

  public String getLink() {

    if(link == null){
      StringBuilder builder = new StringBuilder();

      builder.append("page=" + this.page);

      builder.append("&size=" + this.size);


      if(type != null && type.length() > 0){
        builder.append("&type=" + type);
      }

      if(keyword != null){
        builder.append("&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8));
      }

      link = builder.toString();

    }

    return link;

  }




}
