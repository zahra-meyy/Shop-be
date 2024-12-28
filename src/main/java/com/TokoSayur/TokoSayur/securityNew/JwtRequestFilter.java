package com.TokoSayur.TokoSayur.securityNew;

import com.TokoSayur.TokoSayur.detail.CustomAdminDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomAdminDetails customAdminDetails; // Autowire CustomAdminDetails, bukan CustomAdminDetailsService

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);  // Mengambil token tanpa "Bearer "
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);  // Mendapatkan username dari token
        }

        // Jika username ditemukan dan tidak ada autentikasi yang ada di SecurityContextHolder
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validasi token JWT
            if (jwtTokenUtil.validateToken(jwtToken)) {
                // Memuat user dari database
                UserDetails user = customAdminDetails.loadUserByUsername(username); // Gunakan CustomAdminDetails
                // Membuat objek autentikasi
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Menambahkan detail autentikasi
                SecurityContextHolder.getContext().setAuthentication(auth); // Menyimpan autentikasi ke SecurityContextHolder
            } else {
                SecurityContextHolder.clearContext(); // Hapus konteks jika token tidak valid
            }
        }

        // Melanjutkan filter chain ke filter berikutnya
        filterChain.doFilter(request, response);
    }
}
