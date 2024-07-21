package dev.sagar.literatihub.book;

import dev.sagar.literatihub.common.PageResponse;
import dev.sagar.literatihub.exception.OperationNotPermittedException;
import dev.sagar.literatihub.file.FileStorageService;
import dev.sagar.literatihub.history.BookTransactionHistory;
import dev.sagar.literatihub.history.BookTransactionHistoryRepository;
import dev.sagar.literatihub.user.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookMapper bookMapper;
  private final BookRepository bookRepository;
  private final BookTransactionHistoryRepository transactionHistoryRepository;
  private final FileStorageService fileStorageService;

  public Integer save(BookRequest request, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Book book = bookMapper.toBook(request);
    book.setOwner(user);
    return bookRepository.save(book).getId();
  }

  public BookResponse findById(Integer bookId) {
    return bookRepository
        .findById(bookId)
        .map(bookMapper::toBookResponse)
        .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
  }

  public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
    List<BookResponse> bookResponse = books.stream().map(bookMapper::toBookResponse).toList();
    return new PageResponse<>(
        bookResponse,
        books.getNumber(),
        books.getSize(),
        books.getTotalElements(),
        books.getTotalPages(),
        books.isFirst(),
        books.isLast());
  }

  public PageResponse<BookResponse> findAllBooksByOwner(
      int page, int size, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<Book> books =
        bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
    List<BookResponse> bookResponse = books.stream().map(bookMapper::toBookResponse).toList();
    return new PageResponse<>(
        bookResponse,
        books.getNumber(),
        books.getSize(),
        books.getTotalElements(),
        books.getTotalPages(),
        books.isFirst(),
        books.isLast());
  }

  public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(
      int page, int size, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<BookTransactionHistory> borrowedBooks =
        transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
    List<BorrowedBookResponse> bookResponse =
        borrowedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();
    return new PageResponse<>(
        bookResponse,
        borrowedBooks.getNumber(),
        borrowedBooks.getSize(),
        borrowedBooks.getTotalElements(),
        borrowedBooks.getTotalPages(),
        borrowedBooks.isFirst(),
        borrowedBooks.isLast());
  }

  public PageResponse<BorrowedBookResponse> findAllReturnedBooks(
      int page, int size, Authentication connectedUser) {
    User user = (User) connectedUser.getPrincipal();
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
    Page<BookTransactionHistory> returnedBooks =
        transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
    List<BorrowedBookResponse> bookResponse =
        returnedBooks.stream().map(bookMapper::toBorrowedBookResponse).toList();
    return new PageResponse<>(
        bookResponse,
        returnedBooks.getNumber(),
        returnedBooks.getSize(),
        returnedBooks.getTotalElements(),
        returnedBooks.getTotalPages(),
        returnedBooks.isFirst(),
        returnedBooks.isLast());
  }

  public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
    User user = (User) connectedUser.getPrincipal();
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You are not the owner of this book");
    }
    book.setShareable(!book.isShareable());
    bookRepository.save(book);
    return bookId;
  }

  public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
    User user = (User) connectedUser.getPrincipal();
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You are not the owner of this book");
    }
    book.setArchived(!book.isArchived());
    bookRepository.save(book);
    return bookId;
  }

  public Integer borrowBook(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
    if (book.isArchived() || !book.isShareable()) {
      throw new OperationNotPermittedException(
          "The requested book cannot be borrowed since it is archived or not shareable");
    }
    User user = (User) connectedUser.getPrincipal();
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot borrow your own book");
    }
    final boolean isAlreadyBorrowed =
        transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
    if (isAlreadyBorrowed) {
      throw new OperationNotPermittedException("Requested book is already borrowed");
    }
    BookTransactionHistory transactionHistory =
        BookTransactionHistory.builder()
            .user(user)
            .book(book)
            .returned(false)
            .returnApproved(false)
            .build();
    return transactionHistoryRepository.save(transactionHistory).getId();
  }

  public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
    if (book.isArchived() || !book.isShareable()) {
      throw new OperationNotPermittedException(
          "The requested book cannot be borrowed since it is archived or not shareable");
    }
    User user = (User) connectedUser.getPrincipal();
    if (Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot return your own book");
    }
    BookTransactionHistory transactionHistory =
        transactionHistoryRepository
            .findByBookIdAndUserId(bookId, user.getId())
            .orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));
    transactionHistory.setReturned(true);
    return transactionHistoryRepository.save(transactionHistory).getId();
  }

  public Integer approveReturnedBorrowedBook(Integer bookId, Authentication connectedUser) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
    if (book.isArchived() || !book.isShareable()) {
      throw new OperationNotPermittedException(
          "The requested book cannot be borrowed since it is archived or not shareable");
    }
    User user = (User) connectedUser.getPrincipal();
    if (!Objects.equals(book.getOwner().getId(), user.getId())) {
      throw new OperationNotPermittedException("You cannot return your own book");
    }
    BookTransactionHistory transactionHistory =
        transactionHistoryRepository
            .findByBookIdAndOwnerId(bookId, user.getId())
            .orElseThrow(
                () ->
                    new OperationNotPermittedException(
                        "The book is not returned yet. You cannot approve its return"));
    transactionHistory.setReturnApproved(true);
    return transactionHistoryRepository.save(transactionHistory).getId();
  }

  public void uploadBookCoverPicture(
      MultipartFile file, Authentication connectedUser, Integer bookId) {
    Book book =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("No book found with id:" + bookId));
    User user = (User) connectedUser.getPrincipal();
    var bookCover = fileStorageService.saveFile(file, user.getId());
    book.setBookCover(bookCover);
    bookRepository.save(book);
  }
}
