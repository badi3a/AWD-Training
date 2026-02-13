import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CandidatService {
  urlBackend="http://localhost:8081/mic1/candidats";

  constructor(private http: HttpClient) { }

  public sayHello(): Observable<string> {
    return this.http.get<string>(this.urlBackend, { responseType: 'text' as 'json' });
  }
}
