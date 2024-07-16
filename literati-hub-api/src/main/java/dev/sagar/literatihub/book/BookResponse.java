package dev.sagar.literatihub.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
  private Integer id;
  private String title;
  private String authorName;
  private String isbn;
  private String synopsis;
  private String owner;
  private byte[] cover;
  private double rating;
  private boolean archived;
  private boolean shareable;
}
