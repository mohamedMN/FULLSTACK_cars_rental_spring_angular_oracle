import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-my-bookings',
  templateUrl: './my-bookings.component.html',
  styleUrls: ['./my-bookings.component.scss']
})
export class MyBookingsComponent {
  bookings: any[] = [];
  isSpinning = false;

  constructor(
    private service: CustomerService,
    private router: Router,
    private message: NzMessageService
  ) {}

  ngOnInit() {
    this.getBookingsByUserId();
  }

  private getBookingsByUserId() {
    this.isSpinning = true;
    this.service.getBookingsByUserId().subscribe(
      data => {
        this.bookings = data;
        this.isSpinning = false;
      },
      error => {
        console.log(error);
        this.isSpinning = false;
      }
    );
  }

  goToReserveCar(carId: number) {
    this.router.navigate([`/customer/book/${carId}`]);
  }

  cancelBooking(bookingId: number) {
    this.isSpinning = true;
    this.service.cancelBooking(bookingId).subscribe(
      () => {
        this.message.success('Booking canceled successfully');
        this.getBookingsByUserId();
      },
      error => {
        console.log(error);
        this.message.error('Failed to cancel booking');
        this.isSpinning = false;
      }
    );
  }
}
