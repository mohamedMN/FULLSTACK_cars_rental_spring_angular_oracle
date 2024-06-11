import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { NzMessageService } from 'ng-zorro-antd/message';

@Component({
  selector: 'app-get-bookings',
  templateUrl: './get-bookings.component.html',
  styleUrls: ['./get-bookings.component.scss']
})
export class GetBookingsComponent {
  bookings: any[] = [];
  isSpinning = false;

  constructor(
    private adminService: AdminService,
    private message: NzMessageService
  ) {}

  ngOnInit() {
    this.getBookings();
  }

  changeBookingStatus(bookingId: number, status: string) {
    this.adminService.changeBookingStatus(bookingId, status).subscribe(
      () => {
        this.getBookings();
        this.message.success('Booking status changed successfully');
      },
      error => {
        this.message.error('Error changing booking status');
      }
    );
  }

  refundBooking(bookingId: number) {
    this.isSpinning = true;
    this.adminService.refundBooking(bookingId).subscribe(
      () => {
        this.getBookings();
        this.message.success('Refund successful');
        this.isSpinning = false;
      },
      error => {
        this.message.error('Error processing refund');
        this.isSpinning = false;
      }
    );
  }

  private getBookings() {
    this.isSpinning = true;
    this.adminService.getCarBookings().subscribe(bookings => {
      this.bookings = bookings;
      this.isSpinning = false;
    });
  }
}
