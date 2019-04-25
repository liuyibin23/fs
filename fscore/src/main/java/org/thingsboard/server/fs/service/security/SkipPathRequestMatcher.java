package org.thingsboard.server.fs.service.security;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class SkipPathRequestMatcher implements RequestMatcher {
    private OrRequestMatcher skipMatchers;
//    private RequestMatcher processingMatcher;
    private AndRequestMatcher processingMatcher;

    public SkipPathRequestMatcher(/*@NotEmpty*/ List<String> pathsToSkip, List<String> processingPath) {
        List<RequestMatcher> mSkip = pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        List<RequestMatcher> mProcessing =processingPath.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList());
        skipMatchers = new OrRequestMatcher(mSkip);
        processingMatcher = new AndRequestMatcher(mProcessing);
//        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return !skipMatchers.matches(request) && processingMatcher.matches(request);
    }
}
