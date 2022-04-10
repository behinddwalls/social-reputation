import { Component } from '@angular/core';

import { CreateActivityPage } from '../create/create-activity';
import { ProfilePage } from '../profile/profile';
import { FeedPage } from '../feed/feed';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {
  feedTab: any = FeedPage;
  createActivityTab: any = CreateActivityPage;
  profileTab: any = ProfilePage;

  constructor() {

  }

  tabChanged($event){
    console.log($event);
  }

}
