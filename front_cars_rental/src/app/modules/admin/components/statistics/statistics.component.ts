import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { ChartType } from 'angular-google-charts'; // Import ChartType enum

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {
  userStatistics: any = {};
  bookingStatistics: any = {};
  isSpinning = false;

  userChart = {
    title: 'User Statistics',
    type: ChartType.PieChart, // Use ChartType enum
    data: [] as any[],
    columns: ['Statistic', 'Count'],
    options: {
      pieHole: 0.4,
    },
    width: 400, // Initialize width as number
    height: 400 // Initialize height as number
  };

  bookingChart = {
    title: 'Booking Statistics',
    type: ChartType.PieChart, // Use ChartType enum
    data: [] as any[],
    columns: ['Status', 'Count'],
    options: {
      pieHole: 0.4,
    },
    width: 400, // Initialize width as number
    height: 400 // Initialize height as number
  };

  constructor(
    private adminService: AdminService,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  private loadStatistics(): void {
    this.isSpinning = true;
    this.adminService.getUserStatistics().subscribe(
      (data) => {
        this.userStatistics = data;
        this.userChart.data = [
          ['Total Users', data.totalUsers],
          ['Users with Bookings', data.usersWithBookings]
        ];
        this.isSpinning = false;
      },
      (error) => {
        this.message.error('Error loading user statistics');
        this.isSpinning = false;
      }
    );

    this.adminService.getBookingStatistics().subscribe(
      (data) => {
        this.bookingStatistics = data;
        this.bookingChart.data = [
          ['Pending', data.pendingBookings],
          ['Approved', data.approvedBookings],
          ['Rejected', data.rejectedBookings]
        ];
        this.isSpinning = false;
      },
      (error) => {
        this.message.error('Error loading booking statistics');
        this.isSpinning = false;
      }
    );
  }
}
