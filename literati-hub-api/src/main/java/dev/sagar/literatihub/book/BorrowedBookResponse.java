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
public class BorrowedBookResponse {
  private Integer id;
  private String title;
  private String authorName;
  private String isbn;
  private double rating;
  private boolean returned;
  private boolean returnApproved;
}
