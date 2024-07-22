import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BookResponse } from 'src/app/services/models/book-response';
import { PageResponseBookResponse } from 'src/app/services/models/page-response-book-response';

import { BookService } from 'src/app/services/services/book.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {

  bookResponse: PageResponseBookResponse = {}
  page = 0;
  size = 4;
  message = ''
  level: 'success' | 'error' = 'success';

  constructor(
    private router: Router,
    private bookService: BookService) { }

  ngOnInit(): void {
    this.getAllBooks();
  }

  private getAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books) => {
        this.bookResponse = books;
      }
    })
  }


  goToFirstPage() {
    this.page = 0;
    this.getAllBooks();
  }

  goToPreviousPage() {
    this.page--;
    this.getAllBooks();
  }

  goToPage(page: number) {
    this.page = page;
    this.getAllBooks();
  }
  goToNextPage() {
    this.page++;
    this.getAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.getAllBooks();
  }

  get isLastPage(): boolean {
    return this.page === this.bookResponse.totalPages as number - 1;
  }


  borrowBook(book: BookResponse) {
    this.message = ''
    this.bookService.borrowBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = 'success';
        this.message = 'Book borrowed successfully';
      }, error: (err) => {
        this.level = 'error';
        this.message = err.error.error;
      }
    });
  }
}
