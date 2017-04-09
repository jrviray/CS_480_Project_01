/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpp.edu.cs480.project06;
import javafx.application.Application;
/**
 *
 * @author Hunter
 */
public class Controller extends Application{
   // GUI myGui;
    public Controller() {
        
    }
    public static void main(String[] args) throws InterruptedException{
//        System.out.println("hello");
        GUI myGui = new GUI();


        Runnable guiThread =()->{myGui.startLaunch();
        };
        new Thread(guiThread).start();
        System.out.println("Hello");
    }
}
