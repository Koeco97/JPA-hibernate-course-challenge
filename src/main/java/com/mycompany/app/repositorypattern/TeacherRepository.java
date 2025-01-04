package com.mycompany.app.repositorypattern;

import com.mycompany.app.entities.Teacher;

public interface TeacherRepository {
    public void add(Teacher teacher);
    public void update(Teacher teacher);
    public void remove(Teacher teacher);

    public Teacher getTeacherById(int id);
}
