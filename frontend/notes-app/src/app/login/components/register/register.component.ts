import { Component } from '@angular/core';
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { AuthService } from "../../service/auth.service";
import { Router } from "@angular/router";
import { MatIconModule } from "@angular/material/icon";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  username: string = '';
  password: string = '';
  showPassword = false;
  termsAccepted = false;
  isLoading = false;
  errorMessage: string = '';

  // Expresión regular para validar email
  private usernameRegex = /@/;

  constructor(private authService: AuthService, private router: Router) {}

  isUsernameAllowed(username: string): boolean {
    return this.usernameRegex.test(username);
  }

  clearError() {
    this.errorMessage = '';
  }

  onSubmit() {
    console.log('🔄 Iniciando proceso de registro...');
    this.errorMessage = '';

    if (!this.termsAccepted) {
      console.warn('⚠️ El usuario no aceptó los Términos y Condiciones.');
      this.errorMessage = 'Debes aceptar los Términos y Condiciones para continuar.';
      return;
    }

    if (!this.isUsernameAllowed(this.username)) {
      console.warn('⚠️ El correo no cumple con el formato requerido.');
      this.errorMessage = 'El correo electrónico debe tener un formato válido.';
      return;
    }

    if (!this.firstName.trim() || !this.lastName.trim()) {
      console.warn('⚠️ Los campos de nombres y apellidos son obligatorios.');
      this.errorMessage = 'Los campos de nombres y apellidos no pueden estar vacíos.';
      return;
    }

    if (this.password.length < 6) {
      this.errorMessage = 'La contraseña debe tener al menos 6 caracteres.';
      return;
    }

    this.isLoading = true;

    console.log('📤 Enviando datos de registro con rol ROLE_USER...');

    this.authService.register(this.username, this.password, 'ROLE_USER').subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('✅ Registro exitoso. Respuesta del servidor:', response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('❌ Error en el proceso de registro:', error);

        if (error.status === 409) {
          this.errorMessage = 'El usuario ya existe. Intenta con otro correo.';
        } else if (error.status === 400) {
          this.errorMessage = 'Datos inválidos. Verifica la información ingresada.';
        } else if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend esté funcionando.';
        } else {
          this.errorMessage = 'No se pudo registrar el usuario. Por favor, intenta nuevamente.';
        }
      }
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
    console.log('👁️ Estado de visibilidad de contraseña:', this.showPassword ? 'Visible' : 'Oculta');
  }

  navigateToLogin() {
    console.log('🔄 Navegando a la página de inicio de sesión...');
    this.router.navigate(['/login']);
  }
}
