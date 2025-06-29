import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Solo agregar el token JWT si existe y la request no es para endpoints de autenticación
  if (token && !isAuthEndpoint(req.url)) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });

    console.log('🔐 Enviando solicitud con JWT:', {
      url: authReq.url,
      method: authReq.method,
      hasJWT: true
    });

    return next(authReq);
  }

  console.log('📤 Enviando solicitud sin JWT:', {
    url: req.url,
    method: req.method,
    hasJWT: false
  });

  return next(req);
};

function isAuthEndpoint(url: string): boolean {
  return url.includes('/authentication/sign-in') ||
    url.includes('/authentication/sign-up');
}
