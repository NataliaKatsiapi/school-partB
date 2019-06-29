package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrainersPerCourse {
    
    private Course course;
    private List<Trainer> trainers;

    public TrainersPerCourse() {
    }

    public TrainersPerCourse(Course course) {
        this.course = course;
        trainers = new ArrayList();
    }

    public TrainersPerCourse(Course course, List<Trainer> trainers) {
        this.course = course;
        this.trainers = trainers;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<Trainer> trainers) {
        this.trainers = trainers;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.course);
        hash = 89 * hash + Objects.hashCode(this.trainers);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainersPerCourse other = (TrainersPerCourse) obj;
        if (!Objects.equals(this.course, other.course)) {
            return false;
        }
        if (!Objects.equals(this.trainers, other.trainers)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TrainersPerCourse{" + "course=" + course + ", trainers=" + trainers + '}';
    }
    
    
    
}
