package dev.sagar.literatihub.feedback;

import dev.sagar.literatihub.book.Book;
import dev.sagar.literatihub.book.BookRepository;
import dev.sagar.literatihub.common.PageResponse;
import dev.sagar.literatihub.exception.OperationNotPermittedException;
import dev.sagar.literatihub.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final BookRepository bookRepository;
  private final FeedbackMapper feedbackMapper;
  private final FeedbackRepository feedbackRepository;

  public Integer save(FeedbackRequest request, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(request.bookId())
            .orElseThrow(
                () -> new EntityNotFoundException("No book found with id:" + request.bookId()));
    if (book.isArchived() || !book.isShareable()) {
      throw new OperationNotPermittedException(
          "You cannot give feedback for an archived or not shareable book");
    }
    User user = (User) connectedUser.getPrincipal();
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot give a feedback to your own book");
    }
    Feedback feedback = feedbackMapper.toFeedback(request);
    return feedbackRepository.save(feedback).getId();
  }

  public PageResponse<FeedbackResponse> findAllFeedbacksByBook(
      Integer bookId, int page, int size, Authentication connectedUser) {
    Pageable pageable = PageRequest.of(page, size);
    User user = (User) connectedUser.getPrincipal();
    Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
    List<FeedbackResponse> feedbackResponses =
        feedbacks.stream()
            .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
            .toList();
    return new PageResponse<>(
        feedbackResponses,
        feedbacks.getNumber(),
        feedbacks.getSize(),
        feedbacks.getTotalElements(),
        feedbacks.getTotalPages(),
        feedbacks.isFirst(),
        feedbacks.isLast());
  }
}
