import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CandidatService {
   urlBackend="http://localhost:8080/mic1/candidats/"
  constructor(private http: HttpClient) { }

  public sayHello()  {
     return this.http.get<string>(this.urlBackend+"hello")
  }
}
