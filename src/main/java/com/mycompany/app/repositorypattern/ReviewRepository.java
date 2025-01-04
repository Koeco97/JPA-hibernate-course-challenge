package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.Review;
import com.mycompany.app.entities.Teacher;

import java.util.Map;

public interface ReviewRepository {
    public void add(Review review);
    public void update(Review review);
    public void remove(Review review);

    public Double getAvgRatingForTeacher(String teacherName);
    public Map<String, Double> getAvgRatingsByTeachers();
}
