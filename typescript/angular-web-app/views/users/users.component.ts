import { Component, OnInit, OnDestroy, ChangeDetectorRef, ChangeDetectionStrategy, AfterViewChecked } from '@angular/core';
import { Toastr, LanguageService, MiscService, UserService } from '../../services';
import { Subject } from 'rxjs/Subject';
import { Subscription } from 'rxjs/Subscription';
import { ToastrService } from 'ngx-toastr';
import { PageLoadComponent } from '../pageload.component';
import { BsLocaleService } from 'ngx-bootstrap/datepicker';
import { User } from '../../models';

@Component({
  templateUrl: 'users.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class UsersComponent extends PageLoadComponent implements OnInit, OnDestroy, AfterViewChecked {
  private unsubscribe: Subject<any> = new Subject<any>();
  subscription: Subscription;

  users: User[];
  selectedUser: User;
  selectedUserToDelete: User;
  roles: any[];

  updateSelected: boolean;
  newEntryFlag: boolean;

  searchText: string;
  phoneRegEx: RegExp;
  emailRegEx: RegExp;

  constructor(
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef,
    private languageService: LanguageService,
    private localeService: BsLocaleService,
    private miscService: MiscService,
    private userService: UserService) {
    super();

    this.roles =
    [
      { id: 1, nameEn: 'Admin', nameFr: 'Admin'},
      { id: 2, nameEn: 'Manager', nameFr: 'Directeur'},
      { id: 3, nameEn: 'Employee', nameFr: 'Employé'}
    ];
  }

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

  ngOnInit() {

    this.initializeOnLoad();
    this.toast = new Toastr(this.toastr, this.languageService);
    this.close();

    this.languageService.currentMessage.subscribe(LangSelect => {
      this.selectedLang = LangSelect;
      if (this.selectedLang === 'en') {
        this.languageService.setDatePickerLocaleEn(this.localeService);
      } else {
        this.languageService.setDatePickerLocaleFr(this.localeService);
      }
    });

    this.miscService.return.subscribe(res => { if (res) this.close();  });
    this.phoneRegEx = new RegExp('^\d{3}-\d{3}-\d{4}$');
    this.emailRegEx = new RegExp('^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$');
  }

  initializeOnLoad() {
    this.userService.getUserRole().subscribe( role => {
      this.userRoleId =  role;
      if (this.userRoleId !== 1 ) {
        this.userService.noaccess_redirect();
      }
    });
    this.users = [];
    this.userService.getData().subscribe( res => this.users = res);
  }

  ngOnDestroy() {
    this.unsubscribe.next();
    this.unsubscribe.complete();
    this.cdr.detach();
  }

  editEntry(entry) {
    Object.assign(this.selectedUser, entry);
    this.newEntryFlag = true;
    this.updateSelected = true;
    window.scroll(0, 0);
  }

  addEntry() {
    this.newEntryFlag = true;
    this.selectedUser.roleId = 3;
  }

  setUserToDelete(entry) {
    this.selectedUserToDelete = entry;
  }

  resetAll() {
    this.selectedUser = new User();
    this.selectedUser.active = 1;
    this.selectedUserToDelete = new User();
    this.updateSelected = false;
  }

  close() {
    this.newEntryFlag = false;
    this.resetAll();
  }

  isInvalid() {
    return (
      this.selectedUser.email === ''
      || this.selectedUser.email == null
      || this.selectedUser.password === ''
      || this.selectedUser.password == null
      || this.selectedUser.password == null
      || this.selectedUser.firstName === ''
      || this.selectedUser.firstName == null
      || this.selectedUser.lastName === ''
      || this.selectedUser.lastName == null
      || this.selectedUser.roleId == null
      || !this.phoneRegEx.test( this.selectedUser.phone )
      || !this.emailRegEx.test( this.selectedUser.email )
      ) ? true : false;
  }

  create() {
    if (this.users.find( user => user.email === this.selectedUser.email)) {
      this.toast.showError(`${this.selectedUser.email} already exists!`);
    } else {
      this.userService.create(this.selectedUser).then( user => {
        // if no error thrown
        if (user) {
          this.selectedUser = user;
          this.users.push(this.selectedUser);
          this.toast.showSuccess(`${this.selectedUser.email} sucessfully added.`);
          this.close();
        }
      });

    }
  }

  update() {
    this.userService.update(this.selectedUser);
    const index = this.users.findIndex( u => u.id === this.selectedUser.id );
    this.users[index] = this.selectedUser;
    this.close();
  }

  delete() {
    this.userService.delete(this.selectedUserToDelete);
    const index = this.users.findIndex( u => u.id === this.selectedUserToDelete.id );
    this.users.splice(index, 1);
    this.close();
  }

  getRoleDesc(id: number) {
    const role = this.roles.find( r => r.id === id );
    return this.selectedLang === 'en' ? role.nameEn : role.nameFr;
  }

}
