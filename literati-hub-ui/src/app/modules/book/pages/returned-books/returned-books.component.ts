import { Component, OnInit } from '@angular/core';
import { BorrowedBookResponse, PageResponseBorrowedBookResponse } from 'src/app/services/models';
import { BookService } from 'src/app/services/services/book.service';

@Component({
  selector: 'app-returned-books',
  templateUrl: './returned-books.component.html',
  styleUrls: ['./returned-books.component.scss']
})
export class ReturnedBooksComponent implements OnInit {
  returnedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 4;
  message = '';
  level: 'success' | 'error' = 'success';

  constructor(private bookService: BookService) { }
  ngOnInit(): void {
    this.getAllReturnedBooks();
  }

  getAllReturnedBooks() {
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (resp) => {
        this.returnedBooks = resp;
      }
    });
  }

  approveBookReturn(book: BorrowedBookResponse) {
    if (!book.returned) {
      this.level = 'error';
      this.message = 'Cannot approve a book return request for a book that has not been returned yet.';
    }

    this.bookService.approveReturnedBorrowedBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Return request approved successfully';
        this.getAllReturnedBooks();
      },
      error: (err) => {
        this.level = 'error';
        this.message = err.error.error;
      }
    });

  }

  goToFirstPage() {
    this.page = 0;
    this.getAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllReturnedBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.getAllReturnedBooks();
  }
  goToNextPage() {
    this.page++;
    this.getAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.getAllReturnedBooks();
  }

  get isLastPage(): boolean {
    return this.page === this.returnedBooks.totalPages as number - 1;
  }
}
