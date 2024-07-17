package dev.sagar.literatihub.feedback;

import dev.sagar.literatihub.book.Book;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class FeedbackMapper {

  public Feedback toFeedback(FeedbackRequest request) {
    return Feedback.builder()
        .note(request.note())
        .comment(request.comment())
        .book(Book.builder().id(request.bookId()).archived(false).shareable(false).build())
        .build();
  }

  public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
    return FeedbackResponse.builder()
        .note(feedback.getNote())
        .comment(feedback.getComment())
        .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
        .build();
  }
}
