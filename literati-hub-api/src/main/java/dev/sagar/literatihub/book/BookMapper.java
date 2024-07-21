package dev.sagar.literatihub.book;

import dev.sagar.literatihub.file.FileUtils;
import dev.sagar.literatihub.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

  public Book toBook(BookRequest request) {
    return Book.builder()
        .id(request.id())
        .title(request.title())
        .authorName(request.authorName())
        .synopsis(request.synopsis())
        .isbn(request.isbn())
        .archived(false)
        .shareable(request.shareable())
        .build();
  }

  public BookResponse toBookResponse(Book book) {
    return BookResponse.builder()
        .id(book.getId())
        .title(book.getTitle())
        .authorName(book.getAuthorName())
        .isbn(book.getIsbn())
        .synopsis(book.getSynopsis())
        .rating(book.getRating())
        .archived(book.isArchived())
        .shareable(book.isShareable())
        .owner(book.getOwner().getFullName())
        .cover(FileUtils.readFileFromLocation(book.getBookCover()))
        .build();
  }

  public BorrowedBookResponse toBorrowedBookResponse(
      BookTransactionHistory bookTransactionHistory) {
    return BorrowedBookResponse.builder()
        .id(bookTransactionHistory.getBook().getId())
        .title(bookTransactionHistory.getBook().getTitle())
        .authorName(bookTransactionHistory.getBook().getAuthorName())
        .isbn(bookTransactionHistory.getBook().getIsbn())
        .rating(bookTransactionHistory.getBook().getRating())
        .returned(bookTransactionHistory.isReturned())
        .returnApproved(bookTransactionHistory.isReturnApproved())
        .build();
  }
}
