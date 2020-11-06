import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { AngularFirestore, AngularFirestoreCollection } from '@angular/fire/firestore';
import { UserCredential } from '@firebase/auth-types';
import firebase from '@firebase/app';

import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { of } from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/takeUntil';

import { User } from '../models/user';

import { ErrorHandlerService } from './errorhandler.service';
import { LanguageService } from './language.service';
import { Toastr } from './toastr.service';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class UserService {
  private dataSubject: BehaviorSubject<User[]> = new BehaviorSubject([]);
  data$: Observable<User[]> = this.dataSubject.asObservable();

  private userCollection: AngularFirestoreCollection;
  private toast: Toastr;
  private userRoleId: String;

  constructor(
    private firestore: AngularFirestore,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,
    private toastr: ToastrService,
    private languageService: LanguageService,
  ) {

    this.toast = new Toastr(this.toastr, this.languageService);
    this.userCollection = this.firestore.collection('users');
  }

  getData(): Observable<User[]> {
    const errorService = this.errorHandlerService;
    return this.userCollection.valueChanges().map(
      docs => {
        // tslint:disable-next-line:prefer-const
        let users = new Array<User>();
        docs.forEach(doc => users.push(JSON.parse(JSON.stringify(doc))));
        return users;
      }
    );
  }

  create(user: User) {
    const errorService = this.errorHandlerService;
    const toast = this.toast;
    const firestore = this.firestore;
    const userCollection = this.userCollection;
    return firebase.auth().createUserWithEmailAndPassword(user.email, user.password)
      .catch(function (error) {
        if (error.code === 'auth/email-already-in-use') {
          toast.showWarning('This account already exists in firebase but has been restored on the application')
          return 1;
        } else {
          errorService.handle(error);
          return -1;
        }
      })
      .then(function (credentials) {
        // if not invalid error
        if (credentials !== -1) {
          switch (credentials) {
            case 1: user.id = firestore.createId(); break;
            default: user.id = (<UserCredential>credentials).user.uid;
          }
          user.password = 'XXXXXXXXXXXX';
          userCollection.doc(user.id).set(JSON.parse(JSON.stringify(user)))
            .catch(function (error) {
              errorService.handle(error);
            });
          return user;
        }
      });
  }

  update(user: User) {
    const errorService = this.errorHandlerService;
    this.userCollection.doc(user.id).update(JSON.parse(JSON.stringify(user)))
      .catch(function (error) {
        errorService.handle(error);
      });
  }

  delete(user: User) {
    const errorService = this.errorHandlerService;
    firebase.auth().signInWithEmailAndPassword(user.email, user.password)
      .then(function (info) {
        firebase.auth().currentUser.delete();
      });
    this.userCollection.doc(user.id).delete()
      .catch(function (error) {
        errorService.handle(error);
      });
  }

  setUserRole(email: string): Observable<number> {
    return this.getData().map(users => {
      const foundUser = users.find(user => user.email === email);
      this.userRoleId = foundUser.roleId;
      return foundUser.roleId;
    });
  }

  getUserRole(): Observable<number> {
    const user = firebase.auth().currentUser;
    if (user) {
      return this.setUserRole(user.email).map(role => role);
    } else {
      this.router.navigate(['/login']);
      return of(null);
    }
  }

  noaccess_redirect() {
    this.errorHandlerService.noaccess_redirect();
  }

  logout(accountNt?: string) {
    firebase.auth().signOut();
    this.router.navigate(['/login']);
    //
  }

}
