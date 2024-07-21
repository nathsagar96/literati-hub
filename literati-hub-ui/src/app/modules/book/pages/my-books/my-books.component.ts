import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BookResponse } from 'src/app/services/models/book-response';
import { PageResponseBookResponse } from 'src/app/services/models/page-response-book-response';
import { BookService } from 'src/app/services/services/book.service';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrls: ['./my-books.component.scss']
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {}
  page = 0;
  size = 4;

  constructor(
    private router: Router,
    private bookService: BookService) { }

  ngOnInit(): void {
    this.getAllBooks();
  }

  private getAllBooks() {
    this.bookService.findAllBooksByOwner({
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

  archiveBook(book: BookResponse) {

  }

  shareBook(book: BookResponse) {

  }

  editBook(book: BookResponse) {
  }
}
