package it.ettore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    public enum Category {
        Maths,
        Science,
        History,
        Geography,
        Art,
        Music,
        Languages,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private int startingYear;
    @Column(nullable = false)
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "professor_id", nullable = false)
    private User professor;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_request", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> studentsRequesting;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_join", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> studentsJoined;

    public Course(String name, String description, int startingYear, Category category, User professor) {
        this.name = name;
        this.description = description;
        this.startingYear = startingYear;
        this.category = category;
        this.professor = professor;
    }

    public String formatPeriod() {
        return String.format("(%d/%d)", startingYear, startingYear + 1);
    }

    public String getIcon() {
        switch (category) {
            case Maths:
                return "fa fa-calculator";
            case Science:
                return "fa fa-flask";
            case History:
                return "fa fa-book";
            case Geography:
                return "fa fa-globe";
            case Art:
                return "fa fa-palette";
            case Music:
                return "fa fa-music";
            case Languages:
                return "fa-language";
            default:
                return "fa fa-question";
        }
    }

    @Override
    public String toString() {
        return String.format("Course{id=%d,name=%s}", id, name);
    }

    public boolean isStudentRequesting(User student) {
        if (studentsRequesting == null) return false;
        return studentsRequesting.stream().anyMatch(someStudent -> someStudent.getId() == student.getId());
    }

    public boolean isStudentJoined(User student) {
        if (studentsJoined == null) return false;
        return studentsJoined.stream().anyMatch(someStudent -> someStudent.getId() == student.getId());
    }

    public void requestJoin(User student) {
        if (isStudentJoined(student)) {
            throw new IllegalStateException("This student has already joined, no need to request");
        }
        if (isStudentRequesting(student)) {
            throw new IllegalStateException("This student has already requested to join");
        }

        if (studentsRequesting == null) {
            studentsRequesting = new ArrayList<>();
        }
        studentsRequesting.add(student);
    }

    public void acceptStudent(User student) {
        if (!isStudentRequesting(student)) {
            throw new IllegalStateException("This student has no pending request to join this course");
        }
        if (isStudentJoined(student)) {
            throw new IllegalStateException("This student has already been accepted to join the course");
        }

        studentsRequesting.removeIf(someStudent -> someStudent.getId() == student.getId());
        if (studentsJoined == null) {
            studentsJoined = new ArrayList<>();
        }
        studentsJoined.add(student);
    }

    public void rejectStudent(User student) {
        if (!isStudentRequesting(student)) {
            throw new IllegalStateException("This student has no pending request to join this course");
        }
        if (isStudentJoined(student)) {
            throw new IllegalStateException("This student has already been accepted to join the course");
        }

        studentsRequesting.removeIf(someStudent -> someStudent.getId() == student.getId());
    }

    public void removeStudent(User student) {
        if (!isStudentJoined(student)) {
            throw new IllegalStateException("This student hasn't joined the course");
        }

        studentsJoined.removeIf(someStudent -> someStudent.getId() == student.getId());
    }
}
