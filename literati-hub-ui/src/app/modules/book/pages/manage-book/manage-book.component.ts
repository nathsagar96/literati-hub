import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BookRequest } from 'src/app/services/models';
import { BookService } from 'src/app/services/services/book.service';

@Component({
  selector: 'app-manage-book',
  templateUrl: './manage-book.component.html',
  styleUrls: ['./manage-book.component.scss']
})
export class ManageBookComponent {

  bookRequest: BookRequest = { authorName: '', isbn: '', synopsis: '', title: '' };
  errorMsg: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;

  constructor(private bookService: BookService,
    private router: Router
  ) { }

  onFileSelected(event: any): void {
    this.selectedBookCover = event.target.files[0];
    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      };
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.uploadBookCoverPicture({
          'book-id': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-books'])
          }
        })
      },
      error: (err) => {
        this.errorMsg = err.error.validationErrors;
      }
    });
  }
}