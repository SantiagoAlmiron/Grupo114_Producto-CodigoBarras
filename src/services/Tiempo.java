/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.time.ZonedDateTime;
import java.time.ZoneId;

/**
 *
 * @author santiago
 */
public class Tiempo { 
    public static final ZoneId TIMEZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    public static ZonedDateTime ahora() {
        return ZonedDateTime.now(TIMEZONE);
    }
}
