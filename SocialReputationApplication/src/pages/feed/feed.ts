import { Component } from '@angular/core';

import { NavController } from 'ionic-angular';
import { FeedService } from '../../providers/feed-service';

@Component({
  selector: 'page-feed',
  templateUrl: 'feed.html'
})
export class FeedPage {
   public itemList: Array<Object>;

  constructor(public navCtrl: NavController, private feedService: FeedService) {
        this.itemList = [];
  }
  tabChanged($event){
    console.log($event);
  }

  ionViewDidEnter(){
    if(this.itemList.length == 0){
     this.feedService.getFeedItems().then((result) => { this.itemList = <Array<Object>> result; console.log(result); });
    }
  }
}
