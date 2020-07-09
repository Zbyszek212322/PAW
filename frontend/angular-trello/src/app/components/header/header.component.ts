import { Component, OnInit } from '@angular/core';
import { AuthService} from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  username: string;

  constructor(public authService: AuthService) {
    authService.getLoggedInName.subscribe(name => this.changeName(name));
   }

  ngOnInit(): void {        
      this.username = this.authService.getUsername();
  }

  private changeName(name: string): void {
    this.username = name;
  }

}
