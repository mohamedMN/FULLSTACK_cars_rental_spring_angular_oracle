import { Component, OnInit } from '@angular/core'
import { CustomerService } from './../../services/customer.service'
import { Router } from '@angular/router'
import { NzMessageService } from 'ng-zorro-antd/message'
import { StorageService } from '../../../../auth/components/services/storage/storage.service'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.scss'
})
export class UserProfileComponent implements OnInit {
  bookings: any[] = []
  userProfile: any
  isSpinning = false

  editMode: boolean = false
  changePasswordMode: boolean = false
  passwordForm: FormGroup

  constructor(
    private CustomerService: CustomerService,
    private router: Router,
    private message: NzMessageService,
    private fb: FormBuilder
  ) {
    this.passwordForm = this.fb.group(
      {
        currentPassword: ['', Validators.required],
        newPassword: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', Validators.required]
      },
      { validator: this.passwordMatchValidator }
    )
  }

  ngOnInit() {
    this.getUserProfile()
  }

  private getUserProfile() {
    const userId = StorageService.getUserId()
      ? Number(StorageService.getUserId())
      : 0

    this.CustomerService.getCustomerProfile(userId).subscribe(
      data => {
        this.userProfile = data
        console.log('Response data:', data) // Log the response data
      },
      error => {
        console.log(error)
      }
    )
  }

  toggleEditMode() {
    this.editMode = !this.editMode
    if (!this.editMode) {
      this.changePasswordMode = false
    }
  }

  togglePasswordMode() {
    this.changePasswordMode = !this.changePasswordMode
  }
  passwordMatchValidator(form: FormGroup) {
    return form.get('newPassword')?.value === form.get('confirmPassword')?.value
      ? null
      : { mismatch: true }
  }

  onSubmit() {
    if (this.changePasswordMode) {
      if (this.passwordForm.valid) {
        const currentPassword = this.passwordForm.get('currentPassword')?.value
        const newPassword = this.passwordForm.get('newPassword')?.value
        // Call a service to change the password
        console.log('Changing password', { currentPassword, newPassword })
      }
    } else {
      const name = (document.getElementById('name') as HTMLInputElement).value
      const email = (document.getElementById('email') as HTMLInputElement).value
      // Call a service to update the profile
      console.log('Updating profile', { name, email })
      this.userProfile.name = name
      this.userProfile.email = email
    }

    this.toggleEditMode()
  }
}
