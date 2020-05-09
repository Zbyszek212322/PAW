import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TablePayload} from '../payloads/table-payload';
import {Observable} from 'rxjs';
import {CardPayload} from '../payloads/card-payload';
import {CardListPayload} from '../payloads/card-list-payload';
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class TableService {

  constructor(private httpClient: HttpClient) { }

  getTableList(): Observable<TablePayload[]> {
    return this.httpClient.get<TablePayload[]>('http://localhost:8080/api/table-list/all');
  }

  getTable(permaLink: number): Observable<TablePayload> {
    return this.httpClient.get<TablePayload>('http://localhost:8080/api/table-list/get/' + permaLink);
  }

  getCardLists(tableId: number): Observable<CardListPayload[]> {
    return this.httpClient.get<CardListPayload[]>('http://localhost:8080/api/card-list/get/table/' + tableId);
  }

  getCards(tableId: number): Observable<CardPayload[]> {
    return this.httpClient.get<CardPayload[]>('http://localhost:8080/api/card/get/table/' + tableId);
  }

}