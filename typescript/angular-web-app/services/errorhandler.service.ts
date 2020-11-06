
import { ToastrService } from 'ngx-toastr';
import { Injectable } from '@angular/core';
import { Toastr } from './toastr.service';
import { LanguageService } from './language.service';
import { Router } from '@angular/router';

@Injectable()
export class ErrorHandlerService {

  private toast: Toastr;

  constructor(
    private toastr: ToastrService,
    private languageService: LanguageService,
    private router: Router
  ) {
    this.toast = new Toastr(this.toastr, this.languageService);
  }

  handle(error, extra?: string ) {
    console.error(error.message);
    if (extra) {
      this.toast.showError(`${extra}: ${error.message}`);
    } else {
      this.toast.showError(error.message);
    }
  }

  noaccess_redirect() {
    this.router.navigate(['/dashboard']);
    this.toast.showError('You do not have access to this module!');
  }

  login_redirect() {
    this.router.navigate(['/login']);
    this.toast.showError('You must login to access the application!');
  }

}