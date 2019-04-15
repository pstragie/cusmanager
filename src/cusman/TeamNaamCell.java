/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cusman;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author pieter
 */
public class TeamNaamCell extends TableCell<Team, String> {
        
        
        
        public TeamNaamCell(Team team) {
            // Commit edit on Enter and cancel on Escape.
            // Note that the default behavior consumes key events, so we must 
            // register this as an event filter to capture it.
            // Consequently, with Enter, the datePicker's value won't yet have been updated, 
            // so commit will sent the wrong value. So we must update it ourselves from the
            // editor's text value.
            
            
            
            // Modify default mouse behavior on date picker:
            // Don't hide popup on single click, just set date
            // On double-click, hide popup and commit edit for editor
            // Must consume event to prevent default hiding behavior, so
            // must update date picker value ourselves.
            
            // Modify key behavior so that enter on a selected cell commits the edit
            // on that cell's date.
            
            
                
                
                
        }
        
        @Override
        public void updateItem(String teamnaam, boolean empty) {
            super.updateItem(teamnaam, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(teamnaam);
                setGraphic(null);
            }
            
        }
        
        @Override
        public void startEdit() {
            super.startEdit();
            
        }
        
        
        
}