import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMefpai } from '../mefpai.model';
import { MefpaiService } from '../service/mefpai.service';

@Component({
  templateUrl: './mefpai-delete-dialog.component.html',
})
export class MefpaiDeleteDialogComponent {
  mefpai?: IMefpai;

  constructor(protected mefpaiService: MefpaiService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mefpaiService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
