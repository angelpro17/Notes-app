import {Component, inject} from '@angular/core';
import {ToolbarComponent} from '../../components/toolbar/toolbar.component';
import {ActivatedRoute, Router} from '@angular/router';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-page-not-found',
  imports: [
  ],
  templateUrl: './page-not-found.component.html',
  styleUrl: './page-not-found.component.css'
})
export class PageNotFoundComponent {
  constructor(private router: Router) {}

  goHome() {
    this.router.navigate(["/notes"])

  }

  goBack() {
    window.history.back()
  }
}
