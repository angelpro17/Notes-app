import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import {AuthService} from './service/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('🛡️ AuthGuard verificando acceso a:', state.url);

  if (authService.isLoggedIn()) {
    console.log('✅ Acceso permitido con JWT válido');
    return true;
  } else {
    console.log('❌ Acceso denegado, redirigiendo a login');
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
};
