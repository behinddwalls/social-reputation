import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';

/*
  Generated class for the FeedService provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class FeedService {

  constructor(public http: Http) {
    console.log('Hello FeedService Provider');
  }

  getFeedItems(){
     return new Promise((resolve, reject) => {
       var dataList = [];

       var data = {};
       data['name'] = 'Preetam';
       data['timestamp'] = '2017-01-20';
       data['data'] = 'Hello World !!!';
       dataList.push(data);
       var data = {};
       data['name'] = 'Preetam';
       data['timestamp'] = '2017-01-20';
       data['data'] = 'Hello World !!!';
       dataList.push(data);
       var data = {};
       data['name'] = 'Preetam';
       data['timestamp'] = '2017-01-20';
       data['data'] = 'Hello World !!!';
       dataList.push(data);
       var data = {};
       data['name'] = 'Preetam';
       data['timestamp'] = '2017-01-20';
       data['data'] = 'Hello World !!!';
       dataList.push(data);
       var data = {};
       data['name'] = 'Preetam';
       data['timestamp'] = '2017-01-20';
       data['data'] = 'Hello World !!!';
       dataList.push(data);
       console.log(dataList);
       resolve(dataList);
     });
  }

}
