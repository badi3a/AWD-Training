import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CandidatService} from '../services/candidat.service';

@Component({
  selector: 'app-candidat',
  templateUrl: './candidat.component.html',
  styleUrl: './candidat.component.css'
})
export class CandidatComponent implements OnInit {

  msg:String;
  constructor(private candidat:CandidatService) { }
  ngOnInit(): void {
    this.candidat.sayHello().subscribe(
      (data: string) => {
        this.msg = data;
        console.log('Message reçu:', data);
      },
    );
  }
}
