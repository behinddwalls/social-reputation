import { NgModule, ErrorHandler } from '@angular/core';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { TabsPage } from '../pages/tabs/tabs';

import { CreateActivityPage } from '../pages/create/create-activity';
import { ProfilePage } from '../pages/profile/profile';
import { FeedPage } from '../pages/feed/feed';
import { FeedService } from '../providers/feed-service';


@NgModule({
  declarations: [
    MyApp,
    FeedPage,
    ProfilePage,
    CreateActivityPage,
    TabsPage
  ],
  imports: [
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    FeedPage,
    ProfilePage,
    CreateActivityPage,
    TabsPage
  ],
  providers: [{provide: ErrorHandler, useClass: IonicErrorHandler}, FeedService]
})
export class AppModule {}
