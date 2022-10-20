package com.rmr.dinosaurs.core.auth.security;

import static com.rmr.dinosaurs.core.exception.errorcode.AuthErrorCode.INVALID_TOKEN_PROVIDED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmr.dinosaurs.core.exception.ServiceException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  protected static final ObjectMapper objectMapper = new ObjectMapper();
  protected static final String AUTHORIZATION_HEADER = "Authorization";
  protected static final String BEARER = "Bearer ";
  protected static final String TOKEN_HEADER = "X-USER-TOKEN";
  protected static final List<AntPathRequestMatcher> IGNORED_PATHS = List.of(
      new AntPathRequestMatcher("/api/v1/auth/**"),
      new AntPathRequestMatcher("/swagger-ui/**"),
      new AntPathRequestMatcher("/swagger-resources/**"),
      new AntPathRequestMatcher("/v3/api-docs/**"),
      new AntPathRequestMatcher("/actuator/**"),

      new AntPathRequestMatcher("/api/v1/professions/all", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/api/v1/professions", HttpMethod.GET.name()),

      new AntPathRequestMatcher("/api/v1/providers/*", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/api/v1/providers/all", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/api/v1/providers", HttpMethod.GET.name()),

      new AntPathRequestMatcher("/api/v1/courses", HttpMethod.GET.name()),
      new AntPathRequestMatcher("/api/v1/courses/all", HttpMethod.GET.name())
  );

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenService jwtTokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = resolveToken(request);
      if (Objects.nonNull(token)) {
        if (Strings.isEmpty(token) || !jwtTokenProvider.isTokenValid(token)) {
          customServletFilterExceptionHandler(response);
          return;
        }

        DinoAuthentication auth = jwtTokenService.getDinoAuthenticationByToken(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      SecurityContextHolder.clearContext();
      customServletFilterExceptionHandler(response);
    }
  }

  private void customServletFilterExceptionHandler(HttpServletResponse response)
      throws IOException {
    ServiceException serviceException = new ServiceException(INVALID_TOKEN_PROVIDED);
    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(objectMapper.writeValueAsString(serviceException));
  }

  private String resolveToken(HttpServletRequest request) {
    return Strings.isEmpty(request.getHeader(TOKEN_HEADER))
        ? getBearerToken(request)
        : request.getHeader(TOKEN_HEADER);
  }

  private String getBearerToken(HttpServletRequest request) {
    String authToken = request.getHeader(AUTHORIZATION_HEADER);
    return Strings.isNotEmpty(authToken)
        ? authToken.substring(BEARER.length())
        : Strings.EMPTY;
  }

  @Override
  protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
    return IGNORED_PATHS.stream()
        .anyMatch(antPathRequestMatcher -> antPathRequestMatcher.matches(request));
  }

}
