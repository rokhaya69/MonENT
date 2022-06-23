import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MefpaiComponent } from './list/mefpai.component';
import { MefpaiDetailComponent } from './detail/mefpai-detail.component';
import { MefpaiUpdateComponent } from './update/mefpai-update.component';
import { MefpaiDeleteDialogComponent } from './delete/mefpai-delete-dialog.component';
import { MefpaiRoutingModule } from './route/mefpai-routing.module';

@NgModule({
  imports: [SharedModule, MefpaiRoutingModule],
  declarations: [MefpaiComponent, MefpaiDetailComponent, MefpaiUpdateComponent, MefpaiDeleteDialogComponent],
  entryComponents: [MefpaiDeleteDialogComponent],
})
export class MefpaiModule {}
