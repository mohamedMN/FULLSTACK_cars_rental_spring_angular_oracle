import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { CustomerDashboardComponent } from './components/customer-dashboard/customer-dashboard.component'
import { BookCarComponent } from './components/book-car/book-car.component'
import { MyBookingsComponent } from './components/my-bookings/my-bookings.component'
import { UserProfileComponent } from './components/user-profile/user-profile.component'

const routes: Routes = [
  { path: 'dashboard', component: CustomerDashboardComponent },
  { path: 'book/:id', component: BookCarComponent },
  { path: 'my-bookings', component: MyBookingsComponent },
  { path: 'profile', component: UserProfileComponent }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule {}
