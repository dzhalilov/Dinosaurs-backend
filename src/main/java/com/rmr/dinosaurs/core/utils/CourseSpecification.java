package com.rmr.dinosaurs.core.utils;

import com.rmr.dinosaurs.core.model.Course;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@Or({
    @Spec(path = "title", params = "search", spec = LikeIgnoreCase.class),
    @Spec(path = "description", params = "search", spec = LikeIgnoreCase.class)
})
public interface CourseSpecification extends Specification<Course> {
}
