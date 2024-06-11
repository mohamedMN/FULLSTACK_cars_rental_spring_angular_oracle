import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { PostCarComponent } from './components/post-car/post-car.component';
import { NgZorroImportsModule } from '../../NgZorroImportsModule';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateCarComponent } from './components/update-car/update-car.component';
import { GetBookingsComponent } from './components/get-bookings/get-bookings.component';
import { SearchCarComponent } from './components/search-car/search-car.component';
import { StatisticsComponent } from './components/statistics/statistics.component';
import { GoogleChartsModule } from 'angular-google-charts';

@NgModule({
  declarations: [
    AdminDashboardComponent,
    PostCarComponent,
    UpdateCarComponent,
    GetBookingsComponent,
    SearchCarComponent,
    StatisticsComponent  // Declare StatisticsComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    NgZorroImportsModule,
    ReactiveFormsModule,
    FormsModule,
    GoogleChartsModule  // Import GoogleChartsModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]  // Add CUSTOM_ELEMENTS_SCHEMA
})
export class AdminModule {}
