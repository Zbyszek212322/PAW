import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LoginPayload} from '../payloads/login-payload';
import {JwtAuthResponse} from '../payloads/JwtAuthResponse';
import {map} from 'rxjs/operators';
import {LocalStorageService} from 'ngx-webstorage';
import {Router} from '@angular/router';
import {RegisterPayload} from '../payloads/register-payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = 'http://localhost:8080/api/auth/';

  constructor(private httpClient: HttpClient, private localStorageService: LocalStorageService,  private router: Router) { }


  register(registerPayload: RegisterPayload): Observable<any> {
    return this.httpClient.post(this.url + 'signup', registerPayload);
  }

  login(loginPayload: LoginPayload): Observable<boolean> {
    return this.httpClient.post<JwtAuthResponse>(this.url + 'login', loginPayload).pipe(map(data => {
      this.localStorageService.store('authenticationToken', data.authenticationToken);
      this.localStorageService.store('username', data.username);
      return true;
    }));
  }

  isAuthenticated(): boolean {
    return this.localStorageService.retrieve('authenticationToken') != null;
  }

  logout() {
    this.localStorageService.clear('authenticationToken');
    this.localStorageService.clear('username');
  }
}
