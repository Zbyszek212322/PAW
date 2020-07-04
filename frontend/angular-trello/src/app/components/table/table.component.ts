import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {TableService} from '../../services/table.service';
import {TablePayload} from '../../payloads/table-payload';
import {CardPayload} from '../../payloads/card-payload';
import {CardListPayload} from '../../payloads/card-list-payload';
import {HttpClient} from '@angular/common/http';
import {FormControl} from '@angular/forms';
import {FilePayload} from '../../payloads/file-payload';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {

  table: TablePayload;
  cardLists: CardListPayload[];
  cards: CardPayload[];
  files: FilePayload[];
  permaLink: number;
  name = new FormControl('');
  description = new FormControl('');
  newCardTableId = new FormControl();
  newCardListName = new FormControl('');

  selectedFiles: FileList;
  currentFile: File;

  cardList: number;

  selectedAttachments: FileList;
  currentAttachment: File;

  constructor(private activatedRoute: ActivatedRoute,
              private router: Router,
              private tableService: TableService) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.permaLink = params.id;
      this.newCardTableId.setValue(this.permaLink);
    });
    this.getTable();
  }

  getTable() {
    this.tableService.getTable(this.permaLink).subscribe((data: TablePayload) => {
      this.table = data;
      console.log(this.table.pic);
      this.table.pic = 'data:image/jpeg;base64,' + this.table.pic;
      console.log(this.table.pic);
      this.getCardLists(this.table.id);
      this.getCards(this.table.id);
      this.getFiles();
    }, (error => {
      console.log('Błąd');
    }));
  }

  getCardLists(tableId: number) {
    this.tableService.getCardLists(tableId).subscribe((data: CardListPayload[]) => {
      this.cardLists = data;
    }, (error => {
      console.log('Błąd');
    }));
  }

  addCardList(tableId: number, name: string) {
    this.tableService.addList(tableId, name).subscribe(data => {
      this.name.setValue('');
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + tableId).then(r => true);
    });
  }

  editCardList(tableId: number, cardListId: number, newListName: string, newTableId: number) {
    this.tableService.editCardList(cardListId, newListName, newTableId).subscribe(data => {
      // this.name.setValue('');
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + tableId).then(r => true);
    });
  }

  archiveCardList(ListId: number, TableId: number) {
    this.tableService.archiveCardList(ListId).subscribe(data => {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + TableId).then(r => true);
    });
  }

  moveLeftList(ListId: number, TableId: number) {
    this.tableService.moveLeftList(ListId).subscribe(data => {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + TableId).then(r => true);
    });
  }

  moveRightList(ListId: number, TableId: number) {
    this.tableService.moveRightList(ListId).subscribe(data => {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + TableId).then(r => true);
    });
  }

  addCard(tableId: number, cardListId: number, name: string, desc: string) {
    this.tableService.addCard(cardListId, name, desc).subscribe(data => {
      this.name.setValue('');
      this.description.setValue('');
      this.cardList = null;
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + tableId).then(r => true);
    });
  }

  editCard(tableId: number, cardId: number, name: string, desc: string) {
    this.tableService.editCard(cardId, name, desc).subscribe(data => {
      this.name.setValue('');
      this.description.setValue('');
      this.cardList = null;
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      this.router.onSameUrlNavigation = 'reload';
      this.router.navigateByUrl('/table/' + tableId).then(r => true);
    });
  }

  getCards(tableId: number) {
    this.tableService.getCards(tableId).subscribe((data: CardPayload[]) => {
      this.cards = data;
    }, (error => {
      console.log('Błąd');
    }));
  }

  getFiles() {
    this.tableService.getFiles().subscribe((data: FilePayload[]) => {
      this.files = data;
      console.log('Długoć listy załączników: ', data.length);
    }, (error => {
      console.log('Błąd');
    }));
  }

  deleteFile(tableId: number, fileId: number) {
    this.tableService.deleteFile(fileId).subscribe();
    this.getFiles();
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigateByUrl('/table/' + tableId).then(r => true);
  }

  deleteBackground(tableId: number) {
    this.tableService.deleteImage(tableId).subscribe();
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigateByUrl('/table/' + tableId).then(r => true);
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  }

  upload(tableId: number) {
    this.currentFile = this.selectedFiles.item(0);
    this.tableService.addImage(tableId, this.currentFile).subscribe();

    this.selectedFiles = undefined;

    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigateByUrl('/table/' + tableId).then(r => true);
  }

  selectAttachment(event) {
    this.selectedAttachments = event.target.files;
  }

  uploadAttachment(cardId: number, tableId: number) {
    this.currentAttachment = this.selectedAttachments.item(0);
    this.tableService.addFile(cardId, this.currentAttachment).subscribe();
    this.selectedAttachments = undefined;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigateByUrl('/table/' + tableId).then(r => true);
  }

  downloadFile(fileName: string, pic: string, mimetype: string) {
    this.getFiles();
    // @ts-ignore
    const FileSaver = require('file-saver');

    const blob = new Blob([pic], {type: mimetype});
    // FileSaver.saveAs(blob, fileName, true);
    FileSaver.saveAs(blob, fileName);
  }

}
