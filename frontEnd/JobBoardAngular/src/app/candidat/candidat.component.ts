import {Component, OnInit} from '@angular/core';
import {CandidatService} from '../services/candidat.service';

@Component({
  selector: 'app-candidat',
  templateUrl: './candidat.component.html',
  styleUrl: './candidat.component.css'
})
export class CandidatComponent implements OnInit {
    msg:String;
  constructor(private candidat: CandidatService) {
  }
  ngOnInit() {
    this.candidat.sayHello().subscribe(
      (data)=>{this.msg=data})
  }
}
