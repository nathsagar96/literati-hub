package dev.sagar.literatihub.book;

import dev.sagar.literatihub.common.BaseEntity;
import dev.sagar.literatihub.feedback.Feedback;
import dev.sagar.literatihub.history.BookTransactionHistory;
import dev.sagar.literatihub.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

  private String title;
  private String authorName;
  private String isbn;
  private String synopsis;
  private String bookCover;
  private boolean archived;
  private boolean shareable;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  @OneToMany(mappedBy = "book")
  private List<Feedback> feedbacks;

  @OneToMany(mappedBy = "book")
  private List<BookTransactionHistory> histories;

  @Transient
  public double getRating() {
    if (feedbacks == null || feedbacks.isEmpty()) {
      return 0.0;
    }

    var rating = this.feedbacks.stream().mapToDouble(Feedback::getNote).average().orElse(0.0);
    return Math.round(rating * 10.0) / 10.0;
  }
}
