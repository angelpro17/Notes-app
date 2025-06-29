import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule }                       from '@angular/common';
import { FormsModule }                        from '@angular/forms';
import { MatToolbarModule }                   from '@angular/material/toolbar';
import { MatIconModule }                      from '@angular/material/icon';
import { MatFormFieldModule }                 from '@angular/material/form-field';
import { MatSelectModule }                    from '@angular/material/select';
import { MatOptionModule }                    from '@angular/material/core';
import { MatButtonModule }                    from '@angular/material/button';
import { MatTooltipModule }                   from '@angular/material/tooltip';
import { AuthService }                        from '../../../login/service/auth.service';
import {Router} from '@angular/router';

export interface Category {
  id: number;
  name: string;
  color?: string;
}

@Component({
  selector: 'app-toolbar',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatToolbarModule,
    MatIconModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatButtonModule,
    MatTooltipModule,
  ],
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {
  @Input() categories: Category[] = [];
  @Input() selectedCategoryId: number | null = null;
  @Output() categoryChange = new EventEmitter<number|null>();
  @Output() logoutClick    = new EventEmitter<void>();

  currentUser: { username: string } | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    // Asigna el usuario al arrancar
    this.currentUser = this.authService.getLoggedInUser();
  }

  onCategoryChange(id: number|null) {
    this.categoryChange.emit(id);
  }

  logout() {
    this.authService.logout()
    this.router.navigate(["/login"])  }
}
