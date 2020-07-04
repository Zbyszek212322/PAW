import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TablePayload} from '../payloads/table-payload';
import {Observable} from 'rxjs';
import {CardPayload} from '../payloads/card-payload';
import {CardListPayload} from '../payloads/card-list-payload';
import {FilePayload} from '../payloads/file-payload';

@Injectable({
  providedIn: 'root'
})
export class TableService {
  apiHostLink: string;

  private jsonPost: { table_id: number; listName: string };
  private jsonEditListPost: { table_id: number; listName: string, cardListId: number };
  private jsonCardPost: { cardName: string, description: string, cardListId: number };
  private jsonCardEditPost: { cardName: string, description: string, cardId: number };
  private jsonImagePost: { tableId: number, file: File };

  constructor(private httpClient: HttpClient) {
    this.apiHostLink = 'http://localhost:8080/api';
  }

  getTableList(): Observable<TablePayload[]> {
    return this.httpClient.get<TablePayload[]>(this.apiHostLink + '/table-list/all');
  }

  getTable(permaLink: number): Observable<TablePayload> {
    return this.httpClient.get<TablePayload>(this.apiHostLink + '/table-list/get/' + permaLink);
  }

  getCardLists(tableId: number): Observable<CardListPayload[]> {
    return this.httpClient.get<CardListPayload[]>(this.apiHostLink + '/card-list/get/table/' + tableId);
  }

  getCards(tableId: number): Observable<CardPayload[]> {
    return this.httpClient.get<CardPayload[]>(this.apiHostLink + '/card/get/table/' + tableId);
  }

  deleteTable(tableId: number): Observable<{}> {
    return this.httpClient.delete<any>(this.apiHostLink + '/table-list/delete/' + tableId);
  }

  addTable(tableName: string): Observable<{}> {
    return this.httpClient.post<any>(this.apiHostLink + '/table-list/add/' + tableName, null);
  }

  updateTable(tableId: number, tableName: string): Observable<{}> {
    return this.httpClient.put<any>(this.apiHostLink + '/table-list/' + tableId + '/' + tableName, null);
  }

  addList(tableId: number, name: string): Observable<{}> {
    this.jsonPost = {
      listName: name,
      table_id: tableId
    };
    return this.httpClient.post<any>(this.apiHostLink + '/card-list/add', this.jsonPost);
  }

  editCardList(ListId: number, newListName: string, newTableId: number): Observable<{}> {
    this.jsonEditListPost = {
      listName: newListName,
      table_id: newTableId,
      cardListId: ListId
    };
    return this.httpClient.put<any>(this.apiHostLink + '/card-list/update', this.jsonEditListPost);
  }

  moveLeftList(listId: number) {
    return this.httpClient.put<any>(this.apiHostLink + '/card-list/moveLeft/' + listId, '');
  }

  moveRightList(listId: number) {
    return this.httpClient.put<any>(this.apiHostLink + '/card-list/moveRight/' + listId, '');
  }


  moveRightCard(cardId: number) {
    return this.httpClient.put<any>(this.apiHostLink + '/card/moveRight/' + cardId, '');
  }

  moveLeftCard(cardId: number) {
    return this.httpClient.put<any>(this.apiHostLink + '/card/moveLeft/' + cardId, '');
  }

  archiveCardList(tableId: number): Observable<CardListPayload[]> {
    return this.httpClient.post<any>(this.apiHostLink + '/card-list/' + tableId + '/archive/', '');
  }

  addCard(cardList: number, name: string, desc: string): Observable<{}> {
    this.jsonCardPost = {
      cardName: name,
      description: desc,
      cardListId: cardList
    };
    return this.httpClient.post<any>(this.apiHostLink + '/card/add', this.jsonCardPost);
  }

  editCard(cardEditId: number, name: string, desc: string): Observable<{}> {
    this.jsonCardEditPost = {
      cardName: name,
      description: desc,
      cardId: cardEditId
    };
    return this.httpClient.put<any>(this.apiHostLink + '/card/update', this.jsonCardEditPost);
  }

  getFiles(): Observable<FilePayload[]> {
    return this.httpClient.get<FilePayload[]>(this.apiHostLink + '/file/all');
  }

  deleteFile(fileId: number) {
    return this.httpClient.delete(this.apiHostLink + '/file/?id=' + fileId);
  }

  addImage(id: number, fileString: File): Observable<{}> {
    const formData: FormData = new FormData();
    formData.append('file', fileString);
    return this.httpClient.post<any>(this.apiHostLink + '/table-list/background/' + id, formData);
  }

  deleteImage(id: number) {
    return this.httpClient.delete(this.apiHostLink + '/table-list/background/?tableId=' + id);
  }

  addFile(cardId: number, fileString: File): Observable<{}> {
    const formData: FormData = new FormData();
    formData.append('file', fileString);
    return this.httpClient.post<any>(this.apiHostLink + '/file/uploadAttachment/' + cardId, formData);
  }

}
