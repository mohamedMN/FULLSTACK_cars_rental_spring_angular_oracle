import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './auth/components/login/login.component';
import { SignupComponent } from './auth/components/signup/signup.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NzMessageModule } from 'ng-zorro-antd/message';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgZorroImportsModule } from './NgZorroImportsModule';
import { NZ_I18N, en_US } from 'ng-zorro-antd/i18n';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { registerLocaleData } from '@angular/common';
import zh from '@angular/common/locales/zh';
import { UserProfileComponent } from './modules/customer/components/user-profile/user-profile.component';

registerLocaleData(zh);

@NgModule({
  declarations: [AppComponent, LoginComponent, SignupComponent, UserProfileComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NzMessageModule,
    BrowserAnimationsModule,
    NgZorroImportsModule,
    NzDatePickerModule
  ],
  providers: [{ provide: NZ_I18N, useValue: en_US }],
  bootstrap: [AppComponent]
})
export class AppModule { }
