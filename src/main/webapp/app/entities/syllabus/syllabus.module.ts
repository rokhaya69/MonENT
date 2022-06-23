import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SyllabusComponent } from './list/syllabus.component';
import { SyllabusDetailComponent } from './detail/syllabus-detail.component';
import { SyllabusUpdateComponent } from './update/syllabus-update.component';
import { SyllabusDeleteDialogComponent } from './delete/syllabus-delete-dialog.component';
import { SyllabusRoutingModule } from './route/syllabus-routing.module';

@NgModule({
  imports: [SharedModule, SyllabusRoutingModule],
  declarations: [SyllabusComponent, SyllabusDetailComponent, SyllabusUpdateComponent, SyllabusDeleteDialogComponent],
  entryComponents: [SyllabusDeleteDialogComponent],
})
export class SyllabusModule {}
