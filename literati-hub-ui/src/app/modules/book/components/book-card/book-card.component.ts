import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from 'src/app/services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss']
})
export class BookCardComponent {

  private _book: BookResponse = {}
  private _manage = false;
  private _bookCover: string | undefined;

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  get bookCover(): string | undefined {
    if (this._book.cover) {
      return `data:image/jpeg;base64,${this._book.cover}`
    }
    return this._bookCover;
  }

  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  @Output() private share: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private wishlist: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter();

  onShowDetails() {
    this.details.emit(this._book);
  }
  onBorrow() {
    this.borrow.emit(this._book);
  }

  onArchive() {
    this.archive.emit(this._book);
  }
  onShare() {
    this.share.emit(this._book);
  }
  onEdit() {
    this.edit.emit(this._book);
  }
  onAddToWishlist() {
    this.wishlist.emit(this._book);
  }


}
