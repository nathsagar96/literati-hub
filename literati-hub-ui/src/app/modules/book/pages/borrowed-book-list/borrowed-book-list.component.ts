import { Component, OnInit } from '@angular/core';

import { BorrowedBookResponse, FeedbackRequest, PageResponseBorrowedBookResponse } from 'src/app/services/models';
import { BookService, FeedbackService } from 'src/app/services/services';

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrls: ['./borrowed-book-list.component.scss']
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  feedbackRequest: FeedbackRequest = { bookId: 0, comment: '', note: 0 };
  page = 0;
  size = 4;
  selectedBook: BorrowedBookResponse | undefined = undefined;

  constructor(private bookService: BookService,
    private feedbackService: FeedbackService
  ) { }

  ngOnInit(): void {
    this.getAllBorrowedBooks();
  }

  getAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.borrowedBooks = resp;
      }
    });
  }

  returnBorrowedBook(book: any) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBorrowedBook({
      'book-id': this.selectedBook?.id as number
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.getAllBorrowedBooks();
      }
    });
  }
  giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {
      }
    })
  }

  goToFirstPage() {
    this.page = 0;
    this.getAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllBorrowedBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.getAllBorrowedBooks();
  }
  goToNextPage() {
    this.page++;
    this.getAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.borrowedBooks.totalPages as number - 1;
    this.getAllBorrowedBooks();
  }

  get isLastPage(): boolean {
    return this.page === this.borrowedBooks.totalPages as number - 1;
  }
}
