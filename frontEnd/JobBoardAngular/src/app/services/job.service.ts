import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  urlBackend="http://localhost:8080/mic2/job/"
  constructor(private http: HttpClient) { }
  public sayHello() {
    return this.http.get(this.urlBackend+"hello")
  }
}
