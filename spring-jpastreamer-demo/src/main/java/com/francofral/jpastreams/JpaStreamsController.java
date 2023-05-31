package com.francofral.jpastreams;

import com.speedment.jpastreamer.application.JPAStreamer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class JpaStreamsController {

    private final JPAStreamer jpaStreamer;
}
