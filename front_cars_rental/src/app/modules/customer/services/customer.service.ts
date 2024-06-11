import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Injectable } from '@angular/core'
import { Observable } from 'rxjs'
import { StorageService } from '../../../auth/components/services/storage/storage.service'

const BASIC_URL = 'http://localhost:8080'

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  constructor(private http: HttpClient) {}

  getCustomerProfile(customerId: number): Observable<any> {
    // console.log('Request headers:', StorageService.getToken())

    return this.http.get(`${BASIC_URL}/api/customer/${customerId}`, {
      headers: this.createAuthorizationHeader()
    })
  }

  updateUserProfile(userProfile: any): Observable<any> {
    return this.http.put(`${BASIC_URL}/api/customer/profile`, userProfile, {
      headers: this.createAuthorizationHeader()
    })
  }

  changePassword(passwordData: any): Observable<any> {
    return this.http.post(
      `${BASIC_URL}/api/customer/change-password`,
      passwordData,
      {
        headers: this.createAuthorizationHeader()
      }
    )
  }

  getAllCars(): Observable<any> {
    return this.http.get(`${BASIC_URL}/api/customer/cars`, {
      headers: this.createAuthorizationHeader()
    })
  }

  getCarById(id: number): Observable<any> {
    return this.http.get(`${BASIC_URL}/api/customer/car/${id}`, {
      headers: this.createAuthorizationHeader()
    })
  }

  bookACar(bookACar: any): Observable<any> {
    return this.http.post(
      `${BASIC_URL}/api/customer/car/book-and-pay`,
      bookACar,
      {
        headers: this.createAuthorizationHeader()
      }
    )
  }

  getBookingsByUserId(): Observable<any> {
    const userId = StorageService.getUserId()
      ? Number(StorageService.getUserId())
      : 0

    return this.http.get(`${BASIC_URL}/api/customer/car/bookings/${userId}`, {
      headers: this.createAuthorizationHeader()
    })
  }

  getReservedDates(carId: number): Observable<any> {
    return this.http.get(
      `${BASIC_URL}/api/customer/car/${carId}/reserved-dates`,
      {
        headers: this.createAuthorizationHeader()
      }
    )
  }

  cancelBooking(bookingId: number): Observable<void> {
    return this.http.delete<void>(
      `${BASIC_URL}/api/customer/car/cancel-booking/${bookingId}`,
      {
        headers: this.createAuthorizationHeader()
      }
    )
  }

  private createAuthorizationHeader(): HttpHeaders {
    let authHeaders: HttpHeaders = new HttpHeaders()
    return authHeaders.set(
      'Authorization',
      `Bearer ${StorageService.getToken()}`
    )
  }
}
