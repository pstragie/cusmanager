/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxcusmanager;

/**
 *
 * @author pieter
 */

import java.util.List;
import javafxcusmanager.Schedule;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "schedules")
public class ScheduleListWrapper {
    
    //@XmlRootElement(name = "gameSchedule")

    private List<Schedule> schedules;

    @XmlElement(name = "schedule")
    public List<Schedule> getSchedule() {
        return schedules;
    }

    public void setSchedule(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}




