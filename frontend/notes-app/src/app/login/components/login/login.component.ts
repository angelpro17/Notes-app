import { Component } from '@angular/core';
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { AuthService } from "../../service/auth.service";
import { Router } from "@angular/router";
import { MatIconModule } from "@angular/material/icon";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  showPassword = false;
  isLoading = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  clearError() {
    this.errorMessage = '';
  }

  onSubmit() {
    console.log('🔄 Iniciando proceso de login...');
    this.errorMessage = '';

    if (!this.username.trim() || !this.password.trim()) {
      this.errorMessage = 'Por favor, completa todos los campos.';
      return;
    }

    if (!this.isValidEmail(this.username)) {
      this.errorMessage = 'Por favor, ingresa un correo electrónico válido.';
      return;
    }

    this.isLoading = true;

    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Login exitoso. Respuesta del servidor:', response);
        this.router.navigate(['/notes']);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error en el proceso de login:', error);

        if (error.status === 401) {
          this.errorMessage = 'Credenciales incorrectas. Verifica tu correo y contraseña.';
        } else if (error.status === 404) {
          this.errorMessage = 'Usuario no encontrado. ¿Necesitas crear una cuenta?';
        } else if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica tu conexión.';
        } else {
          this.errorMessage = 'Error al iniciar sesión. Por favor, intenta nuevamente.';
        }
      }
    });
  }

  private isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
    console.log('👁️ Estado de visibilidad de contraseña:', this.showPassword ? 'Visible' : 'Oculta');
  }

  navigateToRegister() {
    console.log('🔄 Navegando a la página de registro...');
    this.router.navigate(['/register']);
  }
}
