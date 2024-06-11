import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from '../../../../auth/components/services/storage/storage.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import moment from 'moment';
import 'moment/locale/en-gb';

const DATE_FORMAT = 'MM-DD-YYYY';

@Component({
  selector: 'app-book-car',
  templateUrl: './book-car.component.html',
  styleUrls: ['./book-car.component.scss']
})
export class BookCarComponent implements OnInit {
  carId: number;
  car: any;
  validateForm!: FormGroup;
  isSpinning: boolean = false;
  reservedDates: { fromDate: Date, toDate: Date }[] = [];

  constructor(
    private service: CustomerService,
    private activeRoute: ActivatedRoute,
    private fb: FormBuilder,
    private message: NzMessageService,
    private router: Router
  ) {
    this.carId = this.activeRoute.snapshot.params['id'];
    moment.locale('en-gb');  // Set the locale to English
  }

  ngOnInit() {
    this.validateForm = this.fb.group({
      fromDate: [null, Validators.required],
      toDate: [null, Validators.required]
    });

    this.getCarById();
    this.getReservedDates();
  }

  bookACar(data: any) {
    this.isSpinning = true;

    const fromDate = new Date(data.fromDate).getTime();
    const toDate = new Date(data.toDate).getTime();

    let bookACarDto = {
      fromDate: fromDate,
      toDate: toDate,
      userId: StorageService.getUserId(),
      carId: this.carId
    };

    this.service.bookACar(bookACarDto).subscribe(
      res => {
        this.isSpinning = false;
        if (res && res.paymentSessionId) {
          this.redirectToStripe(res.paymentSessionId);
        } else {
          this.message.success('Car booked successfully');
          this.router.navigateByUrl('/customer/dashboard');
        }
      },
      error => {
        this.isSpinning = false;
        this.message.error('Error occurred while booking the car');
      }
    );
  }

  private getCarById() {
    this.service.getCarById(this.carId).subscribe(res => {
      this.car = res;
      this.car.processedImage = `data:image/jpeg;base64,${res.returnedImage}`;
    });
  }

  private getReservedDates() {
    this.service.getReservedDates(this.carId).subscribe(res => {
      this.reservedDates = res;
    });
  }

  public disabledDate = (current: Date): boolean => {
    const today = moment().startOf('day');
    return current < today.toDate() || this.reservedDates.some(dateRange => {
      const reservedFromDate = moment(dateRange.fromDate).startOf('day');
      const reservedToDate = moment(dateRange.toDate).startOf('day');
      return current >= reservedFromDate.toDate() && current <= reservedToDate.toDate();
    });
  };

  public rangeDisabledDate = (current: Date): boolean => {
    const fromDate = this.validateForm.controls['fromDate'].value;
    const today = moment().startOf('day');
    if (!fromDate) {
      return current < today.toDate(); // If fromDate is not selected yet, allow selection except for past dates
    }

    const fromMoment = moment(fromDate).startOf('day');
    return current < fromMoment.toDate() || this.reservedDates.some(dateRange => {
      const reservedFromDate = moment(dateRange.fromDate).startOf('day');
      const reservedToDate = moment(dateRange.toDate).startOf('day');
      return current >= reservedFromDate.toDate() && current <= reservedToDate.toDate() || 
             fromMoment.isBetween(reservedFromDate, reservedToDate, undefined, '[]') || 
             reservedFromDate.isBetween(fromMoment, moment(current).startOf('day'), undefined, '[]');
    });
  };

  clearSelection() {
    this.validateForm.reset();
  }

  private redirectToStripe(sessionId: string) {
    const stripe = (window as any).Stripe('pk_test_51PMzfeJ5HViJQ40DznqT9tVMQVpDB2IkN9bhjt32XEcYm9UxDdNS8yPSdVPtxA7bN3v42l49AMC3qnnKs9ww8fkU00qOswbpVm');
    stripe.redirectToCheckout({ sessionId: sessionId }).then((result: any) => {
      if (result.error) {
        this.message.error(result.error.message);
      }
    });
  }
}
