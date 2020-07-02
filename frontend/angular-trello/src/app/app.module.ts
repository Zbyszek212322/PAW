import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {HttpClientInterceptor} from './http-client-interceptor';
import { LoginComponent } from './components/login/login.component';
import { TableListComponent } from './components/table-list/table-list.component';
import {AuthGuard} from './guards/auth.guard';
import { TableComponent } from './components/table/table.component';
import {TableService} from './services/table.service';
import { ListIdPipe } from './pipes/list-id.pipe';
import { RegisterComponent } from './components/register/register.component';
import {HeaderComponent} from './components/header/header.component';
import {LoggedOutGuard} from './guards/logged-out.guard';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TableListComponent,
    TableComponent,
    ListIdPipe,
    RegisterComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgxWebstorageModule.forRoot(),
    RouterModule.forRoot([
      {path: 'tableList', component: TableListComponent, canActivate: [AuthGuard]},
      {path: 'login', component: LoginComponent, canActivate: [LoggedOutGuard]},
      {path: 'table/:id', component: TableComponent, canActivate: [AuthGuard]},
      {path: 'register', component: RegisterComponent, canActivate: [LoggedOutGuard]},
      {path: '**', redirectTo: '/tableList'},
    ], { onSameUrlNavigation: 'reload' }),
    HttpClientModule
  ],
  providers: [TableService, {provide: HTTP_INTERCEPTORS, useClass: HttpClientInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
