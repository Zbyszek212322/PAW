import {Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {RegisterPayload} from '../../payloads/register-payload';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  registerPayload: RegisterPayload;

  constructor(private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router) {
    this.registerForm = this.formBuilder.group({
      username: '',
      password: ''
    });
    this.registerPayload = {
      username: '',
      password: ''
    };
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.registerPayload.username = this.registerForm.get('username').value;
    this.registerPayload.password = this.registerForm.get('password').value;

    this.authService.register(this.registerPayload).subscribe(data => {
      this.router.navigateByUrl('/login');
    }, error => {
    });
  }
}
